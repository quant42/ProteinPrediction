package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Valine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'V';
    }

    @Override
    public String getThreeLetterCode() {
        return "Val";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Valine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "    CH3  O\n    |   ||\n   / \\ /  \\\nCH3   |    OH\n      NH2";
    }
    
}