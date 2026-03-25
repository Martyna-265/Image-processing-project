package optionspanels;

import core.*;
import javax.swing.*;
import java.awt.*;

public class EdgeDetectionPanel extends JPanel {
    private PhotoPanel photoPanel;
    private OptionPanel parentPanel;

    public EdgeDetectionPanel(PhotoPanel photoPanel, OptionPanel parentPanel) {
        this.photoPanel = photoPanel;
        this.parentPanel = parentPanel;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));

        buildUI();
    }

    private void buildUI() {
        JLabel titleLabel = new JLabel("Edge Detection:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel typePanel = new JPanel();
        typePanel.setMaximumSize(new Dimension(300, 40));

        String[] options = {"Sobel", "Roberts Cross", "Laplace", "Prewitt Compass", "Sobel Compass"};
        JComboBox<String> typeComboBox = new JComboBox<>(options);

        typePanel.add(new JLabel("Operator: "));
        typePanel.add(typeComboBox);

        JButton applyBtn = new JButton("Apply");
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        applyBtn.addActionListener(e -> {
            parentPanel.saveUndoState(photoPanel.getImageMatrix());
            String operator = (String) typeComboBox.getSelectedItem();

            int[][][] newMatrix = ImageProcessor.applyEdgeDetection(
                    photoPanel.getImageMatrix(),
                    operator,
                    parentPanel.getBoundaryMode()
            );

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