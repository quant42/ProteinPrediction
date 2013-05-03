package proteinprediction.fastaReaders;

import java.io.File;
import java.util.LinkedList;
import proteinprediction.Protein;

/**
 *
 * @author Yann
 */
public abstract class fastaReader {
    
    public abstract LinkedList<Protein> readFile(File f);
    
}
