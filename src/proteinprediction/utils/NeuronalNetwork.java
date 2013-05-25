package proteinprediction.utils;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.LinkedList;

/**
 *
 * @author Yann
 */
public class NeuronalNetwork implements Serializable {

    /**
     * The net defining the neuronal network
     */
    public double[][] net;
    /**
     * The bottom activity of each neuron
     */
    public double[][] node;

    /**
     * private neuronal network for cloning
     */
    private NeuronalNetwork() {
    }

    /**
     * Construct a new neuronal network, out of it's size description, defined
     * by an array of int values.
     */
    public NeuronalNetwork(int k[]) {
        net = node = new double[k.length][];
        int last = 1;
        for (int i = 0; i < net.length; i++) {
            net[i] = new double[k[i] * last];
            last = k[i];
            for (int j = 0; j < net[i].length; j++) {
                net[i][j] = 0;                                  // beginning activity
            }
            for (int j = 0; j < node[i].length; j++) {
                node[i][j] = 0.0005d * Math.random() - 0.25d;   // "bottom" activity
            }
        }
    }

    /**
     * trains the network with a specific training set. Note: this is an
     * evolutionary training algorithm using multiple Threads!!! So it's good to
     * run this algorithm more than once inputspec: pair<inputdata[],
     * outputdata[]>
     */
    public double train(LinkedList<Pair<double[], double[]>> trainingSet, double stopErr, long maxIterations) {
        // training data
        int maxPopulation = 100;    // this is also the number of threads used; don't choose a too high number, Java don't support numberous threads
        int startingPopulation = 5; // the number of Threads that started calculation, should be lower than maxPopuloation
        int flex = 3;               // number of minimal randomized values
        // save "best"
        NeuronalNetwork masterNet = this.clone();
        double masterErr = 0;
        for (Pair<double[], double[]> z : trainingSet) {
            masterErr += calcErr(z.r, masterNet.predict(z.l));
        }
        // neuronal network population (starting population)
        LinkedList<NeuronalNetwork> population = new LinkedList<>();
        for (int i = 0; i < startingPopulation; i++) {
            population.add(this.clone());
        }
        // for each generation
        LinkedList<CalculationThread> popul = new LinkedList<>();
        for (int generations = 0; generations < maxIterations; generations++) {
            // eval new generation
            popul.clear();
            for(NeuronalNetwork n : population) {
                CalculationThread c = new CalculationThread(n, trainingSet, flex);
                c.start();
                popul.add(c);
            }
            // wait until all threads stopped
            for(CalculationThread t : popul) {
                try {
                    t.join();
                } catch(InterruptedException e) {
                    // Do nothing!
                }
            }
            // sort threads after fitness
            Collections.sort(popul, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    double diff = ((CalculationThread) o1).errLvl - ((CalculationThread) o2).errLvl;
                    return (diff == 0) ? 0 : (diff > 0) ? 1 : -1;
                }
            });
            // actulize master neccessary?
            if(masterErr > popul.getFirst().errLvl) {
                masterNet = popul.getFirst().n.clone();
                masterErr = popul.getFirst().errLvl;
            }
            // stopErr > popul.getFirst().errLvl?
            if(stopErr > popul.getFirst().errLvl) {
                break;
            }
//            System.out.println(popul.getFirst().errLvl + " " + flex); // IMPROVMENT OF CODE!!!
            // acclimatize flex
            for(CalculationThread x : popul) {
                if(Math.abs(popul.getFirst().errLvl - x.errLvl) < 0.001) {
                    if(flex >= ((this.getNetworkSize() + 1) << 1)) {
                        break;
                    }
                    flex++;
                } else {
                    break;
                }
            }
            // generate new population
            population.clear();
            // include master
            population.add(masterNet.clone());
            // input the rest
            for(int i = 0; i < ((popul.size() * 2 > maxPopulation) ? maxPopulation : popul.size() * 2) - 1; i++) {
                int k = 0, l = 0;
                while(Math.random() < 0.5) {
                    k++;
                    if(k == popul.size()) {
                        k = 0;
                        break;
                    }
                }
                while(Math.random() < 0.5) {
                    l++;
                    if(l == popul.size()) {
                        l = 0;
                        break;
                    }
                }
                NeuronalNetwork choose1 = popul.get(k).n, choose2 = popul.get(l).n;
                NeuronalNetwork mix = choose1.clone();
                mix.mixWithNeuronalNetwork(choose2);
                population.add(choose1);
            }
        }
        // set new networks weights
        this.net = masterNet.net;
        this.node = masterNet.node;
        return masterErr;
    }

    /**
     * A calculation training thread
     */
    private class CalculationThread extends java.lang.Thread {

        public NeuronalNetwork n;
        public LinkedList<Pair<double[], double[]>> trainingSet;
        public double errLvl;
        public long number = 1;

        public CalculationThread(NeuronalNetwork n, LinkedList<Pair<double[], double[]>> trainingSet, int number) {
            this.n = n.clone();
            this.trainingSet = trainingSet;
            this.number = number;
        }

        @Override
        public void run() {
            // randomizer
            Random rand = new Random();
            // randomly change this neuronal network
            for (int i = 0; i < number; i++) {
                // neuron to change
                int x = rand.nextInt(n.net.length);
                int y = rand.nextInt(n.net[x].length);
                n.net[x][y] += ((rand.nextBoolean()) ? -1 : 1) * Math.random();
            }
            // eval
            double errLevel = 0;
            for (Pair<double[], double[]> z : trainingSet) {
                errLevel += calcErr(z.r, n.predict(z.l));
            }
            this.errLvl = errLevel;
        }
    }

    /**
     * merge two neuronal networks by average
     */
    public void mixWithNeuronalNetwork(NeuronalNetwork anotherNeuronalNetwork) {
        for (int i = 0; i < net.length; i++) {
            for (int j = 0; j < net[i].length; j++) {
                net[i][j] = (net[i][j] + anotherNeuronalNetwork.net[i][j]) / 2;
            }
        }
        for (int i = 0; i < node.length; i++) {
            for (int j = 0; j < net[i].length; j++) {
                node[i][j] = (node[i][j] + anotherNeuronalNetwork.node[i][j]) / 2;
            }
        }
    }

    /**
     * Calculate the size of the network
     */
    public long getNetworkSize() {
        long result = 0;
        for (int i = 0; i < node.length; i++) {
            result += node[i].length;
        }
        return result;
    }

    /**
     * Caclulate the error of the actual prediction method
     */
    public double calcErr(double[] e1, double[] e2) {
        double result = 0;
        for (int i = 0; i < e1.length; i++) {
            result += Math.abs(e1[i] - e2[i]);
        }
        return result;
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
        for (int i = 0; i < layer.length; i++) {
            layer[i] = funct(input[i] * net[0][i] + node[0][i]);
        }
        layers[z++] = layer;
        int lastSize = net[0].length;
        // foreach layer in the network
        for (int i = 1; i < net.length; i++) {
            // foreach neuron in the current layer
            double cLayer[] = new double[net[i].length / lastSize];
            for (int j = 0; j < cLayer.length; j++) {
                // calculate the input stimulus
                double stimulus = 0;
                for (int h = 0; h < layer.length; h++) {
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
        //return Math.tanh(input);
        return 1 / (1 + Math.exp(input));
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
     * read neuronal network data out of a file (in other words), load a
     * neuronal network
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
     * A class defining a pair of two objects
     */
    public static class Pair<L, R> {

        public L l;
        public R r;

        public Pair(L l, R r) {
            this.l = l;
            this.r = r;
        }
    }

    /**
     * Clones the thread
     */
    @Override
    public NeuronalNetwork clone() {
        NeuronalNetwork result = new NeuronalNetwork();
        // cp array sizes
        result.net = new double[net.length][];
        result.node = new double[node.length][];
        // copy
        for (int i = 0; i < net.length; i++) {
            result.net[i] = new double[net[i].length];
            for (int j = 0; j < net[i].length; j++) {
                result.net[i][j] = net[i][j];
            }
        }
        for (int i = 0; i < node.length; i++) {
            result.node[i] = new double[node[i].length];
            for (int j = 0; j < net[i].length; j++) {
                result.node[i][j] = node[i][j];
            }
        }
        return result;
    }

    /**
     * some little test implementations
     *
     * @param args the command line args (don't have any effects on the program
     * sequence)
     */
    public static void main(String[] args) throws Exception {
        NeuronalNetwork n = new NeuronalNetwork(new int[]{2, 3, 3, 1});
        LinkedList<Pair<double[], double[]>> dataset = new LinkedList<Pair<double[], double[]>>();
        dataset.add(new Pair<double[], double[]>(new double[]{ 0,  0}, new double[]{1}));
        dataset.add(new Pair<double[], double[]>(new double[]{ 0,  1}, new double[]{0}));
        dataset.add(new Pair<double[], double[]>(new double[]{ 1,  0}, new double[]{0}));
        dataset.add(new Pair<double[], double[]>(new double[]{ 1,  1}, new double[]{1}));
        n.train(dataset, 0.1, 2000);
        for(int i = 0; i < n.net.length; i++) {
            for(int j = 0; j < n.net[i].length; j++) {
                System.out.print(n.net[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println(n.predict(new double[]{ 0,  0})[0]);
        System.out.println(n.predict(new double[]{ 0,  1})[0]);
        System.out.println(n.predict(new double[]{ 1,  0})[0]);
        System.out.println(n.predict(new double[]{ 1,  1})[0]);
        n.saveNeuronalNetwork(new File("/home/quant/Desktop/test.nnw"));
        NeuronalNetwork n_ = NeuronalNetwork.getNeuronalNetwork(new File("/home/quant/Desktop/test.nnw"));
        System.out.println(n_.predict(new double[]{ 0,  0})[0]);
        System.out.println(n_.predict(new double[]{ 0,  1})[0]);
        System.out.println(n_.predict(new double[]{ 1,  0})[0]);
        System.out.println(n_.predict(new double[]{ 1,  1})[0]);
    }
}
