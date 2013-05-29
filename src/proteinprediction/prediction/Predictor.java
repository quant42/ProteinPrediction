package proteinprediction.prediction;

import java.io.*;

/**
 * This is an abstract class defining an predictor for the TML/TMH problem.
 * NOTE: All Predictors have to be implemented !!!!THREADSAFE!!!!
 * 
 * @author Yann
 */
public abstract class Predictor {
    
    /**
     * Let the predictor load its data out of an predictordataFile!
     * See also saveTrainingFile
     */
    public abstract void loadModel(File f);
    
    /**
     * save the data the predicter needs to a file; Note, an predictor that loads
     * it's data from a file should predict he same way/with the same accuracy
     * as the predictor that safes this data.
     */
    public abstract void saveModel(File f);
    
    /**
     * Train your prediction method according to that arffFile (the weka's classes
     * could help opening this file)
     */
    public abstract void train(File arffFile, File whereToSafe) throws IOException;
    
    /**
     * The predictor should return a File, with its predicted values for the
     * TML/TMH problem. The new attribute, that should be added to the arffFile
     * should have the name saved in ProgramSettings.PREDICTION_ATTRIBUTE_NAME.
     * (also Note: ProgramSettings.PREDICTION_CLASS_NO_TML_OR_TMH, ProgramSettings.PREDICTION_CLASS_TML,
     * ProgramSettings.PREDICTION_CLASS_NO_TMH)
     * The Predictor might also safe an attribute (ProgramSettings.PREDICTION_ATTRIBUTE_ACCURACY[XX])
     * To save the accuracy of it's prediction!
     * XX:
     * 09: The prediction accuracy is given in a range from 0 to 9 (0 = lowest, 1, 2, 3, 4, 5, 6, 7, 8, 9 = highest)
     * AZ: The prediction accuracy is given in a range from A to Z (A = lowest, B, ..., Z = highest)
     * aZ: The prediction accuracy is given in a range from a to Z (a = lowest, b, ..., z, A, B, C, ..., Z=highest)
     */
    public abstract File predict(File arffFile) throws IOException;
    
}
