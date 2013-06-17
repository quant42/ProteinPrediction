/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import proteinprediction.prediction.MainPredictor;

/**
 *
 * @author Shen Wei
 */
public class BootstrapModelWriter implements Flushable, Closeable{
    
    private ObjectOutputStream os;
    
    public BootstrapModelWriter(File output) 
    throws FileNotFoundException, IOException
    {
        this.os = new ObjectOutputStream(
                new XZCompressorOutputStream(
                new FileOutputStream(output)));
    }
    
    /**
     * append new model to output file
     * @param model 
     */
    public void write(MainPredictor model) throws IOException {
        this.os.writeObject(model);
        //this prevents ovrerflow of heap space
        this.os.reset();
    }
    
    @Override
    public void flush() throws IOException {
        this.os.flush();
    }
    
    @Override
    public void close() throws IOException {
        this.os.close();
    }
    
}
