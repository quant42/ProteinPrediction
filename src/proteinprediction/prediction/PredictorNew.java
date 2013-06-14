package proteinprediction.prediction;

import java.io.*;
import weka.core.Instances;

/**
 * This is an abstract class defining an predictor for the TML/TMH problem.
 * NOTE: All Predictors have to be implemented !!!!THREADSAFE!!!!
 * 
 * @author Yann, Shen Wei
 */
public abstract class PredictorNew  implements Serializable {
    
    /**
     * load trained model
     * @param f read model from this file
     */
    public abstract void loadModel(File f);
    
    /**
     * save trained model
     * @param f output file
     */
    public abstract void saveModel(File f);
    
    /**
     * Train model with a training set and save trained model to a file
     * @param dataset training set
     * @param outputModel file in which to save the model
     */
    public abstract void train(Instances dataset, File outputModel) throws IOException;
    
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
    public abstract Instances predict(Instances dataset) throws IOException;
    
}
