package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Tyrosine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'Y';
    }

    @Override
    public String getThreeLetterCode() {
        return "Tyr";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Tyrosine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "              O\n             ||\n        / \\ /  \\\n    / \\\\   |    OH\n   ||  |   NH2\n  / \\ //\nHO";
    }
    
}