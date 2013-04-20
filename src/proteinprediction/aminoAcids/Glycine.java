package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Glycine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'G';
    }

    @Override
    public String getThreeLetterCode() {
        return "Gly";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Glycine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "  O\n ||\n/  \\\n|   OH\nNH2";
    }
    
    @Override
    public double getHydrophathyIndex() {
        return -0.4;
    }
}
