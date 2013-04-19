package proteinprediction.aminoAcids;

/**
 *
 * @author Yann
 */
public class Selenocysteine extends AminoAcid {
    
    @Override
    public char getOneLetterCode() {
        return 'U';
    }

    @Override
    public String getThreeLetterCode() {
        return "Sec";
    }

    @Override
    public String getAminoAcidFullName() {
        return "Selenocysteine";
    }

    @Override
    public String get3DStringRepresentation() {
        return "        O\n       ||\n   /\\ /  \\\nHSe  |    OH\n     NH2";
    }
    
}