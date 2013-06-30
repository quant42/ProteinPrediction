package proteinprediction.prediction;

/**
 *
 * @author spoeri
 */
public class SummarizedPrediction {
    
    public String id;
    public String seq;
    public boolean isMembranProtein;
    public double isMembranProtConv;
    public char[] inMembran;
    public double[] inMembranConv;
    public char[] insideOutside;
    public double[] insideOutsideConv;
    public char[] tmhTml;
    public double[] tmhtmlConv;
    
    public char[] prediction;
    public double[] predictionConv;
    
    public SummarizedPrediction() {}
    public SummarizedPrediction(String id, String seq, boolean isMembranProtein, double isMembranProtConv, char[] inMembran, double[] inMembranConv,
            char[] insideOutside, double[] insideOutsideConv, char[] tmhTml, double[] tmhtmlConv) {
        this.seq = seq;
        this.isMembranProtein = isMembranProtein;
        this.isMembranProtConv = isMembranProtConv;
        this.inMembran = inMembran;
        this.inMembranConv = inMembranConv;
        this.insideOutside = insideOutside;
        this.insideOutsideConv = insideOutsideConv;
        this.tmhTml = tmhTml;
        this.tmhtmlConv = tmhtmlConv;
    }
    public SummarizedPrediction(String id, String seq, String isMembranProtein, String isMembranProtConv, String inMembran, String inMembranConv,
            String insideOutside, String insideOutsideConv, String tmhTml, String tmhtmlConv) {
        this(id, seq, isMembranProtein.charAt(0) == '+', Double.parseDouble(isMembranProtConv.split("\t")[isMembranProtConv.split("\t").length - 1]),
            inMembran.toCharArray(), parse09Conv(inMembranConv), insideOutside.toCharArray(), parseCommaConv(insideOutsideConv), tmhTml.toCharArray(),
            parse09Conv(tmhtmlConv));
    }
    public SummarizedPrediction(String id, String membranProtein, String inMembran, String insideOutside, String tmhTml) {
        this(id, (inMembran.split("\n").length == 4)?inMembran.split("\n")[1]:(tmhTml.split("\n").length == 4)?tmhTml.split("\n")[1]:"unknown seq!",
                membranProtein.split("\n")[1], membranProtein.split("\n")[1],
                (inMembran.split("\n").length == 4)?inMembran.split("\n")[2]:inMembran.split("\n")[1], (inMembran.split("\n").length == 4)?inMembran.split("\n")[3]:inMembran.split("\n")[2],
                insideOutside.split("\n")[1], insideOutside.split("\n")[2],
                (tmhTml.split("\n").length == 4)?tmhTml.split("\n")[2]:tmhTml.split("\n")[1], (tmhTml.split("\n").length == 4)?tmhTml.split("\n")[3]:tmhTml.split("\n")[2]);
    }
    
    public static double[] parseCommaConv(String s) {
        String[] s_ = s.split(";");
        double[] result = new double[s_.length];
        for(int i = 0; i < s.length(); i++) {
            result[i] = Double.parseDouble(s_[i]);
        }
        return result;
    }
    
    public static double[] parse09Conv(String s) {
        char[] s_ = s.toCharArray();
        double[] result = new double[s_.length];
        for(int i = 0; i < s_.length; i++) {
            result[i] = ((double)(((int)s_[i]) - 48)) / 10d;
        }
        return result;
    }
    
    public void predict() {
        char prediction[] = new char[seq.length()];
        double predictionConv[] = new double[seq.length()];
        boolean[] predicted = new boolean[seq.length()];
        for(int i = 0; i < predicted.length; i++) {
            predicted[i] = false;
        }
        // ---
        while(!allTrue(predicted)) {
            break;
        }
        // ---
        this.prediction = prediction;
        this.predictionConv = predictionConv;
    }
    
    public static boolean allTrue(boolean[] arr) {
        for(int i = 0; i < arr.length; i++) {
            if(!arr[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder bf = new StringBuilder();
        bf.append('>').append(this.id).append("\n");
        bf.append(this.seq).append("\n");
        bf.append(this.prediction).append("\n");
        bf.append(this.predictionConv).append("\n");
        bf.append(this.isMembranProtein).append("\n");
        bf.append(this.isMembranProtConv).append("\n");
        bf.append(this.inMembran).append("\n");
        bf.append(this.inMembranConv).append("\n");
        bf.append(this.insideOutside).append("\n");
        bf.append(this.insideOutsideConv).append("\n");
        bf.append(this.tmhTml).append("\n");
        bf.append(this.tmhtmlConv).append("\n");
        return bf.toString();
    }
}
