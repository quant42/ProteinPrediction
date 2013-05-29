/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinprediction.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serialize and de-serialize java objects
 * @author Shen Wei
 */
public class ObjectIO {
    
    /**
     * serialize object into file
     * @param object
     * @param outputFile
     * @throws IOException 
     */
    public static void serializeObject(
            Object object, 
            File outputFile) throws IOException
    {
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(outputFile));
        
        out.writeObject(object);
        out.flush();
        out.close();
    }
    
    /**
     * deserialize object from file
     * @param inputFile
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static Object deserializeObject(
            File inputFile) 
            throws IOException, ClassNotFoundException
    {
        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(inputFile));
        
        return in.readObject();
    }
    
}
