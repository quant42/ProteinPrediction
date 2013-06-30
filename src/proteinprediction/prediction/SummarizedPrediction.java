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

    public SummarizedPrediction() {
    }

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
        this(id, (inMembran.split("\n").length == 4) ? inMembran.split("\n")[1] : (tmhTml.split("\n").length == 4) ? tmhTml.split("\n")[1] : "unknown seq!",
                membranProtein.split("\n")[1], membranProtein.split("\n")[1],
                (inMembran.split("\n").length == 4) ? inMembran.split("\n")[2] : inMembran.split("\n")[1], (inMembran.split("\n").length == 4) ? inMembran.split("\n")[3] : inMembran.split("\n")[2],
                insideOutside.split("\n")[1], insideOutside.split("\n")[2],
                (tmhTml.split("\n").length == 4) ? tmhTml.split("\n")[2] : tmhTml.split("\n")[1], (tmhTml.split("\n").length == 4) ? tmhTml.split("\n")[3] : tmhTml.split("\n")[2]);
    }

    public static double[] parseCommaConv(String s) {
        String[] s_ = s.split(";");
        double[] result = new double[s_.length];
        for (int i = 0; i < s.length(); i++) {
            result[i] = Double.parseDouble(s_[i]);
        }
        return result;
    }

    public static double[] parse09Conv(String s) {
        char[] s_ = s.toCharArray();
        double[] result = new double[s_.length];
        for (int i = 0; i < s_.length; i++) {
            result[i] = ((double) (((int) s_[i]) - 48)) / 10d;
        }
        return result;
    }

    public void predict() {
        char prediction[] = new char[insideOutside.length];
        double predictionConv[] = new double[insideOutside.length];
        boolean[] predicted = new boolean[insideOutside.length];
        for (int i = 0; i < predicted.length; i++) {
            prediction[i] = 'N';
            predictionConv[i] = '0';
            predicted[i] = false;
        }
        // --- for all membran proteins
        if (this.isMembranProtein || (!this.isMembranProtein && this.isMembranProtConv > 0.1 && containsLongInsideMembranRegion())) {
            // first find inside membran regions
            for (int i = 0; i < this.inMembran.length; i++) {
                // take as outside membran
                if (this.inMembran[i] == '-' && this.inMembranConv[i] >= 0.9 && tmhTml[i] == 'H') {
                    prediction[i] = 'o';
                    predictionConv[i] = 0.9;
                    predicted[i] = true;
                }
                // take as inside
                if ((this.inMembran[i] == '+' && this.inMembranConv[i] >= 0.7) || (this.tmhTml[i] == 'L' && this.inMembran[i] == '+')) {
                    prediction[i] = 'i';
                    predictionConv[i] = 0.8;
                    predicted[i] = true;
                }
            }
            // begin and end points
            if (prediction[0] == 'N') {
                prediction[0] = 'o';
                predictionConv[0] = 0.4;
                predicted[0] = true;
            }
            if (prediction[prediction.length - 1] == 'N') {
                prediction[prediction.length - 1] = 'o';
                predictionConv[prediction.length - 1] = 0.4;
                predicted[prediction.length - 1] = true;
            }
            // middle
            boolean changed = false;
            do {
                changed = false;
                for (int i = 1; i < this.inMembran.length - 1; i++) {
                    if (predicted[i]) {
                        continue;
                    }
                    if (this.inMembran[i - 1] == '-' && this.inMembran[i + 1] == '-' && this.inMembran[i] == '-' && this.inMembranConv[i] >= 0.3) {
                        prediction[i] = 'o';
                        predictionConv[i] = 0.5;
                        predicted[i] = true;
                        changed = true;
                    } else if (this.inMembran[i - 1] == '+' && this.inMembran[i + 1] == '+' && this.inMembran[i] == '+' && this.inMembranConv[i] >= 0.3) {
                        prediction[i] = 'i';
                        predictionConv[i] = 0.5;
                        predicted[i] = true;
                        changed = true;
                    }
                }
            } while (changed);
            // middle sum
            for (int i = 1; i < this.inMembran.length - 1; i++) {
                if (predicted[i]) {
                    continue;
                }
                if (predicted[i - 1] && predicted[i + 1]) {
                    if (prediction[i - 1] == 'i' && prediction[i + 1] == 'i') {
                        if (this.inMembran[i] == '+') {
                            prediction[i] = 'i';
                        } else {
                            if (this.inMembranConv[i] >= 0.8) {
                                prediction[i] = 'o';
                            } else {
                                prediction[i] = 'i';
                            }
                        }
                        predictionConv[i] = 0.4;
                        predicted[i] = true;
                    } else if (prediction[i - 1] == 'o' && prediction[i + 1] == 'o') {
                        if (this.inMembran[i] == '+') {
                            if (this.inMembranConv[i] >= 0.8) {
                                prediction[i] = 'i';
                            } else {
                                prediction[i] = 'o';
                            }
                        } else {
                            prediction[i] = 'o';
                        }
                        predictionConv[i] = 0.4;
                        predicted[i] = true;
                    } else {
                        prediction[i] = (this.inMembran[i] == '+') ? 'i' : 'o';
                        predictionConv[i] = 0.4;
                        predicted[i] = true;
                    }
                }
            }
            // now check rest
            do {
                changed = false;
                for (int i = 0; i < this.inMembran.length - 1; i++) {
                    if (prediction[i] == 'i') {
                        int score = 0;
                        int down = -1, up = -1;
                        for (int j = i - 1; j >= 0; j--) {
                            if (prediction[j] == 'i') {
                                score++;
                            } else {
                                down = j;
                                break;
                            }
                        }
                        for (int j = i + 1; j < this.inMembran.length; j++) {
                            if (prediction[j] == 'i') {
                                score++;
                            } else {
                                up = j;
                                break;
                            }
                        }
                        // tmh residue is likely to has a length of at least 4
                        if (score <= 4 && up < this.inMembran.length && up != -1 && down >= 0) {
                            if (!predicted[up] || !predicted[down]) {
                                // append at lowest
                                if (this.inMembran[down] == '+' && this.inMembran[up] == '-' && !predicted[down]) {
                                    prediction[down] = 'i';
                                    predictionConv[down] = 0.3;
                                    predicted[down] = true;
                                    changed = true;
                                } else if (this.inMembran[down] == '-' && this.inMembran[up] == '+' && !predicted[up]) {
                                    prediction[up] = 'i';
                                    predictionConv[up] = 0.3;
                                    predicted[up] = true;
                                    changed = true;
                                } else if (this.inMembran[down] == '+' && this.inMembran[up] == '+' && !predicted[down] && !predicted[up]) {
                                    prediction[down] = 'i';
                                    predictionConv[down] = 0.3;
                                    predicted[down] = true;
                                    prediction[up] = 'i';
                                    predictionConv[up] = 0.3;
                                    predicted[up] = true;
                                    changed = true;
                                } else {
                                    if (this.inMembranConv[down] > this.inMembranConv[up] && !predicted[up]) {
                                        prediction[up] = 'i';
                                        predictionConv[up] = 0.3;
                                        predicted[up] = true;
                                        changed = true;
                                    } else if (!predicted[down]) {
                                        prediction[down] = 'i';
                                        predictionConv[down] = 0.3;
                                        predicted[down] = true;
                                        changed = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } while (changed);
            // set rest to 'o'
            for (int i = 0; i < predicted.length; i++) {
                if (predicted[i] = false) {
                    prediction[i] = 'o';
                    predictionConv[i] = 0.1;
                }
            }
            // clear predicted
            for (int i = 0; i < predicted.length; i++) {
                predicted[i] = false;
            }
            // now transmembran loop helix
            for (int i = 0; i < this.inMembran.length; i++) {
                // find 'i' regions
                if (prediction[i] == 'i') {
                    // how long is this region?
                    int score = 0;
                    for (int j = i; j < this.inMembran.length; j++) {
                        if (prediction[j] != 'i') {
                            break;
                        }
                        score++;
                    }
                    if (score >= 10) {
                        // H
                        for (int j = i; j < this.inMembran.length; j++) {
                            if (prediction[j] != 'i') {
                                break;
                            }
                            prediction[j] = 'H';
                        }
                    } else {
                        // L
                        for (int j = i; j < this.inMembran.length; j++) {
                            if (prediction[j] != 'i') {
                                break;
                            }
                            prediction[j] = 'L';
                        }
                    }
                }
            }
            // now inside or outside cell
            boolean existOutside = false;
            for (int i = 0; i < this.inMembran.length; i++) {
                if (prediction[i] == 'o') {
                    existOutside = true;
                    break;
                }
            }
            if (existOutside) {
                // get highest score
                int pos = -1;
                while (pos == -1 || prediction[pos] != 'o') {
                    pos = highestIndex(this.insideOutsideConv);
                    this.insideOutsideConv[pos] = -1;   // don't get on it double!!!
                }
                prediction[pos] = (this.insideOutside[pos] == 'i') ? 'I' : 'O';
                // extend area
                boolean turn = false, flag = false;
                for (int i = pos - 1; i >= 0; i--) {
                    if (prediction[i] == 'o') {
                        prediction[pos] = (turn) ? ((this.insideOutside[pos] == 'i') ? 'I' : 'O') : ((this.insideOutside[pos] == 'i') ? 'O' : 'I');
                        flag = false;
                    } else if (prediction[i] == 'H' && !flag) {
                        turn = !turn;
                        flag = true;
                    }
                }
                turn = false;
                flag = false;
                for (int i = pos + 1; i < this.insideOutside.length; i++) {
                    if (prediction[i] == 'o') {
                        prediction[pos] = (turn) ? ((this.insideOutside[pos] == 'i') ? 'I' : 'O') : ((this.insideOutside[pos] == 'i') ? 'O' : 'I');
                        flag = false;
                    } else if (prediction[i] == 'H' && !flag) {
                        turn = !turn;
                        flag = true;
                    }
                }
            }
        }
        // ---
        this.prediction = prediction;
        this.predictionConv = predictionConv;
    }

    public boolean containsLongInsideMembranRegion() {
        int score = 0, rscore = 0;
        for (int i = 0; i < this.inMembran.length; i++) {
            if (rscore > 0) {
                rscore += this.inMembran[i] == '+' ? 1 : -1;
            } else {
                rscore = this.inMembran[i] == '+' ? 1 : -1;
            }
            if (rscore > score) {
                score = rscore;
            }
        }
        if (score > 14) {
            return true;
        }
        return false;
    }

    public static boolean allTrue(boolean[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i]) {
                return false;
            }
        }
        return true;
    }

    public static int highestIndex(double[] score) {
        int result = 0;
        for (int i = 1; i < score.length; i++) {
            if (score[i] > score[result]) {
                result = i;
            }
        }
        return result;
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
