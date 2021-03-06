/*
 * Wrapper function for weka LibSVM
 * License: same license like Weka
 */
package proteinprediction.prediction;

import weka.classifiers.functions.LibSVM;

/**
 * Wrapper function for weka LibSVM
 * @author Shen Wei
 */
public class SVMPredictor  extends WekaPredictor{
    
    private static final long serialVersionUID = 66342117L;
    
    public static final String OUTPUT_FILE = "LibSVM.model";
    
    public static final String RESULT_ATTR = "LibSVM";
    
    public SVMPredictor() {
        super();
        this.classifier = new LibSVM();
        this.trainOptions = new String[] {
            "-S", "0",
            "-K", "2",
            "-D", "3",
            "-G", "0.0",
            "-R", "0.0",
            "-N", "0.5",
            "-M", "40.0",
            "-C", "1.0",
            "-E", "0.001",
            "-P", "0.1",
            "-seed", "1"
        };
        this.outputFileName = OUTPUT_FILE;
        this.resultAttrName = RESULT_ATTR;
        this.resultNumericAttrName = "LibSVM_raw";
    }
}
