package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Cysteine extends AminoAcid {

    @Override
    public char getOneLetterCode() {
        return 'C';
    }

    @Override
    public String getThreeLetterCode() {
        return "Cys";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Cysteine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "       O\n      ||\n  /\\ /  \\\nHS  |    OH\n    NH2";
    }
}
