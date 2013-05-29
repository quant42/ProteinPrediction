package proteinprediction.prediction;

import proteinprediction.*;
import java.io.*;

/**
 *
 * @author Yann
 */
public class PredictionEntryPoint implements ProgramEntryPoint {

    // for multithreading reasons only (Thread syncronizing object)
    private static Object sync = new Object();
    
    /**
     * register your predicter here
     */
    public static Predictor[] predictors = new Predictor[] {
        new NeuronalNetworkPredictor(),
        new ADTreePredictor(),
        new NaiveBayesPredictor(),
        new SVMPredictor()
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
                pThreads[i] = new PredictorThread(predictors[i], new File(ProgramSettings.DATA_FOLDER + "/method" + i + ".dat"), input);
                pThreads[i].start();
            }
            // wait for each preditor to finish and save output files
            File[] files = new File[predictors.length];
            for(int i = 0; i < pThreads.length; i++) {
                pThreads[i].join();
                if(pThreads[i].output == null) {
                    System.err.println("Prediction method " + i + " throws returned a nullobject!");
                    throw new IOException();
                }
                files[i] = pThreads[i].output;
            }
            // merge output files
            NeuronalNetworkPredictionsMixer n = new NeuronalNetworkPredictionsMixer();
            n.loadNeuronalNetwork(new File(ProgramSettings.DATA_FOLDER + "/mixMethodNet.dat"));
            // save
            n.mergePredictionData(files, output);
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
    @Override
    public String getCommandLineArgumentName() {
        return "predict";
    }
    
    /**
     * a Thread running a predictor
     */
    public class PredictorThread extends Thread {
        
        public Predictor predictor;
        public File arffInputFile;
        public File trainingFile;
        public File output;
        
        public PredictorThread(Predictor predictMethod, File trainingFile, File arffFile) {
            predictor = predictMethod;
            arffInputFile = arffFile;
        }
        
        @Override
        public void run() {
            try {
                predictor.loadModel(trainingFile);
                output = predictor.predict(arffInputFile);
            } catch(Exception e) {
                synchronized(sync) {
                    System.err.println("A prediction method has thrown an uncaught error:");
                    System.err.println("A team of highly trained bioinformaticans should resolve this error. However if you see them, show them these informations:");
                    System.err.println(e.toString());
                    System.err.println(e.getStackTrace());
                }
                output = null;
            }
        }
        
    }
    
}
