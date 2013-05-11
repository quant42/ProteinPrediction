package proteinpredictionwekaconverter;

import java.io.*;
import java.util.LinkedList;

/**
 *
 * @author Yann
 */
public class ProteinPredictionWekaConverter {

    public static void main(String[] args) throws Exception {
        // <editor-fold defaultstate="collapsed" desc="read in">
        File f = new File(args[0]);
        int i = Integer.parseInt(args[1]);
        int j = Integer.parseInt(args[2]);
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = new String(), header = new String(), as = new String(), align = new String();
        LinkedList<Data> r = new LinkedList<Data>();
        int stat = 0;
        while((line = br.readLine()) != null) {
            if(stat == 1) {
                as = line;
                stat = 2;
            } else if(stat == 2) {
                align = line;
                stat = 3;
            } else if(stat == 3) {
                r.add(new Data(header, as, align, line));
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
        
        File o = new File("summary" + i + "-" + j + ".arff");
        o.createNewFile();
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(o));
        
        out.write("@RELATION protein interaction\n");
        out.write("\n");
        out.write("@ATTRIBUTE proteinId STRING\n");
        out.write("@ATTRIBUTE taxaId STRING\n");
        out.write("@ATTRIBUTE taxaName STRING\n");
        out.write("@ATTRIBUTE ncbiTaxaId NUMERIC\n");
        out.write("@ATTRIBUTE regna STRING\n");
        out.write("@ATTRIBUTE pdbId STRING\n");
        out.write("@ATTRIBUTE pdbSplit STRING\n");
        out.write("@ATTRIBUTE as" + i + "Seq STRING\n");
        out.write("@ATTRIBUTE stat" + j + " {U,L,H,1,2}\n");
        out.write("\n");
        out.write("@DATA\n");
        out.write("\n");
        
        for(Data x: r) {
            header = x.header.trim();
            as = x.as;
            align = x.align;
            String struct = x.struct;
            
            String splitter[] = header.split("\\|");
            
            // >sp|Q1XA76|ACCN2_CHICK|NCBI_TaxID=9031(vertebrates)|PDB:3HGC:A
            String proteinId = splitter[1];
            String taxaId = splitter[2].split("_")[0];
            String taxaName = splitter[2].split("_")[1];
            String ncbiTaxaId = splitter[3].split("=")[1].split("\\(")[0];
            String regna = splitter[3].split("\\(")[1].substring(0, splitter[3].split("\\(")[1].length() - 1);
            String pdbId = splitter[4].split(":")[1];
            String pdbSplit = splitter[4].split(":")[2];
            
            // MDQETVGNVVLLAIVTLISVVQNGFFAHKVEHESRTQNGRSFQRTGTLAFERVYTANQNCVDAYPTFLAVLWSAGLLCSQVPAAFAGLMYLFVRQKYFVGYLGERTQSTPGYIFGKRIILFLFLMSVAGIFNYYLIFFFGSDFENYIATISTTISPLLLIP
            // 11111111111HHHHHHHHHHHHHHHHHH2222222222222222222222222222HHHHHHHHHHHHHHHHHH1111111HHHHHHHHHHHHHHHHH2222222222222HHHHHHHHHHHHHHHH11111111111111111111UUUUUUUUUUUUU
            for(int z = 0; z < as.length() - i + 1; z++) {
                String wAS = as.substring(z, z + i);
                String wSS = struct.substring(z, z + i);
                char c = wSS.charAt(j);
                if(c == ' ') c = '?';
                
                out.write("\'" + proteinId + "\',\'" + taxaId + "\',\'" + taxaName + "\',\'" + ncbiTaxaId + "\',\'" + regna + "\',\'" + pdbId
                        + "\',\'" + pdbSplit + "\',\'" + wAS + "\',\'" + c + "\'\n");
            }
            
        }
        
        out.close();
    }
    
    static class Data {
        public String header;
        public String as;
        public String align;
        public String struct;
        
        public Data(String header, String as, String align, String struct) {
            this.header = header; this.as = as; this.align = align; this.struct = struct;
        }
        
        @Override
        public String toString() {
            return header + "\n" + as + "\n" + align + "\n" + struct;
        }
    }
}
