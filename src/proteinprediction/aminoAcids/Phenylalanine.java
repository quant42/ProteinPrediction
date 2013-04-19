package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Phenylalanine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'F';
    }

    @Override
    public String getThreeLetterCode() {
        return "Phe";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Phenylalanine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "          O\n         ||\n / \\\\/\\ /  \\\n||  |  |    NH2\n \\ //  OH";
    }
    
}