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
            new Alanine(), new Arginine(), new Asparagine(), new AsparticAcid(), new Cysteine(),
            new GlutamicAcid(), new Glutamine(), new Glycine(), new Histidine(), new Isoleucine(),
            new Leucine(), new Lysine(), new Methionine(), new Phenylalanine(), new Proline(),
            new Serine(), new Threonine(), new Tryptophan(), new Tyrosine(), new Valine()
        };
        
        for(int i = 0; i < as.length; i++) {
            AminoAcid aa = as[i];
            System.out.println(aa.getAminoAcidFullName() + ": (" + aa.getOneLetterCode() + ", " + aa.getThreeLetterCode() + ")");
            System.out.println(aa.get3DStringRepresentation());
            System.out.println("Hydrophathy Index: " + aa.getHydrophathyIndex());
            System.out.println();
        }
        
    }
}
