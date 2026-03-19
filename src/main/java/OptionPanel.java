import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {

    private PhotoPanel photoPanel;
    private int[][][] originalMatrix;
    private JSlider brightnessSlider;

    public OptionPanel(PhotoPanel photoPanel) {
        this.photoPanel = photoPanel;
    }

    public void onBrightness() {
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));

        JLabel titleLabel = new JLabel("Change brightness:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        brightnessSlider = new JSlider(JSlider.HORIZONTAL, -255, 255, 0);
        brightnessSlider.setMajorTickSpacing(85);
        brightnessSlider.setPaintTicks(true);
        brightnessSlider.setPaintLabels(true);

        brightnessSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, brightnessSlider.getPreferredSize().height));

        originalMatrix = photoPanel.getImageMatrix();

        brightnessSlider.addChangeListener(e -> {
            int offset = brightnessSlider.getValue();
            applyBrightnessOffset(offset);
        });

        JLabel rangeLabel = new JLabel("Extend brightness range to (N1, N2):");
        rangeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel rangeInputsPanel = new JPanel();
        rangeInputsPanel.setLayout(new BoxLayout(rangeInputsPanel, BoxLayout.X_AXIS));
        rangeInputsPanel.setMaximumSize(new Dimension(300, 30));

        JSpinner n1Spinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        JSpinner n2Spinner = new JSpinner(new SpinnerNumberModel(255, 0, 255, 1));

        rangeInputsPanel.add(new JLabel("N1: "));
        rangeInputsPanel.add(n1Spinner);
        rangeInputsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        rangeInputsPanel.add(new JLabel("N2: "));
        rangeInputsPanel.add(n2Spinner);

        JButton applyRangeBtn = new JButton("Apply");
        applyRangeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyRangeBtn.addActionListener(e -> {
            int n1 = (int) n1Spinner.getValue();
            int n2 = (int) n2Spinner.getValue();
            applyBrightnessRange(n1, n2);
        });

        this.add(titleLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(brightnessSlider);

        this.add(Box.createVerticalStrut(40));

        this.add(rangeLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(rangeInputsPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(applyRangeBtn);

        this.add(Box.createVerticalGlue());

        this.revalidate();
        this.repaint();
    }

    private void applyBrightnessOffset(int offset) {
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

    private void applyBrightnessRange(int N1, int N2) {
        int[][][] currentMatrix = photoPanel.getImageMatrix();
        if (currentMatrix == null) return;

        int height = currentMatrix.length;
        int width = currentMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        int J_min = 255;
        int J_max = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int val = currentMatrix[y][x][c];
                    if (val < J_min) J_min = val;
                    if (val > J_max) J_max = val;
                }
            }
        }

        if (J_max == J_min) {
            return;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {

                    int originalValue = currentMatrix[y][x][c];
                    double ratio = (double) (originalValue - J_min) / (J_max - J_min);
                    int newValue = (int) Math.round(ratio * (N2 - N1)) + N1;

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
        originalMatrix = newMatrix;
        if (brightnessSlider != null) {
            brightnessSlider.setValue(0);
        }

    }

    public void refreshOnImport() {
        originalMatrix = photoPanel.getImageMatrix();

        if (brightnessSlider != null) {
            brightnessSlider.setValue(0);
        }
    }

    public void onContrast() {
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));

        JLabel titleLabel = new JLabel("Gamma correction:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel("Gamma factor: 1.0");
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JSlider gammaSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);
        gammaSlider.setMajorTickSpacing(10);
        gammaSlider.setPaintTicks(true);
        gammaSlider.setPaintLabels(true);

        java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
        labelTable.put(0, new JLabel("0"));
        labelTable.put(10, new JLabel("1"));
        labelTable.put(20, new JLabel("2"));
        labelTable.put(30, new JLabel("3"));
        labelTable.put(40, new JLabel("4"));
        labelTable.put(50, new JLabel("5"));

        gammaSlider.setLabelTable(labelTable);
        gammaSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, gammaSlider.getPreferredSize().height));

        originalMatrix = photoPanel.getImageMatrix();

        gammaSlider.addChangeListener(e -> {
            double gammaValue = gammaSlider.getValue() / 10.0;
            valueLabel.setText(String.format("Gamma factor: %.1f", gammaValue));
            applyContrastPower(gammaValue);
        });

        JButton applyGammaBtn = new JButton("Apply");
        applyGammaBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyGammaBtn.addActionListener(e -> {
            originalMatrix = photoPanel.getImageMatrix();
            gammaSlider.setValue(10);
        });

        JLabel logLabel = new JLabel("Log correction:");
        logLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyLogBtn = new JButton("Apply");
        applyLogBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyLogBtn.addActionListener(e -> {
            originalMatrix = photoPanel.getImageMatrix();
            applyContrastLog();
            originalMatrix = photoPanel.getImageMatrix();

            gammaSlider.setValue(10);
        });

        this.add(titleLabel);
        this.add(Box.createVerticalStrut(15));
        this.add(valueLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(gammaSlider);
        this.add(Box.createVerticalStrut(10));
        this.add(applyGammaBtn);

        this.add(Box.createVerticalStrut(40));

        this.add(logLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(applyLogBtn);

        this.add(Box.createVerticalGlue());

        this.revalidate();
        this.repaint();
    }

    private void applyContrastPower(double alpha) {
        if (originalMatrix == null) return;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        int J_max = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int val = originalMatrix[y][x][c];
                    if (val > J_max) J_max = val;
                }
            }
        }

        if (J_max == 0) return;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {

                    int originalValue = originalMatrix[y][x][c];
                    double ratio = (double) (originalValue) / J_max;
                    int newValue = (int) Math.round(255 * Math.pow(ratio, alpha));

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

    private void applyContrastLog() {
        if (originalMatrix == null) return;

        int height = originalMatrix.length;
        int width = originalMatrix[0].length;
        int[][][] newMatrix = new int[height][width][3];

        int J_max = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {
                    int val = originalMatrix[y][x][c];
                    if (val > J_max) J_max = val;
                }
            }
        }

        if (J_max == 0) return;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) {

                    int originalValue = originalMatrix[y][x][c];
                    double ratio = Math.log(1 + originalValue) / Math.log(1 + J_max);
                    int newValue = (int) Math.round(255 * ratio);

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