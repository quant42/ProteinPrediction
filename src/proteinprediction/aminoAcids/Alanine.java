package proteinprediction.aminoAcids;

/**
 * This class represents the aminoacid Alanin
 *
 * @author Yann
 */
public class Alanine extends AminoAcid {

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
        return "Alanine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "       O\nCH3   ||\n   \\ /  \\\n    |    OH\n   NH2";
    }
}
