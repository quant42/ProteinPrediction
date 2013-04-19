package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Isoleucine extends AminoAcid {

    @Override
    public char getOneLetterCode() {
        return 'I';
    }

    @Override
    public String getThreeLetterCode() {
        return "Ile";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Isoleucine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "     CH3  O\nH3C  |   ||\n   \\/ \\ /  \\\n       |    OH\n       NH2";
    }
}