package proteinprediction.fastaReaders;

import java.io.*;
import java.util.LinkedList;
import proteinprediction.Protein;

/**
 *
 * @author Yann
 */
public class stdFastaReader extends fastaReader {

    @Override
    public LinkedList<Protein> readFile(File f) throws FileNotFoundException {
        String line = new String();
        BufferedReader br = new BufferedReader(new FileReader(f));
        LinkedList<Protein> result = new LinkedList<Protein>();
        while((line = br.readLine()) != null) {
            
        }
        return result;
    }
    
}
