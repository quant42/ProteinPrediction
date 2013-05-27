package proteinprediction.prediction;

import proteinprediction.utils.NeuronalNetwork;
import java.io.File;

/**
 *
 * @author Yann
 */
public class NeuronalNetworkPredictionsMixer {
    
    NeuronalNetwork n;
    
    public NeuronalNetworkPredictionsMixer() {}
    
    public NeuronalNetworkPredictionsMixer(int cMethods) {
        n = new NeuronalNetwork(new int[] {cMethods, 3, 2});
    }
    
    public void loadNeuronalNetwork(File f) throws Exception {
        n = NeuronalNetwork.getNeuronalNetwork(f);
    }
    
    public void mergePredictionData(File[] f, File output) {
        
    }
    
}
