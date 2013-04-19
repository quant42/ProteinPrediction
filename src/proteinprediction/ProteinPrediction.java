package proteinprediction;

import proteinprediction.aminoAcids.*;

/**
 *
 * @author Yann
 */
public class ProteinPrediction {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        AminoAcid[] as = new AminoAcid [] {
            new Alanine(), new Arginine(), new Asparagine(), new AsparticAcid()
        };
        
        for(int i = 0; i < as.length; i++) {
            AminoAcid aa = as[i];
            System.out.println(aa.getAminoAcidFullName() + ": (" + aa.getOneLetterCode() + ", " + aa.getThreeLetterCode() + ")");
            System.out.println(aa.get3DStringRepresentation());
            System.out.println();
        }
        
    }
}
