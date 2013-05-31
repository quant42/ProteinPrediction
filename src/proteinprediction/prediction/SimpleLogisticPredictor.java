/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.prediction;

import weka.classifiers.functions.SimpleLogistic;

/**
 *
 * @author wei
 */
public class SimpleLogisticPredictor extends WekaPredictor {
    public static final String OUTPUT_FILE = "SimpleLogistic.model";
    public static final String RESULT_ATTR = "SimpleLogistic";
    
    public SimpleLogisticPredictor() {
        super();
        this.classifier =  new SimpleLogistic();
        this.trainOptions = new String[]{
            "-I", "0",
            "-M", "500",
            "-H", "50",
            "-W", "0.0"
        };
        this.outputFileName = OUTPUT_FILE;
        this.resultAttrName = RESULT_ATTR;
    }
}
