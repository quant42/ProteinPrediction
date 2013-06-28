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
    public static HashMap<String, SummarizedPrediction> summarizeHashMaps(HashMap<String, Long> protMap, HashMap<String, Long> innerOuterMap, HashMap<String, Long> tlhMap, HashMap<String, Long> insideOutMap,
            RandomAccessFile prot, RandomAccessFile innerOuter, RandomAccessFile tlh, RandomAccessFile insideOut) {
        HashMap<String, SummarizedPrediction> result = new HashMap<String, SummarizedPrediction>();
        for (Map.Entry<String, Long> cObject : protMap.entrySet()) {
            String key = cObject.getKey();
            Long valProt = cObject.getValue();
        }
        return result;
    }
    
}
