package proteinprediction.utils;

import java.io.*;

/**
 *
 * @author Yann
 */
public class NeuronalNetwork {

    /**
     * The net defining the neuronal network
     */
    public double[][] net;
    
    /**
     * The bottom activity of each neuron
     */
    public double[][] node;
    
    /**
     * The learningCoefficient defines how fast the neuronal network learns new
     * facts. Note, if this factor is choosen to big, the neuronal network will
     * learn the current training example very good, but it will faster forget, 
     * what it has previously learned. On the other hand, if this coeffizient 
     * is choosen to low, the neuronal network doesn't learn very fast.
     */
    public double learningCoefficient = 0.3;

    public NeuronalNetwork(int k[]) {
        net = node = new double[k.length][];
        int last = 1;
        for(int i = 0; i < net.length; i++) {
            net[i] = new double[k[i] * last];
            last = k[i];
            for(int j = 0; j < net[i].length; j++) {
                net[i][j] = 0;                                  // beginning activity
            }
            for(int j = 0; j < node[i].length; j++) {
                node[i][j] = 0.5d * Math.random() - 0.25d;      // "bottom" activity
            }
        }
    }

    /**
     * trains the network with a specific training example.
     * 
     * @param training training set to train
     */
    public void train(double[] inp, double[] out) {
        // make a layer traceback
        double[] nominal = out;
        for(int layer = net.length - 1; layer >= 0; layer--) {
            double[][] layerStates = predictStates(inp);
            double[] layerOut = layerStates[layer];
            double[] layerInp = (layer == 0)?inp:layerStates[layer - 1];
            for(int i = 0; i < net[layer].length; i++) {
                int nodes = node[layer].length;
                
            }
        }
    }

    /**
     * let the network predict the result of the current input
     * 
     * @param input the neuronal network input
     * @return the result of this method
     */
    public double[] predict(double[] input) {
        return predictStates(input)[net.length - 1];
    }
    
    /**
     * predict all layers (for training reasons)
     */
    private double[][] predictStates(double input[]) {
        // layers
        double[][] layers = new double[net.length][];
        int z = 0;
        // calculate the activity of the "neurons" on the current layer level
        double[] layer = new double[net[0].length];
        for(int i = 0; i < layer.length; i++) {
            layer[i] = funct(input[i] * net[0][i] + node[0][i]);
        }
        layers[z++] = layer;
        int lastSize = net[0].length;
        // foreach layer in the network
        for(int i = 1; i < net.length; i++) {
            // foreach neuron in the current layer
            double cLayer[] = new double[net[i].length / lastSize];
            for(int j = 0; j < cLayer.length; j++) {
                // calculate the input stimulus
                double stimulus = 0;
                for(int h = 0; h < layer.length; h++) {
                    stimulus += layer[h] * net[i][h + j * lastSize];
                }
                cLayer[j] = funct(stimulus + node[i][j]);
            }
            layer = cLayer;
            lastSize = cLayer.length;
            layers[z++] = layer;
        }
        return layers;
    }
    
    /**
     * the function of the current neuron
     * 
     * @param input the sum of the current neuron
     * @return the output of this neuron
     */
    public double funct(double input) {
        return Math.tanh(input);
        //return 1 / (1 + Math.exp(input));
    }
    
    /**
     * Save this (probably trained) network in a file
     * 
     * @param f the network to train
     */
    public void saveNeuronalNetwork(File f) throws Exception {
        ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream(f));
        s.writeObject(this);
        s.close();
    }
    
    /**
     * read neuronal network data out of a file (in other words), load a neuronal
     * network
     * 
     * @param f the file to load
     */
    public static NeuronalNetwork getNeuronalNetwork(File f) throws Exception {
        ObjectInputStream s = new ObjectInputStream(new FileInputStream(f));
        NeuronalNetwork net = (NeuronalNetwork) s.readObject();
        s.close();
        return net;
    }

    /**
     * some little test implementations
     * @param args the command line args (don't have any effects on the program sequence)
     */
    public static void main(String[] args) {
        NeuronalNetwork n = new NeuronalNetwork(new int[]{ 2, 5, 3, 1 });
        for(double x = -3; x <= 3; x += 0.1) {
            for(double y = -3; y <= 3; y += 0.1) {
                n.train(new double[] {x, y}, new double[] {Math.tanh(3 * x + y)});
            }
        }
        for(int i = 0; i < n.net.length; i++) {
            for(int j = 0; j < n.net[i].length; j++) {
                System.out.print(n.net[i][j] + "\n");
            }
            System.out.println();
        }
        System.out.println(n.predict(new double[] { 0, 0 })[0]);
        System.out.println(n.predict(new double[] { 0, 0.01 })[0]);
        System.out.println(n.predict(new double[] { 0.01, 0 })[0]);
        System.out.println(n.predict(new double[] { 1, 1 })[0]);
        System.out.println(n.predict(new double[] { -120, 0 })[0]);
    }
}
