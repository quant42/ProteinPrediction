/*
 * This class provides several quality measure functions for weka prediction
 * License: GPL
 */
package proteinprediction.utils.evaluation;

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
    public double[] q2(double[] predictions) {
        double[] results = new double[2];
        
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
    
}
