/*
 * Stores run options
 * License: GPL
 */
package proteinprediction;

/**
 * Stores run options
 *
 * @author Shen Wei
 */
class RunOptions {

    /**
     * defines run action of program
     */
    public enum RunActions {

        ACTION_TRAIN,
        ACTION_PREDICT,
        ACTION_VALIDATE,
        ACTION_DATA,
        ACTION_HELP,
        ACTION_TRAIN_META,
        ACTION_PREDICT_META,
        ACTION_VALIDATE_META,
        ACTION_SUMMARIZE
    };
    public static final String MODE_TRAIN = "train";
    public static final String MODE_PREDICT = "predict";
    public static final String MODE_VALIDATE = "validate";
    public static final String MODE_DATA = "prepareData";
    public static final String MODE_TRAIN_META = "trainMeta";
    public static final String MODE_PREDICT_META = "predictMeta";
    public static final String MODE_VALIDATE_META = "validateMeta";
    public static final String MODE_SUMMARIZE = "summarize";
    //public static final String MODE_HELP = "|-h|--help|-help|-?|help|";
    /**
     * action to be performed
     */
    private RunActions action;
    public String inputArff;
    public String inputFasta;
    public String outputArff;
    public String outputFasta;
    public String outputStatistics;
    public int features;
    public String weights;
    public String fastaSeqIn = null;
    public boolean outConvInFasta = false;
    public boolean balanceInput = false;
    public String membranProteinPrediction;
    public String membranLoopAndHelix;
    public String membranInsideOutside;
    public String membranInnerOuterCell;
    public String outputSummary;

    protected RunOptions() {
        this.features = ProgramSettings.NUM_ATTRS;
    }

    public static RunOptions parseArguments(String[] args) {

        RunOptions option = new RunOptions();

        try {
            String mode = args[0];
            option.inputArff = args[1];

            if (mode.equals(MODE_TRAIN)) {
                option.action = RunActions.ACTION_TRAIN;

                if (args.length > 2) {
                    option.features = Integer.parseInt(args[2]);
                }

            } else if (mode.equals(MODE_PREDICT)) {
                option.action = RunActions.ACTION_PREDICT;

                option.outputArff = args[2];
                if (args.length > 3) {
                    option.outputFasta = args[3];
                }
                if (args.length > 4) {
                    option.outConvInFasta = Boolean.parseBoolean(args[4]);
                }
                if (args.length > 5) {
                    option.fastaSeqIn = args[5];
                }

            } else if (mode.equals(MODE_VALIDATE)) {
                option.action = RunActions.ACTION_VALIDATE;

                if (args.length > 2) {
                    option.outputStatistics = args[2];
                }
            } else if (mode.equals(MODE_DATA)) {
                option.action = RunActions.ACTION_DATA;
                option.inputFasta = args[2];
                option.weights = args[3];

                if (args.length > 4) {
                    option.features = Integer.parseInt(args[4]);
                }
            } else if (mode.equals(MODE_TRAIN_META)) {
                option.action = RunActions.ACTION_TRAIN_META;
            } else if (mode.equals(MODE_PREDICT_META)) {
                option.action = RunActions.ACTION_PREDICT_META;
                option.outputArff = args[2];
                if (args.length > 3) {
                    option.outputFasta = args[3];
                }
                if (args.length > 4) {
                    option.outConvInFasta = Boolean.parseBoolean(args[4]);
                }
                if (args.length > 5) {
                    option.fastaSeqIn = args[5];
                }
            } else if (mode.equals(MODE_VALIDATE_META)) {
                option.action = RunActions.ACTION_VALIDATE_META;
                option.outputStatistics = args[2];

                if (args.length > 3) {
                    option.balanceInput = Boolean.parseBoolean(args[3]);
                }
            } else if (mode.equals(MODE_SUMMARIZE)) {
                option.action = RunActions.ACTION_SUMMARIZE;
                
                option.membranProteinPrediction = args[2];
                option.membranInsideOutside = args[3];
                option.membranLoopAndHelix = args[4];
                option.membranInnerOuterCell = args[5];
                option.outputSummary = args[6];
                
            } else {
                option.action = RunActions.ACTION_HELP;
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            option.action = RunActions.ACTION_HELP;
        }

        return option;
    }

    /**
     * get action to be run
     *
     * @return
     */
    public RunActions getRunAction() {
        return action;
    }

    /**
     * return usage informations that can be stored in the RunOptions class
     *
     * @return
     */
    public static String getUsageString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage:\n");
        sb.append("    prepareData <tmps.arff> <imp_strucure.fasta> ");
        sb.append("<comma_separated_weights> [features=");
        sb.append(ProgramSettings.NUM_ATTRS);
        sb.append("]\n");

        sb.append("    train <train_set.arff> [features=");
        sb.append(ProgramSettings.NUM_ATTRS);
        sb.append("]\n");

        sb.append("    predict <input.arff> <result_output.arff> [output.fasta] [include scores in fasta=true/false] [dictionary.fasta]\n");
        sb.append("    validate <validation_set.arff> [statistics.txt]\n");

        sb.append("    trainMeta <train_set.arff>\n");
        sb.append("    predictMeta <input.arff> <result_output.arff> [output.fasta]\n");
        sb.append("    validateMeta <input.arff> <statistics.txt> [balance_input=false]\n");
        sb.append("    validateMeta <input.arff> <statistics.txt> [balance_input=false]\n");
        sb.append("    summarize <proteinPrediction> <insideOutside> <transmembranLoopHelix> <innerOuterCell> <output>\n");
        sb.append("    -h|--help|-help|-?|help\n\n");

        sb.append("Examples:\n");

        sb.append("* Generate training, testing and valdation set with top 200 features:\n");
        sb.append("    prepareData tmps.arff imp_structure.fasta 0.8,0.1,0.1 200\n\n");

        sb.append("* Train model with top 100 features:\n");
        sb.append("    train ./ppData/datasets/01.arff 100\n\n");

        sb.append("* Predict instances in given data set and write results into arff and fasta files:\n");
        sb.append("    predict input.arff result.arff result.fasta\n\n");

        sb.append("* Validate trained model and write validation result into a text file:\n");
        sb.append("    validate validation_set.arff validation_result.txt");
        
        sb.append("* Summarize the prediction from all groups to one single file:\n");
        sb.append("    summarize proteinPrediction.fasta insideOutside.fasta transmembranLoopHelix.fasta innerOuterCell.fasta output");
        return sb.toString();
    }
}
