package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Serine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'S';
    }

    @Override
    public String getThreeLetterCode() {
        return "Ser";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Serine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "       O\n      ||\n  /\\ /  \\\nHO  |    OH\n    NH2";
    }
    
    @Override
    public double getHydrophathyIndex() {
        return -0.8;
    }
}