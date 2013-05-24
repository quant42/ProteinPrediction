package proteinprediction.utils;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Yann
 */
public class NeuronalNetwork {

    public double[][] net;
    public double learningKoeffizient = 0.5;

    public NeuronalNetwork(int k[]) {
        net = new double[k.length][];
        int last = 1;
        for (int i = 0; i < net.length; i++) {
            net[i] = new double[k[i] * last];
            last = k[i];
            for (int j = 0; j < net[i].length; j++) {
                net[i][j] = Math.random();//0;
            }
        }
    }

    public void train(T training) {
        int lastX = 0, lastY = 0;
        double t = predict(training.inp);
        do {
            int y = lastY + 1, x = lastX;
            if(y >= net[lastX].length) {
                x++; y = 0;
                if(x >= net.length) {
                    x = 0;
                }
            }
            // apply a "simpler" Perzeptron learning rule
            net[x][y] += learningKoeffizient * (training.out - t);
            // set lastX, lastY
            lastY = y; lastX = x;
        } while(lastX != 0);
    }

    public double predict(double[] input) {
        // calculate the activity of the "neurons" on the current layer level
        double[] layer = new double[net[0].length];
        for (int i = 0; i < layer.length; i++) {
            layer[i] = funct(input[i] * net[0][i]);
        }
        int lastSize = net[0].length;
        // foreach layer in the nerwork
        for (int i = 1; i < net.length; i++) {
            // for each neuron in the current layer
            double[] cLayer = new double[net[i].length / lastSize];
            for (int j = 0; j < cLayer.length; j++) {
                // calculate the input stimulus
                double stimulus = 0;
                for (int h = 0; h < layer.length; h++) {
                    stimulus += layer[h] * net[i][h + j * lastSize];
                }
                cLayer[j] = funct(stimulus);
            }
            layer = cLayer;
            lastSize = cLayer.length;
        }
        return layer[0];
    }
    
    public double funct(double input) {
        return Math.tanh(input);
        //return 1 / (1 + Math.exp(input));
    }

    static public class T {

        double[] inp;
        double out;

        public T(double[] inp, double out) {
            this.inp = inp;
            this.out = out;
        }
    }

    public static void main(String[] args) {
        NeuronalNetwork n = new NeuronalNetwork(new int[]{ 2, 5, 3, 1 });
        for(double x = -3; x <= 3; x += 0.1) {
            for(double y = -3; y <= 3; y += 0.1) {
                n.train(new T(new double[] {x, y}, Math.tanh(3 * x + y)));
            }
        }
        for(int i = 0; i < n.net.length; i++) {
            for(int j = 0; j < n.net[i].length; j++) {
                System.out.print(n.net[i][j] + "\n");
            }
            System.out.println();
        }
        System.out.println(n.predict(new double[] { 0, 0 }));
        System.out.println(n.predict(new double[] { 0, 0.01 }));
        System.out.println(n.predict(new double[] { 0.01, 0 }));
        System.out.println(n.predict(new double[] { 1, 1 }));
        System.out.println(n.predict(new double[] { -120, 0 }));
    }
}
