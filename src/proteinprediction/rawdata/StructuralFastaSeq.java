/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.rawdata;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wei
 */
public class StructuralFastaSeq {
    public final String sourceDB;
    public final String uniprotAC;
    public final String uniprotName;
    public final String pdbId;
    public final int ncbiTaxaID;
    public final String lineage;
    public final String[] pdbChains;
    
    //NCBI_TaxID=556(bacteria)
    private static final Pattern patternNcbiTaxa = 
            Pattern.compile("^NCBI_TaxID=(\\d+)\\((\\w+)\\)$");
    
    public String uniprotSeq;
    private String pdbtmSeq;
    private String structuralSeq;
    
    public StructuralFastaSeq(String fastaHeader) {
        String[] parts = fastaHeader.split("\\|");
        //skip '>'
        sourceDB = parts[0].substring(1);
        uniprotAC = parts[1];
        uniprotName = parts[2];
        String ncbiTaxa = parts[3];
        String[] pdbInfo = parts[4].split(":");
        
        Matcher matcher = patternNcbiTaxa.matcher(ncbiTaxa);
        matcher.matches();
        pdbId = pdbInfo[1];
        pdbChains = pdbInfo[2].split("/");

        ncbiTaxaID = Integer.parseInt(matcher.group(1));
        lineage = matcher.group(2);
    }
    
    public void setPDBTMSeq(String seq) {
        this.pdbtmSeq = seq.trim();
    }
    
    public void setStructuralSeq(String seq) {
        this.structuralSeq = seq.trim();
    }

    public char getResidueStructure(int pos) {
        return this.structuralSeq.charAt(pos);
    }
    
    public boolean hasAllEntries() {
        return this.uniprotSeq != null && this.pdbtmSeq != null
                && this.structuralSeq != null;
    }
}
