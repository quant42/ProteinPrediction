/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import proteinprediction.rawdata.StructuralFastaSeq;

/**
 *
 * @author wei
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
        
        while ( (line = reader.readLine()) != null ) {
            //skip empty line
            if (line.length() == 0) continue;
            //meet fasta header
            if (line.charAt(0) == '>') {
                sseq = new StructuralFastaSeq(line);
                map.put(sseq.uniprotName, sseq);
                sseq.uniprotSeq = reader.readLine();
                sseq.setPDBTMSeq(reader.readLine());
                sseq.setStructuralSeq(reader.readLine());
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
