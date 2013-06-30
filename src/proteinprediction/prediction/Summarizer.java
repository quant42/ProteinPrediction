package proteinprediction.prediction;

import java.util.HashMap;
import java.io.RandomAccessFile;
import java.util.Map;

/**
 *
 * @author quant
 */
public class Summarizer {
    
    /**
     * returns an Hashmap where every protein id is connected to its file seek position
     * @param f
     * @return 
     */
    public static HashMap<String, Long> readPos(RandomAccessFile f) throws Exception {
        HashMap<String, Long> result = new HashMap<String, Long>();
        Long filePos = f.getFilePointer();
        f.seek(0);
        Long cSeek = 0L;
        String cLine = new String();
        while((cLine = f.readLine()) != null) {
            cLine = cLine.trim();
            if(cLine.startsWith(">")) {
                result.put(cLine.substring(1).trim(), cSeek);
            }
            cSeek = f.getFilePointer();
        }
        f.seek(filePos);
        return result;
    }
    
    /**
     * Summerizes Hashmaps
     * 
     * @param protMap
     * @param innerOuterMap
     * @param tlhMap
     * @param insideOutMap
     * @return 
     */
    public static HashMap<String, SummarizedPrediction> summarizeHashMaps(HashMap<String, Long> protMap, HashMap<String, Long> innerOuterMap,
            HashMap<String, Long> tlhMap, HashMap<String, Long> insideOutMap, RandomAccessFile prot, RandomAccessFile innerOuter,
            RandomAccessFile tlh, RandomAccessFile insideOut) throws Exception {
        HashMap<String, SummarizedPrediction> result = new HashMap<String, SummarizedPrediction>();
        for (Map.Entry<String, Long> cObject : protMap.entrySet()) {
            String id = cObject.getKey();
            Long isMembranProtPos = cObject.getValue();
            Long innerOuterMembranPos = innerOuterMap.get(id);
            Long tlhtmlPos = tlhMap.get(id);
            Long insideOutPos = insideOutMap.get(id);
            if(isMembranProtPos != null && innerOuterMembranPos != null && tlhtmlPos != null && insideOutPos != null) {
                result.put(id, new SummarizedPrediction(id, readProt(prot, isMembranProtPos), readProt(innerOuter, innerOuterMembranPos),
                        readProt(insideOut, insideOutPos), readProt(tlh, tlhtmlPos)));
            }
        }
        return result;
    }
    
    public static String readProt(RandomAccessFile f, long l) throws Exception {
        StringBuilder result = new StringBuilder();
        f.seek(l);
        result.append(f.readLine());
        String line;
        while((line = f.readLine()) != null) {
            if(line.trim().startsWith(">")) {
                break;
            }
            result.append("\n").append(line);
        }
        return result.toString();
    }
}
