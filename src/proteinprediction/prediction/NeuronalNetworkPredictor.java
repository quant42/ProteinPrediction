package proteinprediction.prediction;

import java.io.File;
import java.io.IOException;
import proteinprediction.utils.NeuronalNetwork;
import weka.core.Instances;

/**
 *
 * @author Yann
 */
public class NeuronalNetworkPredictor extends Predictor {

    /**
     * get an instance of an neuronal network to predict 
     */
    NeuronalNetwork n;
    
    @Override
    public void loadModel(File f) {
        try {
            n = NeuronalNetwork.getNeuronalNetwork(f);
        } catch (Exception ex) {
            System.err.println("Error loading neuronal network data!");
            System.err.println(ex.toString());
            throw new RuntimeException("Unable to load neuronal network data file!");
        }
    }

    @Override
    public void saveModel(File f) {
        try {
            n.saveNeuronalNetwork(f);
        } catch (Exception ex) {
            System.err.println("Error saving Neuronal Network file!");
            System.err.println(ex.toString());
            throw new RuntimeException();
        }
    }

    @Override
    public File predict(File arffFile) throws IOException {
        return null;
    }

    @Override
    public void train(File arffFile, File whereToSafe) throws IOException {
        
    }
    
}
