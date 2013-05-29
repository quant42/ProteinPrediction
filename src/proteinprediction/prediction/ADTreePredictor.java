/*
 * Wrapper class of the weka classifier weka.classifiers.trees.ADTree
 * License: same license like Weka
 */
package proteinprediction.prediction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import proteinprediction.io.ObjectIO;
import weka.classifiers.Classifier;
import weka.classifiers.trees.ADTree;
import weka.core.Instances;

/**
 * Wrapper class of the weka classifier weka.classifiers.trees.ADTree
 * @author Shen Wei
 */
public class ADTreePredictor extends Predictor {
    
    private Classifier classifier;
    
    public ADTreePredictor() {
        this.classifier = new ADTree();
    }

    @Override
    public void loadTrainingFile(File f) {
        try {
            this.classifier = (Classifier) ObjectIO.deserializeObject(f);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void saveTrainingFile(File f) {
        try {
            ObjectIO.serializeObject(this.classifier, f);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void train(File arffFile, File whereToSave) throws IOException {
       String[] options = new String[] {};
       Instances dataset = new Instances(new FileReader(arffFile));
        try {
            //training
            this.classifier.setOptions(options);
            this.classifier.buildClassifier(dataset);
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
       this.saveTrainingFile(whereToSave);
    }

    @Override
    public File predict(File arffFile) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
