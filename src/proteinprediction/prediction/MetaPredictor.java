/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.prediction;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import proteinprediction.ProgramSettings;
import proteinprediction.io.ObjectIO;
import proteinprediction.utils.DatasetGenerator;
import weka.core.Attribute;
import weka.core.Instances;

/**
 *
 * @author shen wei
 */
public class MetaPredictor implements Serializable {
    
    private static final long serialVersionUID = 12532246L;
    
    public static final int ROUNDS = 1000;
    public MainPredictor[] bags = null;
    
    private static Attribute RESULT_ATTR = null;
    public static final String RESULT_ATTR_NAME = "MetaPredictor_result";
    public static final String MODEL_NAME = "MetaPredictor.model";
    
    private boolean trained;
    
    private double[] scores = null;
    
    public MetaPredictor() {
        this.bags = new MainPredictor[ROUNDS];
        for (int i = 0; i < ROUNDS; i++) {
            bags[i] = new MainPredictor();
        }
        this.trained = false;
    }

    public void loadModel(File f) {
        try {
            MetaPredictor meta = (MetaPredictor) ObjectIO.deserializeObject(f);
            this.bags = meta.bags;
            this.trained = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void loadModel() {
        File f = new File(ProgramSettings.MODEL_DIR, MODEL_NAME);
        this.loadModel(f);
    }

    public void saveModel(File f) {
        try {
            ObjectIO.serializeObject(this, f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void saveModel() {
        File f = new File(ProgramSettings.MODEL_DIR, MODEL_NAME);
        this.saveModel(f);
    }

    public void train(Instances dataset) {
        for (int i = 0; i < ROUNDS; i++) {
            Random rand = new Random();
            Instances sample = dataset.resample(rand);
            try {
                System.err.println(
                        String.format("Training: Round (%d/%d)", i+1, ROUNDS)
                );
                bags[i].train(sample);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.trained = true;
    }

    public double[] predict(Instances dataset) throws IOException, Exception {
        if (!this.trained) {
            throw new IllegalStateException("Predictor not trained yet!");
        }
        
        double[] result = new double[dataset.numInstances()];
        this.scores = new double[result.length];
        for (int i = 0; i < ROUNDS; i++) {
            System.err.println(
                    String.format("Predicting: Round (%d/%d)", i+1, ROUNDS));
            addTo(result, bags[i].predict(dataset));
        }
        
        for (int i = 0; i < result.length; i++) {
            scores[i] = 1.0 - result[i] / result.length;
            result[i] = result[i] < ROUNDS / 2.0 ? 0 : 1;
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
