package proteinprediction.prediction;

import java.util.HashMap;
import java.io.RandomAccessFile;

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
    
}
