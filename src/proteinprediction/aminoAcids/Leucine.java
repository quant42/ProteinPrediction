package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Leucine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'L';
    }

    @Override
    public String getThreeLetterCode() {
        return "Leu";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Leucine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "          O\nH3C      ||\n   \\ /\\ /  \\\n    |  |    OH\n   NH2 NH2";
    }
}
