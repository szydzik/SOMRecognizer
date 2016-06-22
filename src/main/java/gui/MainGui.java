package gui;

import data.GoodPixels;
import gui.components.CustomPanel;
import gui.components.DrawingPanel;
//import neural.Train;
//import neural.TrainingSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import neural.Network;

public class MainGui extends JFrame {

    private final int RESOLUTION = 7;

//    private Train networkTrainer;
    private JPanel mainPanel;
    private DrawingPanel drawingPanel;
    private CustomPanel resultPanel;
    private JButton printButton;
    private JButton clearButton;
    private JButton transformButton;
    private JButton trainNetworkButton;
    private JButton drawLetterButton;
    private JTextField trainingSetsAmount;
    private JComboBox<String> drawLetterCombo;
    private JTextArea outputTextArea;

    public static void main(String[] args) {
        Network.getInstance().setup();
        new MainGui();
    }

    public MainGui() {
        super("Drawing letters using neural networks");

//        networkTrainer = new Train();
        setMainPanel();
        setLeftSide();
        setCenterArea();
        setRightSide();
        setOutputPanel();

        setOnClicks();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(1260, 500));
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        setContentPane(mainPanel);
    }

    private void setLeftSide() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(410, 440));

        drawLetterButton = new JButton("Draw:");
//        drawLetterCombo = new JComboBox<>(new String[]{"A", "B"});//, "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"});
//        drawLetterCombo = new JComboBox<>(new String[]{"0", "1", "2"});
          drawLetterCombo = new JComboBox<>();
          
          for (int i=0; i<GoodPixels.getInstance().getGoodValues().size();i++){
              drawLetterCombo.addItem(""+i);
          }
          
        drawingPanel = new DrawingPanel(400, 400, RESOLUTION);

        panel.add(drawLetterButton);
        panel.add(drawLetterCombo);
        panel.add(drawingPanel);

        mainPanel.add(panel);
    }

    private void setCenterArea() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setPreferredSize(new Dimension(200, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        trainNetworkButton = new JButton("Train Netwoork");
        trainingSetsAmount = new JFormattedTextField("500");
        trainingSetsAmount.setMaximumSize(new Dimension(100, 30));
        trainingSetsAmount.setPreferredSize(new Dimension(100, 30));
        centerPanel.add(trainNetworkButton, gbc);
        centerPanel.add(trainingSetsAmount, gbc);

        centerPanel.add(Box.createVerticalStrut(50));

        centerPanel.add(Box.createVerticalStrut(50));

        transformButton = new JButton(">>");
        centerPanel.add(transformButton, gbc);

        centerPanel.add(Box.createVerticalStrut(50));

        clearButton = new JButton("Clear");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(clearButton, gbc);

        printButton = new JButton("Console print");
        centerPanel.add(printButton, gbc);

        mainPanel.add(centerPanel);
    }

    private void setRightSide() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        resultPanel = new CustomPanel(400, 400, RESOLUTION);
        panel.add(resultPanel);
        mainPanel.add(panel);
    }

    private void setOutputPanel() {
        JPanel outputPanel = new JPanel();
        outputPanel.setPreferredSize(new Dimension(200, 430));

        outputTextArea = new JTextArea();
        outputTextArea.setPreferredSize(new Dimension(200, 430));
        outputPanel.add(outputTextArea);

        mainPanel.add(outputPanel);
    }

    private void setOnClicks() {
        clearButton.addActionListener(e -> drawingPanel.clear());

        transformButton.addActionListener(e -> {

            Network.getInstance().setInputs(drawingPanel.getPixels());
            ArrayList<Double> outputs = Network.getInstance().getOutputs();

            int winIndex = 0;

            for (int i = 0; i < outputs.size(); i++) {
                if (outputs.get(i) > outputs.get(winIndex)) {
                    winIndex = i;
                }
            }
            updateTextArea();
//            trainAsCombo.setSelectedIndex(winIndex);
            resultPanel.drawLetter(GoodPixels.getInstance().getGoodPixels(winIndex));
            drawingPanel.getPixels();
        });

        trainNetworkButton.addActionListener((ActionEvent e) -> {
            int number = 0;
            try {
                number = Integer.parseInt(trainingSetsAmount.getText());
            } catch (Exception x) {
                JOptionPane.showMessageDialog(this, "Wrong input", "ERROR", JOptionPane.PLAIN_MESSAGE);
            }

            //training loop
            Network.getInstance().train(number);

            Network.getInstance().getNeurons().stream().forEach((n) -> {
                System.out.println(n.toString());
            });
        });
//
        drawLetterButton.addActionListener(e -> {
            String letter = (String) drawLetterCombo.getSelectedItem();
            ArrayList<Integer> goodPixels = GoodPixels.getInstance().getGoodPixels(letter);
            drawingPanel.drawLetter(goodPixels);
        });

        drawLetterCombo.addActionListener(e -> {
            String letter = (String) drawLetterCombo.getSelectedItem();
            ArrayList<Integer> goodPixels = GoodPixels.getInstance().getGoodPixels(letter);
            drawingPanel.drawLetter(goodPixels);
        });

        printButton.addActionListener((ActionEvent e) -> {
            ArrayList<Integer> pixels = drawingPanel.getPixels();
            System.out.println(""+pixels);
//            Iterator i = pixels.iterator();
//            System.out.println("");
//            while (i.hasNext()) {
//                Integer element = (Integer) i.next();
//                System.out.print(element);
//                if (i.hasNext()) System.out.print(", ");
//            }
//
//            for (Integer pixel : pixels) {
//                System.out.print(pixel);
//            }
        });
    }

    private void updateTextArea() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Double> outputs = Network.getInstance().getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            int letterValue = i + 48;
            sb.append((char) letterValue);
            double value = outputs.get(i);
//            if (value < 0.01) {
//                value = 0;
//            }
//            if (value > 0.99)
//                value = 1;

            value *= 1000000;
            int x = (int) (value);
            value = x / 1000000.0;

            sb.append("\t " + value);
            sb.append("\n");
        }
        outputTextArea.setText(sb.toString());
    }

}
