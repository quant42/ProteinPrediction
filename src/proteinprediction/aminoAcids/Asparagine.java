package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Asparagine extends AminoAcid {

    @Override
    public char getOneLetterCode() {
        return 'N';
    }

    @Override
    public String getThreeLetterCode() {
        return "Asn";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Asparagine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "       O\nO     ||\n\\\\/ \\/ \\\n |  |   OH\nNH2 NH2";
    }
}