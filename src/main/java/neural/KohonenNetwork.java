package neural;

import data.GoodPixels;
import java.util.ArrayList;

public class KohonenNetwork {

    public static int NUMBER_OF_NEURONS = 2;
    private ArrayList<Neuron> neurons = new ArrayList<>();

    //promień sąsiedztwa
//    private double lambda = 0.4;
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

    public void setup() {
        for (int i = 0; i < NUMBER_OF_NEURONS; i++) {
            System.out.println("Neuron =>" + i + " ZNAK: " + (char) (i + 65) + " " + GoodPixels.getInstance().getGoodPixels(i));
            neurons.add(new Neuron(GoodPixels.getInstance().getGoodPixels(i)));
        }
    }

    public void train(int number) {
        for (int i = 0; i < number; i++) {
            KohonenNetwork.getInstance().updateWeights();
        }
    }

    public void updateWeights() {

        int indexBMU = findBMU();
        for (int i = 0; i < neurons.get(indexBMU).getWeights().size(); i++) {
            double weight = neurons.get(indexBMU).getWeights().get(i) + alpha * (neurons.get(indexBMU).getInputs().get(i) - neurons.get(indexBMU).getWeights().get(i));
            neurons.get(indexBMU).getWeights().set(i, weight);
        }
        updateParameters();
    }

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
    
    //miara Euklidesa
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
//        lambda *= DECAY;
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
