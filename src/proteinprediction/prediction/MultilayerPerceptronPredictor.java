/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.prediction;

import weka.classifiers.functions.MultilayerPerceptron;

/**
 *
 * @author wei
 */
public class MultilayerPerceptronPredictor extends WekaPredictor {
    public static final String OUTPUT_FILE = "MultilayerPerceptron.model";
    public static final String RESULT_ATTR = "MultilayerPerceptron";
    
    public MultilayerPerceptronPredictor() {
        super();
        this.classifier = new MultilayerPerceptron();
        this.trainOptions = new String[]{
            "-L", "0.3",
            "-M", "0.2", 
            "-N", "500",
            "-V", "0",
            "-S", "0",
            "-E", "20",
            "-H", "a"
        };
        this.outputFileName = OUTPUT_FILE;
        this.resultAttrName = RESULT_ATTR;
    }
}
