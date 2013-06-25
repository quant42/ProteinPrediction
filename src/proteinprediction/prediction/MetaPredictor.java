/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.prediction;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import proteinprediction.ProgramSettings;
import proteinprediction.io.BootstrapModelReader;
import proteinprediction.io.BootstrapModelWriter;
import proteinprediction.utils.DatasetGenerator;
import proteinprediction.utils.DatasetPreprocessor;
import weka.core.Attribute;
import weka.core.Instances;

/**
 *
 * @author shen wei
 */
public class MetaPredictor implements Serializable {
    
    private static final long serialVersionUID = 12532246L;
    
    public static final int ROUNDS = 1000;
    
    private static Attribute RESULT_ATTR = null;
    public static final String RESULT_ATTR_NAME = "MetaPredictor_result";
    public static final String MODEL_NAME = "MetaPredictor.model";
    
    private double[] scores = null;
    
    public final File modelsFile;
    
    public MetaPredictor() {
        modelsFile = new File(ProgramSettings.MODEL_DIR, MODEL_NAME);
    }

    public void train(Instances dataset) throws IOException {
        this.modelsFile.delete();
        BootstrapModelWriter writer = 
                new BootstrapModelWriter(this.modelsFile);
        for (int i = 0; i < ROUNDS; i++) {
            Instances sample = DatasetPreprocessor.bootstrapResample(dataset);
            try {
                System.err.println(
                        String.format("Training: Round (%d/%d)", i+1, ROUNDS)
                );
                MainPredictor predictor = new MainPredictor();
                predictor.train(sample);
                writer.write(predictor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        writer.flush();
        writer.close();
    }

    public double[] predict(Instances dataset) throws IOException, Exception {
        
        double[] result = new double[dataset.numInstances()];
        this.scores = new double[result.length];
        BootstrapModelReader reader = new BootstrapModelReader(this.modelsFile);
        int N = 0;
        for (int i = 0; i < ROUNDS; i++) {
            System.err.println(
                    String.format("Predicting: Round (%d/%d)", i+1, ROUNDS));
            try {
                MainPredictor predictor = reader.read();
                addTo(result, predictor.predict(dataset));
                N++;
            } catch (EOFException e) {
            }
        }
        
        reader.close();
        
        for (int i = 0; i < result.length; i++) {
            double classLabel = result[i] <= N / 2.0 ? 0 : 1;
            scores[i] = (classLabel == 0) ? 1.0 - result[i] / N : result[i] / N;
            if (N > 0) {
                result[i] = result[i] < N / 2.0 ? 0 : 1;
            } else {
                result[i] = 0;
            }
        }
        
        return result;
    }
    
    
    public Attribute getResultAttribute() {
        if (RESULT_ATTR == null) {
            RESULT_ATTR = new Attribute(
                    RESULT_ATTR_NAME,
                    DatasetGenerator.getClassLabels());
        }
        
        return RESULT_ATTR;
    }

    private void addTo(double[] result, final double[] newResult) {
        for (int i = 0; i < result.length; i++) {
            result[i] += newResult[i];
        }
    }

    public double[] getPredictionScores() {
        if (this.scores == null) {
            throw new IllegalStateException("Prediction not performed yet!");
        }
        return scores;
    }
    
}
