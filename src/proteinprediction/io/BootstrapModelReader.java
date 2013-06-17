/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import proteinprediction.prediction.MainPredictor;

/**
 *
 * @author Shen Wei
 */
public class BootstrapModelReader implements Closeable{
    
    private ObjectInputStream is;
    
    /**
     * constructor: read models from file
     * @param input
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public BootstrapModelReader(File input) 
            throws FileNotFoundException, IOException {
        this.is = new ObjectInputStream(
                new XZCompressorInputStream(
                new FileInputStream(input)));
    }

    @Override
    public void close() throws IOException {
        this.is.close();
    }
    
    /**
     * read next predictor model from input file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public MainPredictor read() 
            throws IOException, ClassNotFoundException {
        return (MainPredictor) this.is.readObject();
    }
    
}
