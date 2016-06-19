package neural;

import data.GoodPixels;
import java.util.ArrayList;

public class KohonenNetwork {

    public int NUMBER_OF_NEURONS = GoodPixels.getInstance().getGoodValues().size();
    private ArrayList<Neuron> neurons = new ArrayList<>();

    //współczynnik uczenia
    private double alpha = 0.1;
    private double conscience = 0.01;

    private int lastWinnerIndex = -1;
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
            System.out.println("Neuron =>" + i + " ZNAK: " + (char) (i + 48) + " " + GoodPixels.getInstance().getGoodPixels(i));
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
        if (indexBMU != lastWinnerIndex) {
            lastWinnerIndex = indexBMU;
        } else {
            System.out.println("return");
            return;
        }
        for (int i = 0; i < neurons.get(indexBMU).getWeights().size(); i++) {
            double weight = neurons.get(indexBMU).getWeights().get(i) + alpha * (neurons.get(indexBMU).getInputs().get(i) - neurons.get(indexBMU).getWeights().get(i));
            neurons.get(indexBMU).getWeights().set(i, weight);
        }
        updateParameters();
    }

    private int findBMU() {
        double minimumDistance = getDistance(0) * (neurons.get(0).getWins() + 1) * conscience;
        int minimumIndex = 0;
        for (int i = 0; i < neurons.size(); i++) {
            double distance = getDistance(i) * (neurons.get(i).getWins() + 1) * conscience;
//            System.out.println("Neuron: " + i + " Distance: " + distance);
            if (distance < minimumDistance) {
                minimumDistance = distance;
                minimumIndex = i;
            }
        }
        neurons.get(minimumIndex).win();
        System.out.println("Wygrał neuron " + minimumIndex + " ---> " + neurons.get(minimumIndex).getWins() + " razy!");
        return minimumIndex;
    }

    //miara Euklidesa
    private double getDistance(int index) {
        double temp = 0;
        for (int j = 0; j < neurons.size(); j++) {
            temp += Math.pow(neurons.get(index).getInputs().get(j) - neurons.get(index).getWeights().get(j), 2);
        }
//        System.out.println("Neuron: " + index + " Distance: " + Math.sqrt(temp));
        return Math.sqrt(temp);
    }

    private void updateParameters() {
        alpha *= DECAY;
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
