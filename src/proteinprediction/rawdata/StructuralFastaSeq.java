/*
 * Data structure to store protein sequences and structural information
 * License: GPL
 */
package proteinprediction.rawdata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Data structure to store uniprot sequences which have PDB sequences and 
 * structural information
 * @author Shen Wei
 */
public class StructuralFastaSeq {
    /**
     * source database of the sequence
     */
    public final String sourceDB;
    
    /**
     * Uniprot accession code
     */
    public final String uniprotAC;
    
    /**
     * Uniprot protein name
     */
    public final String uniprotName;
    
    /**
     * pdb id of the protein structure
     */
    public final String pdbId;
    
    /**
     * NCBI taxonomy: taxa ID
     */
    public final int ncbiTaxaID;
    
    /**
     * NCBI taxonomy: lineage
     */
    public final String lineage;
    
    /**
     * pdb chains that have the sequence
     */
    public final String[] pdbChains;
    
    //NCBI_TaxID=556(bacteria)
    private static final Pattern patternNcbiTaxa = 
            Pattern.compile("^NCBI_TaxID=(\\d+)\\((\\w+)\\)$");
    
    /**
     * Uniprot protein sequence
     */
    public String uniprotSeq;
    
    /**
     * PDBTM protein sequence
     */
    private String pdbtmSeq;
    
    /**
     * Sequence which encodes all structural information
     */
    private String structuralSeq;
    
    /**
     * value for unknown entries
     */
    public static final String UNKNOWN_ATTR = "__unknown__";
    
    /**
     * Constructor
     * @param fastaHeader FASTA header of the corresponding sequences
     */
    public StructuralFastaSeq(String fastaHeader) {
        String[] parts = fastaHeader.split("\\|");
        //skip '>'
        if (parts.length > 2) {
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
        } else {
            sourceDB = "uniprot";
            uniprotAC = parts[0].substring(1);
            uniprotName = uniprotAC;
            pdbId = UNKNOWN_ATTR;
            pdbChains = new String[]{};
            ncbiTaxaID = -1;
            lineage = UNKNOWN_ATTR;
            
        }
    }
    
    /**
     * set protein sequence derived from PDBTM, leading and tailing white spaces
     * are removed for accurate indexing
     * @param seq 
     */
    public void setPDBTMSeq(String seq) {
        this.pdbtmSeq = seq.trim();
    }
    
    /**
     * set sequence with structure annotations, leading and tailing white spaces
     * are removed for accurate indexing
     * @param seq 
     */
    public void setStructuralSeq(String seq) {
        this.structuralSeq = seq.trim();
    }

    /**
     * get structure annotation of a residue
     * @param pos index of the residue in PDBTM sequence, starting from 0
     * @return structural annotation, or throws StringIndexOutOfBoundException
     */
    public char getResidueStructure(int pos) {
        return this.structuralSeq.charAt(pos);
    }
    
    /**
     * test whether this object contains all of uniprot seq, pdbtm seq and 
     * structural information
     * @return 
     */
    public boolean hasAllEntries() {
        return this.uniprotSeq != null && this.pdbtmSeq != null
                && this.structuralSeq != null;
    }
}
