package proteinprediction;

/**
 *
 * @author Yann
 */
public class ProgramSettings {

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
    
    public static final String DATA_FOLDER = "ppData";
    
}
