package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class AsparticAcid extends AminoAcid {

    @Override
    public char getOneLetterCode() {
        return 'D';
    }

    @Override
    public String getThreeLetterCode() {
        return "Asp";
    }

    @Override
    public String getAminoAcidFullName() {
        return "AsparticAcid";
    }

    @Override
    public String get3DStringRepresentation() {
        return "       O\nO     ||\n\\\\/ \\/ \\\n |  |   OH\n OH NH2";
    }
    
    @Override
    public double getHydrophathyIndex() {
        return -3.5;
    }
}