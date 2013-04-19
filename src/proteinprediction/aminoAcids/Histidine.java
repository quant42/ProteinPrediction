package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Histidine extends AminoAcid {

    @Override
    public char getOneLetterCode() {
        return 'H';
    }

    @Override
    public String getThreeLetterCode() {
        return "His";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Histidine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "           O\n  N       ||\n//  \\ /\\ / \\\n \\  ||  |   OH\n HN /   NH2";
    }
}