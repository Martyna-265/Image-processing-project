package optionspanels;

import core.*;

import javax.swing.*;
import java.awt.*;

public class SharpeningPanel extends JPanel {
    private PhotoPanel photoPanel;
    private OptionPanel parentPanel;

    public SharpeningPanel(PhotoPanel photoPanel, OptionPanel parentPanel) {
        this.photoPanel = photoPanel;
        this.parentPanel = parentPanel;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));

        buildUI();
    }

    private void buildUI() {
        JLabel titleLabel = new JLabel("Sharpening Filter:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel typePanel = new JPanel();
        typePanel.setMaximumSize(new Dimension(300, 40));
        String[] options = {"Standard", "Strong"};
        JComboBox<String> typeComboBox = new JComboBox<>(options);
        typePanel.add(new JLabel("Intensity: "));
        typePanel.add(typeComboBox);

        JButton applyBtn = new JButton("Apply");
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyBtn.addActionListener(e -> {
            parentPanel.saveUndoState(photoPanel.getImageMatrix());
            String type = (String) typeComboBox.getSelectedItem();

            double[][] mask = ImageProcessor.getSharpeningMask(type);
            int[][][] newMatrix = ImageProcessor.applyConvolution(
                    photoPanel.getImageMatrix(), mask, parentPanel.getBoundaryMode());

            photoPanel.setImageMatrix(newMatrix);
        });

        this.add(titleLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(typePanel);
        this.add(Box.createVerticalStrut(10));
        this.add(applyBtn);
        this.add(Box.createVerticalGlue());
    }
}