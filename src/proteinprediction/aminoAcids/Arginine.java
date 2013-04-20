package proteinprediction.aminoAcids;

/**
 * This class represents the aminoacid Alanin
 *
 * @author Yann
 */
public class Arginine extends AminoAcid {

    @Override
    public char getOneLetterCode() {
        return 'R';
    }

    @Override
    public String getThreeLetterCode() {
        return "Arg";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Arginine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "    NH         O\n    ||        ||\n   /  \\ /\\/\\ / \\\nNH2    N    |   OH\n       H    NH2";
    }
    
    @Override
    public double getHydrophathyIndex() {
        return -4.5;
    }
}
