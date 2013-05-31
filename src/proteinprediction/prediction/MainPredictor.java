/*
 * Combines different predictors with neural network predictor
 * License: GPL
 */
package proteinprediction.prediction;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import proteinprediction.ProgramSettings;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * Combines different predictors with neural network predictor
 * @author Shen Wei
 */
public class MainPredictor {    
    /**
     * neural network for combining results of other predictors
     */
    private WekaPredictor neuralNetwork;
    
    /**
     * set of low-level predictors
     */
    private WekaPredictor[] predictors;
    
    public MainPredictor() {
        this.neuralNetwork = new MultilayerPerceptronPredictor();
        this.predictors = new WekaPredictor[] {
            //new J48Predictor(),
            new NaiveBayesPredictor(),
            new SVMPredictor(),
            new RBFNetworkPredictor(),
            new VotedPerceptronPredictor(),
            new SimpleLogisticPredictor()
        };
    }
    
    /**
     * load trained predictor model
     * @param model input file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void loadModel() 
            throws IOException, ClassNotFoundException 
    {
        this.neuralNetwork.loadModel();
        
        //load low-level predictors
        for (WekaPredictor predictor : predictors) {
            predictor.loadModel();
        }
    }
    
    /**
     * save this trained predictor to a model file
     * @param model
     * @throws IOException 
     */
    public void saveModel() 
            throws IOException 
    {
        this.neuralNetwork.saveModel();
    }
    
    /**
     * train neural network with results of other predictors
     * @param dataset
     * @throws IOException
     * @throws Exception 
     */
    public void train(Instances dataset) 
            throws IOException, Exception 
    {        
        dataset.setClassIndex(dataset.numAttributes() - 1);
        
        //train low-level predictors at first
        for (WekaPredictor predictor : predictors) {
            //save model
            System.err.println("Training " 
                    + predictor.classifier.getClass().getSimpleName() + " ...");
            predictor.train(dataset, null);
        }
        
        //generate training set for neural network
        System.err.println("Generating new training set for neural network ...");
        Instances trainSet = generateHighLevelSet(dataset, true);
        
        //train neural network
        System.err.println("Training neural network ...");
        neuralNetwork.train(trainSet, null);
    }
    
    /**
     * perform prediction over all instances
     * @param dataset
     * @return
     * @throws Exception 
     */
    public double[] predict(Instances dataset) 
            throws Exception {
        Instances testSet = generateHighLevelSet(dataset, false);
        
        ArffSaver saver = new ArffSaver();
        saver.setFile(new File(ProgramSettings.RESULT_DIR, "intermediate_result.arff"));
        saver.setInstances(testSet);
        saver.writeBatch();
        
        double[] values = new double[testSet.numInstances()];
        for (int i = 0; i < values.length; i++) {
            values[i] = this.neuralNetwork.predictInstance(testSet.instance(i));
        }
        return values;
    }
    
    /**
     * generate high level training/test set for neural network
     * @param dataset
     * @param hasClass whether the new set contains class attribute
     * @return
     * @throws Exception 
     */
    private Instances generateHighLevelSet(Instances dataset, boolean hasClass) 
            throws Exception 
    {
        FastVector attrInfo = new FastVector();
        
        //add result attributes
        for (int i = 0; i < predictors.length; i++) {
            WekaPredictor predictor = predictors[i];
            attrInfo.addElement(predictor.getResultAttribute());
        }
        
        //add class attribute
        attrInfo.addElement(
                dataset.attribute(dataset.numAttributes() - 1));
        
        Instances trainSet = new Instances(
                "NeuralNetwork_dataset", 
                attrInfo, 
                0);
        trainSet.setClassIndex(trainSet.numAttributes() - 1);
        Enumeration enm = dataset.enumerateInstances();
        while (enm.hasMoreElements()) {
            Instance inst = (Instance) enm.nextElement();
            Instance newInst = new Instance(trainSet.numAttributes());
            newInst.setDataset(trainSet);
            
            if (hasClass) {
                //set class label
                newInst.setClassValue(inst.classValue());
            } else {
                newInst.setClassMissing();
            }

            //set prediction result for each predictors
            for (int i = 0; i < predictors.length; i++) {
                double value = predictors[i].predictInstance(inst);
                newInst.setValue(i, value);
            }
            trainSet.add(newInst);
        }
        return trainSet;
    }
    
}
