package neural;

import java.util.ArrayList;

public class Neuron {

//    private Point coordinate;
    private ArrayList<Integer> inputs;  //wejścia
    private ArrayList<Double> weights;  //wagi

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }

    private double output;  //wartość wyjścia neuronu

    //konstruktor - inicjalizacja neuronu
//    public Neuron() {
//        //tworzenie list wejść i odpowiadających im wag
//        this.inputs = new ArrayList<>();
//        this.weights = new ArrayList<>();
//
//    }
    public Neuron(ArrayList<Integer> inputs) {
        this.weights = new ArrayList<>();
        setInputs(inputs);
        generateWeights();
    }

    //wprowadzanie wejść dla neuronu
    public void setInputs(ArrayList<Integer> inputs) {

        this.inputs = new ArrayList<>(inputs);
//        generateWeights();

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
//        output = sum;
    }

//    pobranie wartości wyjscia danego neuronu
    public double getOutput() {
        calculateOutput();
        return output;
    }

    @Override
    public String toString() {
        return "Neuron{" + "inputs=" + inputs + ", weights=" + weights + ", output=" + output + '}';
    }
  
}
