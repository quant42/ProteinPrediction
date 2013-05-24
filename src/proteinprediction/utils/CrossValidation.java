/*
 * This class holds all data sets for training, testing and cross-validation
 * License: GPL
 */
package proteinprediction.utils;

import java.util.HashMap;
import java.util.Map;
import weka.core.Instances;

/**
 * This class holds all data sets for training, testing and cross-validation
 * @author Shen Wei
 */
public class CrossValidation {
    
    /**
     * training set
     */
    private Instances trainSet;
    
    /**
     * test set
     */
    private Instances testSet;
    
    /**
     * validation set
     */
    private Instances validationSet;
    
    /**
     * stores which data set does a instance belong to
     */
    private Map<String, Integer> members;
    
    public static final int MEMBER_OF_TRAIN_SET = 0;
    public static final int MEMBER_OF_TEST_SET = 1;
    public static final int MEMBER_OF_VALIDATION_SET = 2;
    
    /**
     * constructor
     * @param train
     * @param test
     * @param validate 
     */
    public CrossValidation(
            Instances train, 
            Instances test, 
            Instances validate) 
    {
        this.trainSet = train;
        this.testSet = test;
        this.validationSet = validate;
        this.members = new HashMap<String, Integer>();
    }
    
    
    public Instances getTrainingSet()
    {
        return this.trainSet;
    }
    
    public Instances getTestSet() {
        return this.testSet;
    }
    
    public Instances getValidationSet() {
        return this.validationSet;
    }
    
    public int getMembership(String id) {
        Integer membership = members.get(id);
        if (membership != null)
            return membership;
        return -1;
    }
    
    public void setMembership(String id, int membership)
    {
        this.members.put(id, membership);
    }
}
