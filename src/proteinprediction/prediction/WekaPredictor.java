/*
 * Wrapper for all weka classifiers
 * License: same as license of weka
 */
package proteinprediction.prediction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import proteinprediction.ProgramSettings;
import proteinprediction.io.ObjectIO;
import proteinprediction.utils.DatasetGenerator;
import proteinprediction.utils.DatasetPreprocessor;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * Wrapper for all weka classifiers
 * @author Shen Wei
 */
public abstract class WekaPredictor extends Predictor {
    protected Classifier classifier;
    protected boolean trained;
    protected String[] trainOptions;
    protected String outputFileName;
    
    public WekaPredictor() {
        this.trained = false;
        this.trainOptions = null;
        this.outputFileName  = null;
    }
    
    @Override
    public void loadModel(File f) {
        try {
            this.classifier = (Classifier) ObjectIO.deserializeObject(f);
            this.trained = true;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void saveModel(File f) {
        try {
            ObjectIO.serializeObject(this.classifier, f);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    
    @Override
    /**
     * @throws IllegalStateException if options are not set to classifier
     */
    public void train(File arffFile, File whereToSave) 
    throws IOException, IllegalStateException{
       if (this.trainOptions == null) {
           throw new IllegalStateException("Options are not given to classifier!");
       }
       Instances dataset = new Instances(new FileReader(arffFile));
        try {
            //training
            this.classifier.setOptions(this.trainOptions);
            this.classifier.buildClassifier(dataset);
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
       this.saveModel(whereToSave);
       this.trained = true;
    }
    
    @Override
    public File predict(File arffFile) throws IOException {
        Instances dataset = new Instances(new FileReader(arffFile));
        dataset = this.predictInstances(dataset);
        File output = new File(ProgramSettings.DATA_FOLDER, outputFileName);
        ArffSaver saver = new ArffSaver();
        saver.setCompressOutput(true);
        saver.setFile(output);
        saver.setInstances(dataset);
        saver.writeBatch();
        
        return output;
    }
    
    /**
     * predict
     * @param dataset
     * @return
     * @throws IllegalStateException 
     */
    public Instances predictInstances(Instances dataset) 
    throws IllegalStateException
    {
        if (!trained) {
            throw new IllegalStateException("Predictor is not trained!");
        }
        //remove string attributes (ID_POS)
        dataset.deleteStringAttributes();
        //TODO: use selected attributes
        DatasetPreprocessor.appendAttribute(
                    dataset,
                    DatasetGenerator.getClassAttribute());
        Enumeration enm = dataset.enumerateInstances();
        try {
            while (enm.hasMoreElements()) {
                Instance inst = (Instance)enm.nextElement();
                double classInternal = this.classifier.classifyInstance(
                        inst);
                inst.setValue(inst.numAttributes()-1, classInternal);
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return dataset;
    }  
}
