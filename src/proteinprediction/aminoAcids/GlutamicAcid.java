package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class GlutamicAcid extends AminoAcid {

    @Override
    public char getOneLetterCode() {
        return 'E';
    }

    @Override
    public String getThreeLetterCode() {
        return "Glu";
    }

    @Override
    public String getAminoAcidFullName() {
        return "GlutamicAcid";
    }

    @Override
    public String get3DStringRepresentation() {
        return "   O     O\n  ||    ||\n  /\\/\\ / \\\nHO    |   OH\n      NH2";
    }
}
