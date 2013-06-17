/*
 * Utility functions for preprocessing data set in different ways
 * License: GPL
 */
package proteinprediction.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;

/**
 * This class provides utility functions for preprocessing data set in different 
 * ways
 * @author Shen Wei
 */
public class DatasetPreprocessor {
    
    public static final Random randomSeed = new Random();
    
    public static Instances getBalancedDataset(
            Instances dataset, 
            boolean oversampling,
            int classIndex)
    {
        //count number of instances with specific class label
        AttributeStats stats = dataset.attributeStats(classIndex);
        final int[] classCounts = stats.nominalCounts;
        final int classes = classCounts.length;
        
        //find out the class which have most / least instances
        int minClass = 0, maxClass = 0, 
            minCount = Integer.MAX_VALUE, maxCount = -1;
        for (int i = 0; i < classes; i++)
        {
            int count = classCounts[i];
            if (minCount > count) {
                minCount = count;
                minClass = i;
            }
            
            if (maxCount < count) {
                maxCount = count;
                maxClass = i;
            }
        }
        
        //split instances by class label
        Instances[] isoSets = getOneClassedDatasets(
                dataset, 
                classIndex);
        
        //generate new data set with same structure but empty size
        Instances result = new Instances(dataset, -1);
        
        //perform sampling
        for (int klass = 0; klass < classes; klass++)
        {
            Instances isoSet = isoSets[klass];
            Enumeration enm = isoSet.enumerateInstances();
            
            //randomize data set
            isoSet.randomize(randomSeed);
            
            //over sampling
            if (oversampling) {
                
                //class label with maximum instance number
                if (klass == maxClass) {
                    //no sampling, just use them all
                    while (enm.hasMoreElements()) {
                        result.add((Instance) enm.nextElement());
                    }
                    continue;
                }
                
                //classes other than the largest one
                int oversamplingRate = (int) Math.ceil(
                        (double) maxCount / isoSet.numInstances());
                int remain = maxCount;
                
                //insert instance several times to new data set
                while (remain > 0 && enm.hasMoreElements()) {
                    Instance inst = (Instance) enm.nextElement();
                    int times = oversamplingRate + 1;
                    while (times > 0 && remain > 0)
                    {
                        result.add(inst);
                        times--;
                        remain--;
                    }
                }
            } else { //down sampling
                
                //label with minimum instance number
                if (klass == minClass) {
                    //no down sampling just use them all
                    while (enm.hasMoreElements()) {
                        result.add((Instance) enm.nextElement());
                    }
                    continue;
                }
                
                //classes other than the smallest one
                int items = 0;
                while (enm.hasMoreElements() && (items++) < minCount)
                {
                    result.add((Instance) enm.nextElement());
                }
            }
        }
        
        return result;
    }
    
    /**
     * group instances with same class label together
     * @param dataset original data set
     * @param classIndex index of class label (should be nominal)
     * @return array of data sets
     */
    public static Instances[] getOneClassedDatasets(
            Instances dataset, 
            int classIndex)
    {
        final int classes = dataset.attribute(classIndex).numValues();
        Instances[] isoSets = new Instances[classes];
        
        //new empty datasets with same structure
        for (int klass = 0; klass < classes; klass++)
        {
            isoSets[klass] = new Instances(dataset, -1);
        }
        
        //reassign instances to their groups
        Enumeration enm = dataset.enumerateInstances();
        while (enm.hasMoreElements()) {
            Instance inst = (Instance) enm.nextElement();
            int klass = (int) inst.value(classIndex);
            isoSets[klass].add(inst);
        }
        return isoSets;
    }
    
    /**
     * split data set into several subsets
     * @param dataset original dataset
     * @param weights weight for each subset
     * @return 
     */
    public static Instances[] splitDataset(
            final Instances dataset, double[] weights)
    {
        Instances[] sets = new Instances[weights.length];
        Instances[] isoSets = getOneClassedDatasets(
                dataset, 
                dataset.numAttributes() - 1);
        double totalWeight = 0.0;
        for (double w : weights) totalWeight += w;
        for (int klass = 0; klass < isoSets.length; klass++) {
            Instances isoSet = isoSets[klass];
            isoSet.randomize(
                    new Random(
                    java.util.Calendar.getInstance().getTimeInMillis()));
            final int numInst = isoSet.numInstances();
            Enumeration enm = isoSet.enumerateInstances();
            int remain = numInst;
            for (int fold = 0; fold < sets.length; fold++) {
                if (sets[fold] == null) {
                    sets[fold] = new Instances(dataset, -1);
                }
                int size = (int) (weights[fold] / totalWeight * numInst);
                
                for (int i = 0; i < size; i++) {
                    sets[fold].add((Instance) enm.nextElement());
                }
                remain -= size;
            }
            while(remain > 0) {
                sets[remain % sets.length].add((Instance) enm.nextElement());
                remain--;
            }
        }
        
        return sets;
    }
    
    /**
     * split dataset into equal-sized subsets
     * @param dataset
     * @param folds
     * @return 
     */
    public static Instances[] splitDataset(
            final Instances dataset, final int folds) {
        double[] weights = new double[folds];
        for (int i = 0; i < folds; i++) {
            weights[i] = 1.0;
        }
        return splitDataset(dataset, weights);
    }
    
    /**
     * select features from data set with given attribute indices
     * @param dataset
     * @param indices
     * @return 
     */
    public static Instances selectFeatures(Instances dataset, String indices) 
            throws Exception {
        Reorder reorder = new Reorder();
        String[] options = new String[]{
            "-R",
            indices
        };
        reorder.setOptions(options);
        reorder.setInputFormat(dataset);
        Instances result = Filter.useFilter(dataset, reorder);
        return result;
    }
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
       try {
           String cmd = args[0];
           if (!"|balance|reorder|cvset|folds|".contains("|" + cmd + "|")) {
               throw new IllegalArgumentException("Unknown command: " + cmd);
           }
           
           for (String arg : args) {
               System.out.println("Parameter: " + arg);
           }
           System.out.println("Parameters: " + args.length);
           
           String input = args[1];
           ArffSaver saver = new ArffSaver();
           
           if (cmd.equals("balance")) {
               String output = args[2];
               boolean balance = true;
               if (args.length > 3) {
                   balance = Boolean.parseBoolean(args[3]);
               }
               Instances dataset = new Instances(new FileReader(input));
               int classIndex = dataset.numAttributes() - 1;
               if (dataset.classIndex() > 0)
                   classIndex = dataset.classIndex();
               dataset = DatasetPreprocessor.getBalancedDataset(
                       dataset, balance, classIndex);
               saver.setFile(new File(output));
               saver.setInstances(dataset);
               saver.writeBatch();
           } else if (cmd.equals("cvset")) {
               
           } else if (cmd.equals("reorder")) {
               String output = args[2];
               String indices = args[3];
               Instances dataset = new Instances(new FileReader(input));
               dataset = selectFeatures(dataset, indices);
               
               saver.resetOptions();
               saver.setFile(new File(output));
               saver.setInstances(dataset);
               saver.writeBatch();
           } else if (cmd.equals("folds")) {
               File outputDir = new File(args[2]);
               int folds = Integer.parseInt(args[3]);
               if (!outputDir.exists()) {
                   outputDir.mkdirs();
               } else if (!outputDir.isDirectory()) {
                   throw new IllegalArgumentException(
                           "Not a directory! " + args[2]);
               }
               double[] w = null;
               if (args.length > 4) {
                   String[] weights = args[4].split(",");
                   w = new double[weights.length];
                   for (int i = 0; i < w.length; i++) {
                       w[i] = Double.parseDouble(weights[i]);
                   }
               }
               Instances dataset = new Instances(new FileReader(input));
               Instances[] subsets = 
                       w == null ? splitDataset(dataset, folds) :
                       splitDataset(dataset, w);
               for (int i = 0; i < folds; i++) {
                   File output = new File(outputDir, "fold_" + (i+1) + ".arff");
                   Instances subset = subsets[i];
                   saver.resetOptions();
                   saver.setFile(output);
                   saver.setInstances(subset);
                   saver.writeBatch();
               }
           }
           
       } catch (IOException e) {
           System.err.println(e);
       } catch (Exception e) {
           e.printStackTrace();
           System.err.println(e);
           System.err.println("Usage: DatasetPreprocessor balance <inputarff>"
                   + " <output.arff> [oversampling=true|false]\n"
                   + "       DatasetPreprocessor reorder <input.arff> "
                   + "<output.arff> <comma_seperated_indices>\n"
                   + "       DatasetPreprocessor folds <input.arff> "
                   + "<output_directory> <folds> [comma_separated_weights]");
       }
    }

    public static double[] getWeights(String weights) {
        String[] arr = weights.split(",");
        double[] ret = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ret[i] = Double.parseDouble(arr[i]);
        }
        return ret;
    }
    
    /**
     * filter a dataset using GainRatio feature selection
     * @param dataset
     * @param classIndex
     * @param n
     * @return 
     */
    public static Instances featureSelection(
            Instances dataset, int classIndex, int n) 
            throws Exception
    {
        
        String[] options = new String[] {
            "-c",
            String.format("%s", classIndex),
            "-T",
            "-1.7976931348623157E308",
            "-N",
            String.format("%d", n)
        };
        
        dataset.setClassIndex(classIndex);
        
        AttributeSelection filter = new AttributeSelection();
        
        GainRatioAttributeEval eval = new GainRatioAttributeEval();
        Ranker ranker = new Ranker();
        ranker.setOptions(options);
        
        filter.setEvaluator(eval);
        filter.setSearch(ranker);
        
        filter.SelectAttributes(dataset);
        return filter.reduceDimensionality(dataset);
    }
    
    
    /**
     * Use GainRatio feature selection method to select indices of good attributes
     * (Index of class attribute is not included)
     * @param dataset
     * @param classIndex
     * @param n
     * @return 
     */
    public static int[] featureIndicesSelection(
            Instances dataset, int classIndex, int n) 
            throws Exception
    {
        
        String[] options = new String[] {
            "-c",
            String.format("%s", classIndex),
            "-T",
            "-1.7976931348623157E308",
            "-N",
            String.format("%d", n)
        };
        
        dataset.setClassIndex(classIndex);
        
        AttributeSelection filter = new AttributeSelection();
        
        GainRatioAttributeEval eval = new GainRatioAttributeEval();
        Ranker ranker = new Ranker();
        ranker.setOptions(options);
        
        filter.setEvaluator(eval);
        filter.setSearch(ranker);
        
        filter.SelectAttributes(dataset);
        return filter.selectedAttributes();
    }

    /**
     * get comma separated selected indices
     * @param dataset
     * @param classIndex
     * @param n
     * @return
     * @throws Exception 
     */
    public static String featureIndicesStringSelection(
            Instances dataset, int classIndex, int n) 
            throws Exception 
    {
        int[] indices = featureIndicesSelection(dataset, classIndex, n);
        ArrayList<Integer> list = new ArrayList<Integer>(indices.length);
        for(int i : indices) list.add(i);
        return StringUtils.join(list, ',');
    }

    /**
     * append an attribute at end of attribute list
     * @param dataset
     * @param classAttribute
     */
    public static void appendAttribute(
            Instances dataset, Attribute classAttribute) {
        dataset.insertAttributeAt(classAttribute, dataset.numAttributes());
    }
    
    public static Instances bootstrapResample(Instances dataset) {
        Instances tmp = new Instances(dataset), newSet;
        tmp.randomize(randomSeed);
        int n = (int) Math.ceil(tmp.numInstances() * 0.632);
        final int N = dataset.numInstances();
        
        newSet = new Instances(tmp, 0, n);
        
        n = newSet.numInstances();
        while (newSet.numInstances() < N) {
            newSet.add(newSet.instance(randomSeed.nextInt(n)));
        }
        
        return newSet;
    }

    public static double[] getClassLabels(Instances dataset, int classIndex) {
        return dataset.attributeToDoubleArray(classIndex);
    }
}
