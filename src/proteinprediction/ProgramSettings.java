package proteinprediction;

import java.io.File;

/**
 *
 * @author Yann
 */
public class ProgramSettings {
    
    public static final String DATA_FOLDER = "ppData";
    
    /**
     * IO: main directory for results, models
     */
    public static final File MAIN_DIR = new File("data");
    
    /**
     * IO: directory for storing trained models
     */
    public static final File MODEL_DIR = new File(MAIN_DIR, "models");
    
    /**
     * IO: directory for storing predicted results
     */
    public static final File RESULT_DIR = new File(MAIN_DIR, "results");
    
    /**
     * IO: directory for storing different data sets
     */
    public static final File DATASET_DIR = new File(MAIN_DIR, "datasets");
    
    /**
     * Feature selection: number of attributes to be selected
     */
    public static final int NUM_ATTRS = 200;
    
    /**
     * initialize program: make required directories
     */
    public static void initialize() {
        MAIN_DIR.mkdir();
        MODEL_DIR.mkdir();
        RESULT_DIR.mkdir();
        DATASET_DIR.mkdir();
    }
    
    // <editor-fold defaultstate="collapsed" desc="error quitting codes">
    public static final int PROGRAM_EXIT_NORMAL = 0;
    public static final int PROGRAM_EXIT_ERROR = 1;
    public static final int PROGRAM_EXIT_UNEXPECTED_ERROR = 2;
    public static final int PROGRAM_EXIT_MALFORMED_ARGS = 3;
    public static final int PROGRAM_EXIT_IOERROR = 4;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="predictor settings">
    public static final String PREDICTION_ATTRIBUTE_NAME = "prediction";
    public static final String PREDICTION_ATTRIBUTE_ACCURACY09 = "predictionAcc09";
    public static final String PREDICTION_ATTRIBUTE_ACCURACYAZ = "predictionAccAZ";
    public static final String PREDICTION_ATTRIBUTE_ACCURACYaZ = "predictionAccaZ";
    public static final String PREDICTION_CLASS_NO_TML_OR_TMH = "N";
    public static final String PREDICTION_CLASS_TML = "L";
    public static final String PREDICTION_CLASS_TMH = "H";
    // </editor-fold>
    
}
