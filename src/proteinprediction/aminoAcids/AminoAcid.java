package proteinprediction.aminoAcids;

/**
 * Abstract class representing aminoacids
 * 
 * @author Yann
 */
public abstract class AminoAcid {
    
    // <editor-fold defaultstate="collapsed" desc="class attributes">
    
    /**
     * 
     */
    private AminoAcid[] aminoAcids;
    
    // </editor-fold>
    
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
    
    /**
     * @return An array with all AminoAcids
     */
    public AminoAcid[] getAllAminoAcids() {
        return new AminoAcid[] {
            new Alanin(), // etc.
        };
    }
    
    //public abstract boolean isHydrohil();
    //public abstract boolean isHydrophob();
    
    
}
