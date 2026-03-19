import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {

    private PhotoPanel photoPanel;
    private int[][][] originalMatrix;

    public OptionPanel(PhotoPanel photoPanel) {
        this.photoPanel = photoPanel;
    }

    public void onBrightness() {
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));

        JLabel titleLabel = new JLabel("Change brightness:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider brightnessSlider = new JSlider(JSlider.HORIZONTAL, -255, 255, 0);
        brightnessSlider.setMajorTickSpacing(85);
        brightnessSlider.setPaintTicks(true);
        brightnessSlider.setPaintLabels(true);

        brightnessSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, brightnessSlider.getPreferredSize().height));

        originalMatrix = photoPanel.getImageMatrix();

        brightnessSlider.addChangeListener(e -> {
            int offset = brightnessSlider.getValue();
            applyBrightnessModifier(offset);
        });

        this.add(Box.createVerticalStrut(20));
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(brightnessSlider);
        this.add(Box.createVerticalGlue());

        this.revalidate();
        this.repaint();
    }
    private void applyBrightnessModifier(int offset) {
        if (originalMatrix == null) return;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {

                    int newValue = originalMatrix[y][x][c] + offset;

                    if (newValue > 255) {
                        newValue = 255;
                    } else if (newValue < 0) {
                        newValue = 0;
                    }

                    newMatrix[y][x][c] = newValue;
                }
            }
        }

        photoPanel.setImageMatrix(newMatrix);
    }
}