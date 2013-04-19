package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Tryptophan extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'W';
    }

    @Override
    public String getThreeLetterCode() {
        return "Trp";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Tryptophan";
    }

    @Override
    public String get3DStringRepresentation() {
        return " / \\\\         O\n||  |       ||\n \\ // \\  /\\ /  \\\n    \\  ||  |    OH\n    HN-    NH2";
    }
    
}