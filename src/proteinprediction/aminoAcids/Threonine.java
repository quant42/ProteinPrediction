package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Threonine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'T';
    }

    @Override
    public String getThreeLetterCode() {
        return "Thr";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Threonine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "    OH   O\n    |   ||\n   / \\ /  \\\nH3C   |    OH\n      NH2";
    }
    
    @Override
    public double getHydrophathyIndex() {
        return -0.7;
    }
}