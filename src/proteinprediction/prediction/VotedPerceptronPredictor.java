/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.prediction;

import weka.classifiers.functions.VotedPerceptron;

/**
 *
 * @author wei
 */
public class VotedPerceptronPredictor extends WekaPredictor {
    private static final long serialVersionUID = 12532246L;
    
    public static final String OUTPUT_FILE = "VotedPerceptron.model";
    public static final String RESULT_ATTR = "VotedPerceptron";
    
    public VotedPerceptronPredictor() {
        super();
        this.classifier =  new VotedPerceptron();
        this.trainOptions = new String[]{
            "-I", "1",
            "-E", "1.0",
            "-S", "1",
            "-M", "10000"
        };
        this.outputFileName = OUTPUT_FILE;
        this.resultAttrName = RESULT_ATTR;
        this.resultNumericAttrName = "VotedPerceptron_raw";
    }
}
