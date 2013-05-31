package proteinprediction.io;

import java.io.*;

/**
 *
 * @author Yann
 */
public class FastaWriter {

    /**
     * stream to safe everything in
     */
    private BufferedWriter bf;
    
    /**
     * constuctor for a fasta filewriter
     */
    public FastaWriter(File f) throws IOException {
        this.bf = new BufferedWriter(new FileWriter(f));
    }
    
    /**
     * write data to output file
     */
    public void write(String fastaHeader, String[] fastaLines) throws IOException {
        bf.write(">" + fastaHeader + "\n");
        for(int i = 0; i < fastaLines.length; i++) {
            bf.write(fastaLines[i] + "\n");
        }
    }

    /**
     * close the stream
     */
    public void close() throws IOException {
        bf.close();
    }
    
    /**
     * flush the stream
     */
    public void flush() throws IOException {
        bf.close();
    }
}
