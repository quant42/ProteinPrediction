package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Pyrrolysine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'O';
    }

    @Override
    public String getThreeLetterCode() {
        return "Pyl";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Pyrrolysine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "     N     O\n   // \\   //\n HC   CH-C                 O\n  |   /    \\              //\nH2C -CH     NH-[CH2]4-CH-C\n      \\                |  \\\n       CH3             NH2 OH";
    }
    
}