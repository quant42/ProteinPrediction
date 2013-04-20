package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Glutamine extends AminoAcid {

    @Override
    public char getOneLetterCode() {
        return 'Q';
    }

    @Override
    public String getThreeLetterCode() {
        return "Gln";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Glutamine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "   O     O\n  ||    ||\n  /\\/\\ / \\\nNH2   |   OH\n      NH2";
    }
    
    @Override
    public double getHydrophathyIndex() {
        return -3.5;
    }
}
