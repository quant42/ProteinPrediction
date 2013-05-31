package proteinprediction.prediction;

import java.io.File;
import java.io.IOException;
import proteinprediction.ProgramEntryPoint;
import proteinprediction.ProgramSettings;

/**
 *
 * @author Yann
 */
public class TrainingEntryPoint implements ProgramEntryPoint {

    // for multithreading reasons only (Thread syncronizing object)
    private static Object sync = new Object();
    
    /**
     * you don't need to register your Predictors in this class, it will automatically
     * get the predictors from the PredictionEntryPoint class
     */
    
    /**
     * Starts the training of all prediction methods
     */
    @Override
    public int run(String[] args) {
        try {
            // parse args
            File input = new File(args[0]);
            // train methods
            for(int i = 0; i < PredictionEntryPoint.predictors.length; i++) {
                PredictionEntryPoint.predictors[i].train(input, new File(ProgramSettings.DATA_FOLDER + "/method" + i + ".dat"));
            }
            // train network
            
            
        } catch (IOException e) {
            return ProgramSettings.PROGRAM_EXIT_IOERROR;
        } catch (Exception e) {
            return ProgramSettings.PROGRAM_EXIT_MALFORMED_ARGS;
        }
        return ProgramSettings.PROGRAM_EXIT_NORMAL;
    }

    @Override
    public String getUsageAndHelp() {
        return "train <trainingset.arff>\n";
    }

    @Override
    public String getShortDescription() {
        return "given an dataset this will train the prediction methods";
    }

    @Override
    public String getCommandLineArgumentName() {
        return "train";
    }
    
}
