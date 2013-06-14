/*
 * Wrapper class for weka classifier SimpleLogisitic
 * License: same as license of weka
 */
package proteinprediction.prediction;

import weka.classifiers.functions.SimpleLogistic;

/**
 * Wrapper class for weka classifier SimpleLogisitic
 * @author Shen Wei
 */
public class SimpleLogisticPredictor extends WekaPredictor {
    
    private static final long serialVersionUID = 59724141L;
    
    public static final String OUTPUT_FILE = "SimpleLogistic.model";
    public static final String RESULT_ATTR = "SimpleLogistic";
    
    public SimpleLogisticPredictor() {
        super();
        this.classifier =  new SimpleLogistic();
        this.trainOptions = new String[]{
            "-I", "0",
            "-M", "100",
            "-H", "10",
            "-W", "0.0"
        };
        this.outputFileName = OUTPUT_FILE;
        this.resultAttrName = RESULT_ATTR;
        this.resultNumericAttrName = "SimpleLogistic_raw";
    }
}
