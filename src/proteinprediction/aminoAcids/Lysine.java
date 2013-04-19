package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Lysine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'K';
    }

    @Override
    public String getThreeLetterCode() {
        return "Lys";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Lysine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "           O\nH2N       ||\n   \\/\\/\\ /  \\\n        |    OH\n       NH2";
    }
}
