/*
 * This class represents a segment of a protein
 * License: GPL
 */
package proteinprediction.utils;

import java.util.ArrayList;
import java.util.HashMap;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class represents a segment of a protein
 * @author Shen Wei
 */
public class ProteinSegment {
    
    public final String protein;
    
    /**
     * start position of the segment (inclusive)
     */
    public final int start;
    
    /**
     * end position of the segment (exclusive)
     */
    public final int end;
    
    /**
     * constructor
     * @param protein name of the protein
     * @param s start position of the segment (starts from 0, inclusive)
     * @param e end position of the segment (starts from 0, exclusive)
     */
    public ProteinSegment(String protein, int s, int e) {
        this.protein = protein;
        this.start = s;
        this.end = e;
    }
    
    public boolean intersectsWith(ProteinSegment segment) {
        if (!protein.equals(segment.protein)) {
            return false;
        }
        
        return this.start < segment.end && segment.start < this.end;
    }
    
    /**
     * get segments from protein filtered by attribute value
     * @param dataset
     * @param idPosIdx index for ID_pos attribute
     * @param attrVals values of an attribute
     * @param filter filter value of the attribute
     * @return 
     */
    public static HashMap<String, ArrayList<ProteinSegment>> getSegmentsByAttribute(
            Instances dataset, 
            int idPosIdx, 
            double[] attrVals, 
            double filter) 
    {
        HashMap<String, ArrayList<ProteinSegment>> map = 
                new HashMap<String, ArrayList<ProteinSegment>>();
        boolean segmentBegins = false;
        String currentProtein = "";
        int startPos = 0;
        int lastPos = 0;
        
        for (int i = 0; i < dataset.numInstances(); i++) {
            
            double attrVal = attrVals[i];
            
            Instance inst = dataset.instance(i);
            String idPos = inst.stringValue(idPosIdx);
            int delimIdx = idPos.lastIndexOf('_');
            String protein = idPos.substring(0, delimIdx);
            
            int pos = Integer.parseInt(idPos.substring(delimIdx+1));
            
            if (!segmentBegins) {
                if (attrVal == filter) {
                    //begin a new segment
                    currentProtein = protein;
                    startPos = pos;
                    segmentBegins = true;
                } else {
                    continue;
                }
            } else if (attrVal == filter){
                
                //different protein is found, start new segment
                if (!currentProtein.equals(protein)) {
                    ArrayList<ProteinSegment> list = map.get(protein);
                    if (list == null) {
                        list = new ArrayList<ProteinSegment>();
                        map.put(protein, list);
                    }
                    list.add(new ProteinSegment(currentProtein, startPos, lastPos + 1));
                    
                    segmentBegins = true;
                    currentProtein = protein;
                    startPos = pos;
                } else {
                    //segment begins and continues (lastPos = pos - 1)
                    if (lastPos == pos - 1 || lastPos == pos) {
                        //do nothing, extends segment
                    } else {
                        //previous segment ends and start new segment
                        ArrayList<ProteinSegment> list = map.get(protein);
                        if (list == null) {
                            list = new ArrayList<ProteinSegment>();
                            map.put(protein, list);
                        }
                        list.add(new ProteinSegment(currentProtein, startPos, lastPos + 1));
                        segmentBegins = true;
                        currentProtein = protein;
                        startPos = pos;
                    }
                }
            } else if (attrVal != filter) {
                //segment ends
                if (segmentBegins) {
                    segmentBegins = false;
                    ArrayList<ProteinSegment> list = map.get(protein);
                        if (list == null) {
                            list = new ArrayList<ProteinSegment>();
                            map.put(protein, list);
                        }
                    list.add(new ProteinSegment(currentProtein, startPos, lastPos + 1));
                }
            }
            
            lastPos = pos;
        }
        return map;
    }
    
    /**
     * checks whether 2 segments equals
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        ProteinSegment seg = (ProteinSegment) o;
        if (!seg.protein.equals(this.protein)) {
            return false;
        }
        
        return this.start == seg.start && this.end == seg.end;
    }
    
    @Override
    public int hashCode() {
        return this.protein.hashCode() + 7 * start + 49 * end;
    }
}
