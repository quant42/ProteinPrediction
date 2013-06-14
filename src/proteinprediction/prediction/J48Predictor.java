/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.prediction;

import weka.classifiers.trees.J48;

/**
 *
 * @author wei
 */
public class J48Predictor extends WekaPredictor {
    
    private static final long serialVersionUID = 44179305L;
    
    public static final String OUTPUT_FILE = "J48.model";
    public static final String RESULT_ATTR = "J48";
    
    public J48Predictor() {
        super();
        this.classifier =  new J48();
        this.trainOptions = new String[]{
            "-C", "0.25",
            "-M", "2"
        };
        this.outputFileName = OUTPUT_FILE;
        this.resultAttrName = RESULT_ATTR;
        this.resultNumericAttrName = "J48_raw";
    }
}
