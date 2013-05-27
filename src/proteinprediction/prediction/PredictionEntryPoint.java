package proteinprediction.prediction;

<<<<<<< HEAD
import proteinprediction.ProgramEntryPoint;

/**
 *
 * @author quant
 */
public class PredictionEntryPoint implements ProgramEntryPoint {

    @Override
    public int run(String[] args) {
        return 0;
    }

    @Override
    public String getUsageAndHelp() {
        return "predict <toPredict.arff> <output.fasta>";
    }

    @Override
    public String getShortDescription() {
        return "predict the HML/TML structure of roteins";
    }

=======
import proteinprediction.*;
import java.io.*;
import java.lang.Thread;

/**
 *
 * @author Yann
 */
public class PredictionEntryPoint implements ProgramEntryPoint {

    /**
     * register your predicter here
     */
    Predictor[] predictors = new Predictor[] {
        
    };
    
    /**
     * runs the predictor of every prodicting method
     */
    @Override
    public int run(String[] args) {
        try {
            File input = new File(args[0]);
            File output = new File(args[1]);
            // handle PredictorThread in Array
            PredictorThread[] pThreads = new PredictorThread[predictors.length];
            // start each Predictor
            for(int i = 0; i < predictors.length; i++) {
                pThreads[i] = new PredictorThread(predictors[i], input);
                pThreads[i].start();
            }
            // wait for each preditor to finish
            for(int i = 0; i < pThreads.length; i++) {
                pThreads[i].join();
                if(pThreads[i].output == null) {
                    System.err.println("Prediction method " + i + " throws returned a nullobject!");
                    throw new IOException();
                }
            }
            // merge output files
        } catch(IOException e) {
            return ProgramSettings.PROGRAM_EXIT_IOERROR;
        } catch (Exception e) {
            return ProgramSettings.PROGRAM_EXIT_MALFORMED_ARGS;
        }
        return ProgramSettings.PROGRAM_EXIT_NORMAL;
    }

    /**
     * returns the predicting Usage
     */
    @Override
    public String getUsageAndHelp() {
        return "predict <toPredict.arff> <output.arff>";
    }

    /**
     * a short discription of this program entry point 
     */
    @Override
    public String getShortDescription() {
        return "predict TML/TMH state for each aminoacid of a protein";
    }

    /**
     * the command line argument for this prediction
     */
>>>>>>> ca1716e4a66d5e64743009e3e194181988f56d94
    @Override
    public String getCommandLineArgumentName() {
        return "predict";
    }
    
<<<<<<< HEAD
=======
    /**
     * a Thread runnint a predictor
     */
    public class PredictorThread extends Thread {
        
        public Predictor predictor;
        public File arffInputFile;
        public File output;
        
        public PredictorThread(Predictor predictMethod, File arffFile) {
            predictor = predictMethod;
            arffInputFile = arffFile;
        }
        
        public void run() {
            try {
                output = predictor.predict(arffInputFile);
            } catch(Exception e) {
                output = null;
            }
        }
        
    }
    
>>>>>>> ca1716e4a66d5e64743009e3e194181988f56d94
}
