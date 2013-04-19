package proteinprediction;

/**
 *
 * @author Yann
 */
public class ProteinPrediction {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // handle input parameters
        proteinprediction.aminoAcids.Alanin ala = new proteinprediction.aminoAcids.Alanin();
        System.out.println(ala.get3DStringRepresentation());
        
    }
}
