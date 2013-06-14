/*
 * Wrapper class for weka classifier RBFNetwork
 * License: same as license of weka
 */
package proteinprediction.prediction;

import weka.classifiers.functions.RBFNetwork;

/**
 * Wrapper class for weka classifier RBFNetwork
 * @author Shen Wei
 */
public class RBFNetworkPredictor extends WekaPredictor {
    
    private static final long serialVersionUID = 98064175L;
    
    public static final String OUTPUT_FILE = "RBFNetwork.model";
    
    public static final String RESULT_ATTR = "RBFNetwork";
    
    public RBFNetworkPredictor() {
        super();
        this.classifier = new RBFNetwork();
        this.trainOptions = new String[]{
            "-S", "1",
            "-B", "2",
            "-R", "1.0E-8",
            "-M", "-1",
            "-W", "0.1"
        };
        this.outputFileName = OUTPUT_FILE;
        this.resultAttrName = RESULT_ATTR;
        this.resultNumericAttrName = "RBFNetwork_raw";
    }
}
