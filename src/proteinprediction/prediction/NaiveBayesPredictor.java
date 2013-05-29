/*
 * Wrapper class for weka.classifiers.bayes.NaiveBayes
 * License: same as license of Weka
 */
package proteinprediction.prediction;

import weka.classifiers.bayes.NaiveBayes;

/**
 * Wrapper class for weka.classifiers.bayes.NaiveBayes
 * @author Shen Wei
 */
public class NaiveBayesPredictor extends WekaPredictor {
    
    public static final String OUTPUT_FILE = "NaiveBayes_result.arff.gz";
    
    public NaiveBayesPredictor() {
        super();
        this.classifier =  new NaiveBayes();
        this.trainOptions = new String[]{};
        this.outputFileName = OUTPUT_FILE;
    }
}
