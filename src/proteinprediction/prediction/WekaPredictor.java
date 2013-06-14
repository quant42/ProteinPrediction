/*
 * Wrapper for all weka classifiers
 * License: same as license of weka
 */
package proteinprediction.prediction;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import proteinprediction.ProgramSettings;
import proteinprediction.io.ObjectIO;
import proteinprediction.utils.DatasetGenerator;
import proteinprediction.utils.DatasetPreprocessor;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Wrapper for all weka classifiers
 * @author Shen Wei
 */
public  class WekaPredictor extends PredictorNew{
    
    private static final long serialVersionUID = 17590219L;
    /**
     * weka classifier
     */
    protected Classifier classifier;
    
    /**
     * whether the classifier is already trained
     */
    protected boolean trained;
    
    /**
     * options for weka classifier
     */
    protected String[] trainOptions;
    
    /**
     * output file for storing prediction results
     */
    protected String outputFileName;
    
    /**
     * name of the attribute where the prediction results should be stored
     */
    protected String resultAttrName;
    
    /**
     * attribute where the prediction results should be stored
     */
    private Attribute resultAttribute;
    
    protected String resultNumericAttrName;
    protected Attribute resultNumericAttribute;
    
    public WekaPredictor() {
        this.trained = false;
        this.trainOptions = null;
        this.outputFileName  = null;
        this.resultAttrName = null;
        this.resultAttribute = null;
        this.resultNumericAttribute = null;
        this.resultNumericAttrName = null;
    }
    
    @Override
    public void loadModel(File f) {
        try {
            this.classifier = (Classifier) ObjectIO.deserializeObject(f);
            this.trained = true;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    
    /**
     * load model from default path
     */
    public void loadModel() {
        try {
            this.classifier = (Classifier) ObjectIO.deserializeObject(
                    new File(ProgramSettings.MODEL_DIR, outputFileName)
                    );
            this.trained = true;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void saveModel(File f) {
        try {
            ObjectIO.serializeObject(this.classifier, f);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    
    /**
     * save model to default path
     */
    public void saveModel() {
        try {
            ObjectIO.serializeObject(
                    this.classifier, 
                    new File(ProgramSettings.MODEL_DIR, outputFileName));
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    
    @Override
    /**
     * @throws IllegalStateException if options are not set to classifier
     */
    public void train(Instances dataset, File outputModel) 
    throws IOException, IllegalStateException{
       if (this.trainOptions == null) {
           throw new IllegalStateException("Options are not given to classifier!");
       }
        try {
            //training
            this.classifier.setOptions(this.trainOptions);
            this.classifier.buildClassifier(dataset);
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        
        if (outputModel != null) {
            this.saveModel(outputModel);
        } else {
            //this.saveModel();
        }
       this.trained = true;
    }
    
    @Override
    public Instances predict(Instances dataset) throws IOException {
        dataset = this.predictInstances(dataset);        
        return dataset;
    }
    
    /**
     * predict
     * @param dataset
     * @return
     * @throws IllegalStateException 
     */
    public Instances predictInstances(Instances dataset) 
    throws IllegalStateException
    {
        if (!trained) {
            throw new IllegalStateException("Predictor is not trained!");
        }
        //remove string attributes (ID_POS)
        dataset.deleteStringAttributes();
        //TODO: use selected attributes
        DatasetPreprocessor.appendAttribute(
                    dataset,
                    this.getResultAttribute());
        Enumeration enm = dataset.enumerateInstances();
        try {
            while (enm.hasMoreElements()) {
                Instance inst = (Instance)enm.nextElement();
                double classInternal = this.classifier.classifyInstance(
                        inst);
                inst.setValue(inst.numAttributes()-1, classInternal);
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        return dataset;
    }
    
    /**
     * get name of attribute where prediction results are stored in
     * @return 
     */
    public String getResultAttributeName() {
        return this.resultAttrName;
    }
    
    /**
     * get attribute in which results are stored
     * @return 
     */
    public Attribute getResultAttribute() {
        if (this.resultAttribute == null) {
            this.resultAttribute = new Attribute(
                    this.resultAttrName, 
                    DatasetGenerator.getClassLabels());
        }
        return this.resultAttribute;
    }
    
    public Attribute getResultNumericAttribute() {
        if (this.resultNumericAttribute == null) {
            this.resultNumericAttribute = new Attribute(
                    this.resultNumericAttrName);
        }
        return this.resultNumericAttribute;
    }

    /**
     * predict class label of one instance
     * @param inst
     * @return
     * @throws Exception 
     */
    public double predictInstance(Instance inst) throws Exception {
        if (!this.trained) {
            throw new IllegalStateException("Classifier is not trained yet!");
        }
        return this.classifier.classifyInstance(inst);
    }
    
    /**
     * get prediction score of the instance
     * @param inst
     * @return
     * @throws Exception 
     */
    public double[] predictionScore(Instance inst) throws Exception{
        if (!this.trained) {
            throw new IllegalStateException("Classifier is not trained yet!");
        }
        return this.classifier.distributionForInstance(inst);
    }
}
