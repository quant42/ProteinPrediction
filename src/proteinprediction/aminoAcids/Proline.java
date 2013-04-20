package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Proline extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'P';
    }

    @Override
    public String getThreeLetterCode() {
        return "Pro";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Proline";
    }

    @Override
    public String get3DStringRepresentation() {
        return "     O\n    ||\n   /  \\\n/-|    OH\n\\-NH2";
    }
    
    @Override
    public double getHydrophathyIndex() {
        return -1.6;
    }
}