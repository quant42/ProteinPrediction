package proteinprediction.aminoAcids;

/**
 * Abstract class representing aminoacids
 * 
 * @author Yann
 */
public abstract class AminoAcid {
    
    // <editor-fold defaultstate="collapsed" desc="abstract functions">
    
    /**
     * @return The one letter code of this aminoacid
     */
    public abstract char getOneLetterCode();
    
    /**
     * @return The three letter code of this aminoacid
     */
    public abstract String getThreeLetterCode();
    
    /**
     * @return The full name representing this aminoacid
     */
    public abstract String getAminoAcidFullName();
    
    /**
     * @return Returns a string representing the 3D structure of the aminoAcid
     */
    public abstract String get3DStringRepresentation();
    
    // </editor-fold>
    
}
