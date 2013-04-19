package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Methionine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'M';
    }

    @Override
    public String getThreeLetterCode() {
        return "Met";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Methionine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "           O\n    S     ||\n   / \\/\\ /  \\\nH3C     |   OH\n        NH2";
    }
    
}
