/*
 * This class provides loader for fasta files combined with structural 
 * information
 * License: GPL
 */
package proteinprediction.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import proteinprediction.rawdata.StructuralFastaSeq;

/**
 * This class provides loader for fasta files combined with structural 
 * information
 * @author Shen Wei
 */
public class StructuralFastaLoader {
   
    /**
     * load imp_structure.fa into RAM
     * @param db File object to the database
     * @return map from uniprot name to structural fasta sequences
     * @throws IOException 
     */
    public static HashMap<String, StructuralFastaSeq> loadFromFile(File db) 
            throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(db));
        HashMap<String, StructuralFastaSeq> map = new 
                HashMap<String, StructuralFastaSeq>();
        String line;
        StructuralFastaSeq sseq = null;
        
        //check how many lines does an entry have
        int lines = -1;
        while ( (line = reader.readLine()) != null) {
            if (lines == -1 && line.startsWith(">")) {
                lines = 1;
                continue;
            }
            
            if (lines > 0 && line.startsWith(">")) {
                break;
            }
            
            lines++;
        }
        reader.close();
        reader = new BufferedReader(new FileReader(db));
        
        while ( (line = reader.readLine()) != null ) {
            //skip empty line
            if (line.length() == 0) continue;
            //meet fasta header
            if (line.charAt(0) == '>') {
                sseq = new StructuralFastaSeq(line);
                map.put(sseq.uniprotName, sseq);
                if (lines == 4) {
                    sseq.uniprotSeq = reader.readLine();
                    sseq.setPDBTMSeq(reader.readLine());
                    sseq.setStructuralSeq(reader.readLine());
                } else if (lines == 3) {
                    sseq.setPDBTMSeq(reader.readLine());
                    sseq.setStructuralSeq(reader.readLine());
                    //no uniprot seq
                    sseq.uniprotSeq = "";
                }
            }
        }
        
        for (StructuralFastaSeq s1 : map.values()) {
            if (!s1.hasAllEntries()) {
                System.err.println(s1.uniprotName);
                System.exit(-1);
            }
        }
        return map;
    }
}
