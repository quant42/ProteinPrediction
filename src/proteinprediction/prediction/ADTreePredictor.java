/*
 * Wrapper class of the weka classifier weka.classifiers.trees.ADTree
 * License: same license like Weka
 */
package proteinprediction.prediction;

import weka.classifiers.trees.ADTree;

/**
 * Wrapper class of the weka classifier weka.classifiers.trees.ADTree
 * @author Shen Wei
 */
public class ADTreePredictor extends WekaPredictor {
    
    private static final long serialVersionUID = 35839782L;
    
    public static final String OUTPUT_FILE = "ADTree.model";
    
    public static final String RESULT_ATTR = "ADTree";
    
    public ADTreePredictor() {
        super();
        this.classifier = new ADTree();
        this.trainOptions = new String[] {
            "-B",
            "10",
            "-E",
            "-3"
        };
        this.outputFileName = OUTPUT_FILE;
        this.resultAttrName = RESULT_ATTR;
        this.resultNumericAttrName = "ADTree_raw";
    }
}
