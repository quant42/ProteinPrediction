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
     * analyse dataset and its prediction
     */
    public void writeDataset(Instances original, double[] prediction) throws Exception {
        LinkedList<Data> fasta = new LinkedList<Data>();
        FastVector vec = DatasetGenerator.getClassLabels();
        // saveall instances in new "datastructure"
        for (int i = 0; i < original.numAttributes(); i++) {
            Instance curr = original.instance(i);
            String ppNamePos = curr.stringValue(0);
            int splitPos = ppNamePos.lastIndexOf("_");
            String ppName = ppNamePos.substring(splitPos + 1);
            char as = ' ';// TODO: if as seq is expected
            int pos = Integer.parseInt(ppNamePos.substring(0, splitPos - 1));
            fasta.add(new Data(ppName, pos, as, ((String) vec.elementAt((int) (prediction[i] * 0.5))).charAt(0)));
        }
        // sort the "datasetstructure"
        Collections.sort(fasta);
        // output everything
        boolean flag = false;
        String ppName = new String();
        String seq = new String();
        String pred = new String();
        for (Data d : fasta) {
            if (flag && !ppName.equals(d.proteinName)) {
                // write to file
                writeProtein(ppName, seq, pred);
                // clear
                ppName = new String();
                seq = new String();
                pred = new String();
            }
            ppName = d.proteinName;
            seq += d.as;
            pred += d.prediction;
            flag = true;
        }
        writeProtein(ppName, seq, pred);
    }

    /**
     * write proteins name, sequence and prediction to file
     */
    public void writeProtein(String name, String seq, String prediction) throws Exception {
        out.write(">" + name + "\n");
        //out.write(seq + "\n");// TODO: uncomment, if as seq is expected!
        out.write(prediction + "\n");
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
        public char prediction;

        public Data(String proteinName, int pos, char as, char prediction) {
            this.proteinName = proteinName;
            this.pos = pos;
            this.as = as;
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