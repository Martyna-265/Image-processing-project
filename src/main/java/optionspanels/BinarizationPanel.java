package optionspanels;

import core.*;

import javax.swing.*;
import java.awt.*;

public class BinarizationPanel extends JPanel {
    private PhotoPanel photoPanel;
    private OptionPanel parentPanel;
    private int[][][] originalMatrix;
    private boolean isGrayscaleApplied = false;

    public BinarizationPanel(PhotoPanel photoPanel, OptionPanel parentPanel) {
        this.photoPanel = photoPanel;
        this.parentPanel = parentPanel;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));

        this.originalMatrix = photoPanel.getImageMatrix();
        parentPanel.saveUndoState(originalMatrix);

        buildUI();
    }

    private void buildUI() {
        JLabel titleLabel = new JLabel("Binarization:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton grayscaleBtn = new JButton("Convert to Grayscale");
        grayscaleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel spinnerLabel = new JLabel("Set your own threshold:");
        spinnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        spinnerLabel.setEnabled(false);

        JSpinner thresholdSpinner = new JSpinner(new SpinnerNumberModel(128.0, 0.0, 255.0, 1.0));
        thresholdSpinner.setMaximumSize(new Dimension(100, 30));
        thresholdSpinner.setEnabled(false);

        JButton applyBinarizationBtn = new JButton("Apply Segmentation");
        applyBinarizationBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyBinarizationBtn.setEnabled(false);

        grayscaleBtn.addActionListener(e -> {
            parentPanel.saveUndoState(photoPanel.getImageMatrix());
            int[][][] newMatrix = ImageProcessor.applyGrayscale(photoPanel.getImageMatrix(), GrayscalePanel.GrayscaleOptions.LUMINANCE);
            photoPanel.setImageMatrix(newMatrix);
            originalMatrix = newMatrix;
            isGrayscaleApplied = true;

            spinnerLabel.setEnabled(true);
            thresholdSpinner.setEnabled(true);
            applyBinarizationBtn.setEnabled(true);

            photoPanel.updateProjections();
        });

        applyBinarizationBtn.addActionListener(e -> {
            if (!isGrayscaleApplied) return;
            parentPanel.saveUndoState(photoPanel.getImageMatrix());
            double t = (Double) thresholdSpinner.getValue();

            int[][][] newMatrix = ImageProcessor.applySegmentation(photoPanel.getImageMatrix(), t);
            photoPanel.setImageMatrix(newMatrix);
            originalMatrix = newMatrix;

            parentPanel.updateHistogram();
        });

        this.add(titleLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(grayscaleBtn);
        this.add(Box.createVerticalStrut(30));
        this.add(spinnerLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(thresholdSpinner);
        this.add(Box.createVerticalStrut(10));
        this.add(applyBinarizationBtn);
        this.add(Box.createVerticalGlue());
    }
}