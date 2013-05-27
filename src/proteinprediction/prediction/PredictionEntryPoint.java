package proteinprediction.prediction;

import proteinprediction.ProgramEntryPoint;

/**
 *
 * @author quant
 */
public class PredictionEntryPoint implements ProgramEntryPoint {

    @Override
    public int run(String[] args) {
        return 0;
    }

    @Override
    public String getUsageAndHelp() {
        return "predict <toPredict.arff> <output.fasta>";
    }

    @Override
    public String getShortDescription() {
        return "predict the HML/TML structure of roteins";
    }

    @Override
    public String getCommandLineArgumentName() {
        return "predict";
    }
    
}
