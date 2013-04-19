package proteinprediction.aminoAcids;

/**
 * This class represents the aminoacid Alanin
 *
 * @author Yann
 */
public class Alanin extends AminoAcid {

    @Override
    public char getOneLetterCode() {
        return 'A';
    }

    @Override
    public String getThreeLetterCode() {
        return "Ala";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Alanin";
    }

    @Override
    public String get3DStringRepresentation() {
        return "       O\n"
                + "CH3   ||\n"
                + "   \\ /  \\\n"
                + "    |    OH\n"
                + "   NH2";
    }
}
