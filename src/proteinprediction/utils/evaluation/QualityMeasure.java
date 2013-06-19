/*
 * This class provides several quality measure functions for weka prediction
 * License: GPL
 */
package proteinprediction.utils.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import proteinprediction.utils.ProteinSegment;
import proteinprediction.utils.DatasetGenerator;
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
                if (gold == 1) {
                    tp[prot]++;
                } else {
                    tn[prot]++;
                }
            } else {
                if (gold == 1) {
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
    public double mcc(double[] predictions) {
        double tp = 0, 
               fp = 0, 
               tn = 0,
               fn = 0;
        double result;
        for (int i = 0; i < predictions.length; i++) {
            double klass = this.goldenStandard[i];
            if (klass == predictions[i]) {
                if (klass == 1) {
                    tp++;
                } else {
                    tn++;
                }
            } else {
                if (klass == 1) {
                    fn++;
                } else {
                    fp++;
                }
            }
        }
        
        result = (tp*tn - fp*fn) /
                Math.sqrt((tp + fp)*(tp+fn)*(tn+fp)*(tn+fn));
        return result;
    }
    
    /**
     * calculates Qtop measure for prediction. The comparison is region-wise. 
     * Unit for comparison is a region in protein which is TML or TMH. (region)
     * @return 
     */
    public double[] qtop(double[] predictions) {
       double[] results = new double[2];
       // ID_pos attribute
       Attribute idPos = dataset.attribute("ID_pos");
        if (idPos == null) {
            throw new IllegalArgumentException("ID_pos attribute is missing!");
        }
       //get golden standard for TMH and TML
       HashMap<String, ArrayList<ProteinSegment>> tmhs = ProteinSegment.getSegmentsByAttribute(
               dataset,
               idPos.index(),
               this.goldenStandard,
               DatasetGenerator.getClassLabels().indexOf("H"));
       
       HashMap<String, ArrayList<ProteinSegment>> tmls = ProteinSegment.getSegmentsByAttribute(
               dataset,
               idPos.index(),
               this.goldenStandard,
               DatasetGenerator.getClassLabels().indexOf("L"));
       
       //get predictions for TMH and TML
       HashMap<String, ArrayList<ProteinSegment>> ptmhs = ProteinSegment.getSegmentsByAttribute(
               dataset, 
               idPos.index(), 
               predictions, 
               DatasetGenerator.getClassLabels().indexOf("H"));
       HashMap<String, ArrayList<ProteinSegment>> ptmls = ProteinSegment.getSegmentsByAttribute(
               dataset, 
               idPos.index(), 
               predictions, 
               DatasetGenerator.getClassLabels().indexOf("L"));
       
       //compute Qtop for TML
       final int tmlProts = tmls.keySet().size();
       ArrayList<String> tmlProteins = new ArrayList<String>();
       tmlProteins.addAll(tmls.keySet());
       
       for (int p = 0; p < tmlProts; p++) {
           String protein = tmlProteins.get(p);
           //System.err.println(protein);
           int correctPreds = getNumberOfCorrectPredictions(
                   tmls.get(protein), ptmls.get(protein));
           double qObs  = correctPreds / (double) tmls.get(protein).size();
           double qPred = correctPreds / (double) ptmls.get(protein).size();
           if (Math.round(qObs * 100) == 100L 
                   && Math.round(qPred * 100) == 100L) {
               results[0]++;
           }
       }
       results[0] /= tmlProts;
       
       //compute Qtop for TMH
       final int tmhProts = tmhs.keySet().size();
       ArrayList<String> tmhProteins = new ArrayList<String>();
       tmhProteins.addAll(tmhs.keySet());
       
       for (int p = 0; p < tmhProts; p++) {
           String protein = tmhProteins.get(p);
           if (ptmhs.get(protein) == null) {
               continue;
           }
           int correctPreds = getNumberOfCorrectPredictions(
                   tmhs.get(protein), ptmhs.get(protein));
           double qObs  = correctPreds / (double) tmhs.get(protein).size();
           double qPred = correctPreds / (double) ptmhs.get(protein).size();
           if (Math.round(qObs * 100) == 100L 
                   && Math.round(qPred * 100) == 100L) {
               results[1]++;
           }
       }
       results[1] /= tmhProts;
       
       return results; 
    }

    /**
     * get mapping from instance index to protein
     * @return 
     */
    private HashMap<Integer, String> getInstanceToProteinMapping() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        Attribute idPos = dataset.attribute("ID_pos");
        if (idPos == null) {
            throw new IllegalArgumentException("ID_pos attribute is missing!");
        }
        for (int i = 0; i < dataset.numInstances(); i++) {
            Instance inst = dataset.instance(i);
            String isPosStr = inst.stringValue(idPos);
            int end = isPosStr.lastIndexOf('_');
            String id = isPosStr.substring(0, end);
            map.put(i, id);
        }
        return map;
    }

    private int getNumberOfCorrectPredictions(
            ArrayList<ProteinSegment> observation, 
            ArrayList<ProteinSegment> prediction) {
        HashSet<ProteinSegment> obsSet = new HashSet();
        
        int num = 0;
        for (int p = 0; p < prediction.size(); p++) {
            
            if (obsSet.size() == observation.size()) {
                //no more correct prediction possible
                break;
            }
            
            ProteinSegment pred = prediction.get(p);
            
            for (int o = 0; o < observation.size(); o++) {
                ProteinSegment obs = observation.get(o);
                
                if (obsSet.contains(obs)) {
                    continue;
                } else if (pred.intersectsWith(obs)){
                    num++;
                    obsSet.add(obs);
                    
                    //prediction can overlap to maximal 1 observation
                    break;
                }
            }
        }
        return num;
    }
}
