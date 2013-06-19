/*
 * This class provides several quality measure functions for weka prediction
 * License: GPL
 */
package proteinprediction.utils.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class provides several quality measure functions for weka prediction
 * @author Shen Wei
 */
public class QualityMeasure {
    
    public final double[] goldenStandard;
    public final Instances dataset;
    
    /**
     * constructor
     * @param dataset original data set with ID_POS
     * @param golden class labels from golden standard
     */
    public QualityMeasure(
            Instances dataset,
            double[] golden) 
    {
        this.dataset = dataset;
        this.goldenStandard = golden;
    }
    
    /**
     * calculates Q2 score for the prediction. The calculation is based on 
     * protein-wise comparison. (Each protein is the smallest unit here)
     * @return 
     */
    public double q2(double[] predictions) {
        double results = 0;
        //get proteins
        HashMap<Integer, String> instProt = getInstanceToProteinMapping();
        HashMap<String, Integer> proteins = new HashMap<String, Integer>(); 
        
        int protIdx = 0;
        for (String prot : new HashSet<String>(instProt.values())) {
            proteins.put(prot, protIdx++);
        }
        
        final int N = proteins.size();
        
        //confusion "matrix"
        double tp[] = new double[N], 
               tn[] = new double[N], 
               fp[] = new double[N], 
               fn[] = new double[N],
               q2[] = new double[N];
        
        //get confusion "matrix" for each PROTEIN
        for (int i = 0; i < dataset.numInstances(); i++) {
            double gold = this.goldenStandard[i];
            double pred = predictions[i];
            int prot = proteins.get(instProt.get(i));
            if (gold == pred) {
                if (gold == 0) {
                    tp[prot]++;
                } else {
                    tn[prot]++;
                }
            } else {
                if (gold == 0) {
                    fn[prot]++;
                } else {
                    fp[prot]++;
                }
            }
        }
        
        //compute q2
        for (int i = 0; i < N; i++) {
            q2[i] = (tp[i] + tn[i]) / (tp[i] + fp[i] + tn[i] + fn[i]);
        }
        
        for (double qTwo : q2) {
            results += qTwo;
        }
        results /= N;
        return results;
    }
    
    
    
    
    /**
     * calculates MMC measure for the prediction. The comparison unit is amino 
     * acid residue. (residue-wise)
     * @return 
     */
    public double[] mcc(double[] predictions) {
        double tp[] = new double[2], 
               fp[] = new double[2], 
               tn[] = new double[2],
               fn[] = new double[2];
        double result[] = new double[2];
        for (int i = 0; i < predictions.length; i++) {
            double klass = this.goldenStandard[i];
            int classIdx = (int) klass;
            if (klass == predictions[i]) {
                tp[classIdx]++;
                tn[1 - classIdx]++;
            } else {
                fn[classIdx]++;
                fp[1 - classIdx]++;
            }
        }
        
        for (int i = 0; i < result.length; i++) {
            result[i] = (tp[i]*tn[i] - fp[i]*fn[i]) /
                    Math.sqrt((tp[i] + fp[i])*(tp[i]+fn[i])*(tn[i]+fp[i])*(tn[i]+fn[i]));
        }
        
        return result;
    }
    
    /**
     * calculates Qtop measure for prediction. The comparison is region-wise. 
     * Unit for comparison is a region in protein which is TML or TMH. (region)
     * @return 
     */
    public double[] qtop(double[] predictions) {
       double[] results = new double[2];
       return results; 
    }

    /**
     * get mapping from instance index to protein
     * @return 
     */
    private HashMap<Integer, String> getInstanceToProteinMapping() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < dataset.numInstances(); i++) {
            Instance inst = dataset.instance(i);
            String isPosStr = inst.stringValue(0);
            int end = isPosStr.lastIndexOf('_');
            String id = isPosStr.substring(0, end);
            map.put(i, id);
        }
        return map;
    }
    
}
