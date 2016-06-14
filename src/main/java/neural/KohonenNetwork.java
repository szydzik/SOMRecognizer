package neural;

import data.GoodPixels;
import java.util.ArrayList;

public class KohonenNetwork {

//    public static int NUMBER_OF_LETTERS = 26;
    public static int NUMBER_OF_LETTERS = 2;

    private ArrayList<Neuron> neurons = new ArrayList<>();

    //promień sąsiedztwa
    private double lambda = 0.4;

    //współczynnik uczenia
    private double alpha = 0.1;

    //współczynnik ograniczania uczenia
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
//        int index = (int) (Math.random() * (neurons.size() - 1));
//
//        Neuron n = neurons.get(index);
//        ArrayList<Double> weights = n.getWeights();

//        int indexBMU = findBMU(n.getWeights());
        int indexBMU = findBMU();

        for (int i = 0; i < neurons.get(indexBMU).getWeights().size(); i++) {
            double weight = alpha * (neurons.get(indexBMU).getInputs().get(i) - neurons.get(indexBMU).getWeights().get(i));
            neurons.get(indexBMU).getWeights().set(i, weight);
        }

//        //Update all the weights
//        for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
//            for (int j = 0; j < n.getWeights().size(); j++) {
//
//                double temp = neurons.get(i).getWeights().get(j);
//                temp += (alpha * Phi(i, indexBMU, lambda) * (n.getWeights().get(j) - temp));
////                temp += alpha  * (n.getWeights().get(j) - temp);
//                neurons.get(i).getWeights().set(j, temp);
//            }
//        }
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
    private int findBMU() {
        double minimumDistance = getDistance(0);
        int minimumIndex = 0;
        for (int i = 0; i < neurons.size(); i++) {
            double distance = getDistance(i);
            if (getDistance(i) < minimumDistance) {
                minimumDistance = distance;
                minimumIndex = i;
            }
        }
        return minimumIndex;
    }

    // Calculates the distance between neurons
//    private double Phi(int index, int index2, double lambda) {
        //exp((-distance(x,y)^2)/(2*lambda^2)
//        double distance = Math.exp(-1.0 * Math.pow(getDistance(index, index2), 2) / (2.0 * Math.pow(lambda, 2)));
//        return distance;
//        return -1;
//    }

    //miara euklidesowa
    private double getDistance(int index) {
        double temp = 0;
        for (int j = 0; j < neurons.size(); j++) {
            temp += Math.pow(neurons.get(index).getInputs().get(j) - neurons.get(index).getWeights().get(j), 2);
        }
        System.out.println("Neuron: " + index + " Distance: " + Math.sqrt(temp));
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

    public void train(int number) {
        for (int i = 0; i < number; i++) {
            KohonenNetwork.getInstance().updateWeights();
        }
    }

}
