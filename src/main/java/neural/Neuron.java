package neural;

import java.util.ArrayList;

public class Neuron {

    private ArrayList<Integer> inputs;  //wejścia
    private ArrayList<Double> weights;  //wagi
    private int wins;

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }

    private double output;  //wartość wyjścia neuronu

    public Neuron(ArrayList<Integer> inputs) {
        this.wins = 0;
        this.weights = new ArrayList<>();
        setInputs(inputs);
        generateWeights();
    }

    public void setInputs(ArrayList<Integer> inputs) {
        this.inputs = new ArrayList<>(inputs);
    }

    //generacja losowych wag [0,1]
    private void generateWeights() {
        for (Integer input : inputs) {
            weights.add(Math.random());
        }
    }

    //wyliczenie wyjścia neuronu według odpowiedniego wzoru
    //sum - suma wejść pomnożonych przez odpowiadające im wagi
    public void calculateOutput() {
        double sum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            sum += inputs.get(i) * weights.get(i);
        }
        output = (1 / (1 + Math.exp(-sum)));
    }

//    pobranie wartości wyjscia danego neuronu
    public double getOutput() {
        calculateOutput();
        return output;
    }

    public ArrayList<Integer> getInputs() {
        return inputs;
    }

    @Override
    public String toString() {
        return "Neuron{" + "inputs=" + inputs + ", weights=" + weights + ", output=" + output + '}';
    }

    public void win() {
        this.wins += 1;
    }

    public int getWins() {
        return wins;
    }

}
