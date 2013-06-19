package proteinprediction.io;

import java.io.*;
import weka.core.Instances;
import weka.core.Instance;
import java.util.LinkedList;
import proteinprediction.utils.DatasetGenerator;
import weka.core.FastVector;
import java.util.Collections;

/**
 *
 * @author Yann
 */
public class FastaWriter {

    private BufferedWriter out;

    /**
     * generate a new Fastawriter for Outputing a prediction
     */
    public FastaWriter(File f) throws Exception {
        out = new BufferedWriter(new FileWriter(f));
    }

    /**
     * reads a protein seq from a file
     */
    private String getSeq(String seqId, File fastaFile) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(fastaFile));
        String result = new String(), line;
        while ((line = bf.readLine()) != null) {
            line = line.trim();
            if (line.startsWith(">")) {
                if (line.contains(seqId)) {
                    result = bf.readLine();
                    bf.close();
                    return result;
                }
            }
        }
        bf.close();
        return result;
    }

    /**
     * analyse dataset and its prediction
     */
    public void writeDataset(Instances original, double[] prediction, File fastaFile, double[] scores, boolean boolConv) throws Exception {
        boolean boolSeq = (fastaFile != null);
        LinkedList<Data> fasta = new LinkedList<Data>();
        FastVector vec = DatasetGenerator.getClassLabels();
        // saveall instances in new "datastructure"
        for (int i = 0; i < original.numInstances(); i++) {
            Instance curr = original.instance(i);
            String ppNamePos = curr.stringValue(0);
            int splitPos = ppNamePos.lastIndexOf("_");
            if (splitPos == -1) {
                System.err.println("ID_POS ATTRIBUTE WAS NOT FOUND! PLEASE GIVE ME SUCH!");
            }
            String ppName = ppNamePos.substring(0, splitPos);
            char as = ' ';
            int pos = Integer.parseInt(ppNamePos.substring(splitPos + 1));
            if (boolSeq) {
                String l = getSeq(ppName, fastaFile);
                if (l.length() > pos) {
                    as = l.charAt(pos);
                } else {
                    as = ' ';
                }
            }
            fasta.add(new Data(ppName, pos, as, ((String) vec.elementAt((int) prediction[i])).charAt(0), scores[i]));
        }
        // sort the "datasetstructure"
        Collections.sort(fasta);
        // output everything
        boolean flag = false;
        String ppName = new String();
        String seq = new String();
        String pred = new String();
        String conv = new String();
        for (Data d : fasta) {
            if (flag && !ppName.equals(d.proteinName)) {
                // write to file
                writeProtein(ppName, seq, pred, conv, boolSeq, boolConv);
                // clear
                ppName = new String();
                seq = new String();
                pred = new String();
                conv = new String();
            }
            ppName = d.proteinName;
            seq += d.as;
            pred += d.prediction;
            conv += doubleToChar(d.conv);
            flag = true;
        }
        writeProtein(ppName, seq, pred, conv, boolSeq, boolConv);
    }

    private static char doubleToChar(double conv) {
        return ((char) (33 + (int) Math.round(conv * 50)));
    }

    /**
     * write proteins name, sequence and prediction to file
     */
    public void writeProtein(String name, String seq, String prediction, String conv, boolean boolSeq, boolean boolConv) throws Exception {
        out.write(">" + name + "\n");
        if (boolSeq) {
            out.write(seq + "\n");
        }
        out.write(prediction + "\n");
        if (boolConv) {
            out.write(conv + "\n");
        }
    }

    /**
     * close the fasta writer output stream
     */
    public void close() throws Exception {
        out.close();
    }

    /**
     * little helper class for trans. all elements
     */
    private class Data extends Object implements Comparable {

        public String proteinName;
        public int pos;
        public char as;
        public double conv;
        public char prediction;

        public Data(String proteinName, int pos, char as, char prediction, double conv) {
            this.proteinName = proteinName;
            this.pos = pos;
            this.as = as;
            this.conv = conv;
            this.prediction = prediction;
        }

        @Override
        public int compareTo(Object c) {
            Data d = (Data) c;
            int ret = this.proteinName.compareTo(d.proteinName);
            if (ret != 0) {
                return ret;
            } else {
                return this.pos - d.pos;
            }
        }
    }
}