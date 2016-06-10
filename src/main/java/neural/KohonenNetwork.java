package neural;

import data.GoodPixels;
import java.util.ArrayList;

public class KohonenNetwork {

    public static int DOWNSAMPLE_HEIGHT = 20;
    public static int DOWNSAMPLE_WIDTH = 20;
    public static int NUMBER_OF_LETTERS = 26;

    private ArrayList<Neuron> neurons = new ArrayList<>();

    private double lambda = 0.4;
    private double alpha = 0.4;

    private static final double DECAY = 0.995;

    public static KohonenNetwork instance = null;

    public static KohonenNetwork getInstance() {
        if (instance == null) {
            instance = new KohonenNetwork();
        }
        return instance;
    }

    public void updateWeights() {

        // Pick a Random neuron
        int index = (int) (Math.random() * (neurons.size() - 1));

        Neuron n = neurons.get(index);
//        ArrayList<Double> weights = n.getWeights();

        int indexBMU = findBMU(n.getWeights());

        //Update all the weights
        for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
            for (int j = 0; j < n.getWeights().size(); j++) {

                double temp = neurons.get(i).getWeights().get(j);
                temp += (alpha * Phi(i, indexBMU, lambda) * (n.getWeights().get(j) - temp));
//                temp += alpha  * (n.getWeights().get(j) - temp);
                neurons.get(i).getWeights().set(j, temp);
            }
        }

        //Updates alpha and theta (Monoton decay)
        updateParameters();
    }

    public void setup() {
        for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
            System.out.println("Neuron =>" + i + " Liteta: " + (char) (i + 65) + " " + GoodPixels.getInstance().getGoodPixels(i));
            neurons.add(new Neuron(GoodPixels.getInstance().getGoodPixels(i)));
        }
    }

//    private int findBMU(double location1, double location2) {
    private int findBMU(ArrayList<Double> weights) {
        double minimumDistance = 100000000.0; // A high initial value which will be overwritten
        int minimumIndex = -1;
        for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
            double distance = 0;
//            double distance = (Math.pow((location1 - weight[i][0]), 2)) + (Math.pow((location2 - weight[i][1]), 2));
//                double distance = (Math.pow((location1 - neurons.get(i).getWeights().get(j)), 2)) + (Math.pow((location2 - neurons.get(i).getWeights().get(j)), 2));
            for (int j = 0; j < neurons.get(i).getWeights().size(); j++) {
                distance += (Math.pow((weights.get(j) - neurons.get(i).getWeights().get(j)), 2));
            }

            if (distance < minimumDistance) {
                minimumDistance = distance;
                minimumIndex = i;
            }
        }
        return minimumIndex;
    }

    // Calculates the distance between neurons
    private double Phi(int index, int index2, double lambda) {
        double distance = Math.exp(-1.0 * Math.pow(getDistance(index, index2), 2) / (2.0 * Math.pow(lambda, 2)));
//        System.out.println("Distance: "+distance);
        return distance;
    }

    private double getDistance(int index, int index2) {
        double temp = 0;
        for (int j = 0; j < neurons.size(); j++) {
            temp += Math.pow(neurons.get(index).getWeights().get(j) - neurons.get(index2).getWeights().get(j), 2);
        }
        return Math.sqrt(temp);
    }

    private void updateParameters() {
        alpha *= DECAY;
        lambda *= DECAY;
    }

    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }

    public void setInputs(ArrayList<Integer> inputs) {
        neurons.stream().forEach((n) -> {
            n.setInputs(inputs);
        });
    }

    public ArrayList<Double> getOutputs() {
        ArrayList<Double> outputs = new ArrayList<>();
        neurons.stream().forEach((n) -> {
            outputs.add(n.getOutput());
        });
        return outputs;
    }

}
