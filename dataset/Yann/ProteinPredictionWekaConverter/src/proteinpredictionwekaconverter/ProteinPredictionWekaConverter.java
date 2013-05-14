package proteinpredictionwekaconverter;

import java.io.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Yann
 */
public class ProteinPredictionWekaConverter {

    public static void main(String[] args) throws Exception {
        File f = new File(args[0]); // fasta
        File g = new File(args[1]); // arff
        // <editor-fold defaultstate="collapsed" desc="read in fasta">
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = new String(), header = new String(), as = new String(), align = new String();
        HashMap<String, FastaClass> dict = new HashMap<String, FastaClass>();
        int stat = 0;
        while((line = br.readLine()) != null) {
            if(stat == 1) {
                as = line;
                stat = 2;
            } else if(stat == 2) {
                align = line;
                stat = 3;
            } else if(stat == 3) {
                dict.put(header.split("\\|")[2], new FastaClass(header, as, align, line));
                header = new String(); as = new String(); align = new String();
                stat = 0;
            } else {
                line = line.trim();
                if(line.isEmpty())
                    continue;
                else if(line.startsWith(">")) {
                    header = line;
                    stat = 1;
                }
            }
        }
        br.close();
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="open stream">
        File o = new File("summary.arff");
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(o));
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="read arff and output new arf">
        BufferedReader r = new BufferedReader(new FileReader(g));
        stat = 0;
        while((line = r.readLine()) != null) {
            String trimLine = line.trim();
            if(stat == 0) {
                // as long as @DATA didn't appeared, copy
                if(trimLine.equalsIgnoreCase("@DATA")) {
                    out.write("@ATTRIBUTE stat {N,H,L}\n");
                    out.write("@DATA\n");
                    stat = 1;
                } else {
                    out.write(line + "\n");
                }
            } else if(stat == 1) {
                if(trimLine.startsWith("%")) {
                    // comment
                    out.write(line);
                } else {
                    // "coding"
                    String toSearch = trimLine.split(",")[0];
                    int p = toSearch.lastIndexOf("_");
                    String name = toSearch.substring(0, p);
                    int pos = Integer.parseInt(toSearch.substring(p + 1));
                    FastaClass fc = dict.get(name);
                    //String struct = fc.struct.replaceAll(" ", "");
                    String struct = fc.struct;
                    char x = struct.charAt(pos);
                    if(x != 'L' &&  x != 'H') x = 'N';
                    out.write(line + "," + x + "\n");
                }
            }
        }
        r.close();
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="close stream">
        out.close();
        // </editor-fold>
    }
    
    // <editor-fold defaultstate="collapsed" desc="helper fasta class">
    
    static class FastaClass {
        public String header;
        public String as;
        public String align;
        public String struct;
        
        public FastaClass(String header, String as, String align, String struct) {
            this.header = header; this.as = as; this.align = align; this.struct = struct;
        }
        
        @Override
        public String toString() {
            return header + "\n" + as + "\n" + align + "\n" + struct;
        }
    }
    
    // </editor-fold>
}
