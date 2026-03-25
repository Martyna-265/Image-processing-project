package core;

import optionspanels.*;

import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {

    // Keep the enum here so core.MenuBar.java doesn't break!
    public enum BoundaryMode {
        CROP, KEEP_ORIGINAL, PAD_BLACK, PAD_WHITE, PAD_GRAY, REPLICATE, MIRROR
    }

    private PhotoPanel photoPanel;
    private EditMenu editMenu;
    private BoundaryMode currentBoundaryMode = BoundaryMode.REPLICATE;

    public OptionPanel(PhotoPanel photoPanel) {
        this.photoPanel = photoPanel;

        // Use BorderLayout so the injected tool panels can expand to fill the space
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 0));
    }

    public void setEditMenu(EditMenu editMenu) {
        this.editMenu = editMenu;
    }

    public void setBoundaryMode(BoundaryMode mode) {
        this.currentBoundaryMode = mode;
    }

    public BoundaryMode getBoundaryMode() {
        return currentBoundaryMode;
    }

    // Universal helper so tool panels can easily save the image state before modifying it
    public void saveUndoState(int[][][] matrixToSave) {
        if (editMenu != null && matrixToSave != null) {
            editMenu.setLastImageMatrix(matrixToSave);
        }
    }

    // Clears the sidebar when a new image is imported
    public void refreshOnImport() {
        this.removeAll();
        this.setPreferredSize(new Dimension(400, 0));
        this.revalidate();
        this.repaint();
    }

    // THIS IS THE MAGIC METHOD!
    // It clears the sidebar and loads whichever tool panel the user clicked.
    public void loadToolPanel(JPanel toolPanel) {
        this.removeAll();

        // Always reset width to 400 in case UIPanels.CustomMaskPanel previously expanded it
        this.setPreferredSize(new Dimension(400, 0));

        // Add the new tool panel to the center
        this.add(toolPanel, BorderLayout.CENTER);

        // Force the app window to visually acknowledge the size reset
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.revalidate();
        }

        this.revalidate();
        this.repaint();
    }
}

//import javax.swing.*;
//import java.awt.*;
//
//public class OptionsPanels.OptionPanel extends JPanel {
//
//    private core.PhotoPanel photoPanel;
//    private int[][][] originalMatrix;
//    private JSlider brightnessSlider;
//    private boolean isGrayscaleApplied = false;
//    private core.EditMenu editMenu;
//    private JButton grayscaleBtn;
//    private JLabel spinnerLabel;
//    private JSpinner thresholdSpinner;
//    private JButton applyBinarizationBtn;
//
//    public OptionsPanels.OptionPanel(core.PhotoPanel photoPanel) {
//        this.photoPanel = photoPanel;
//    }
//
//    public void setEditMenu(core.EditMenu editMenu) {
//        this.editMenu = editMenu;
//    }
//
//    private void saveUndoState() {
//        if (editMenu != null && originalMatrix != null) {
//            editMenu.setLastImageMatrix(originalMatrix);
//        }
//    }
//
//    public void onBrightness() {
//        this.removeAll();
//        this.setPreferredSize(new Dimension(400, 0));
//        Window window = SwingUtilities.getWindowAncestor(this);
//        if (window != null) window.revalidate();
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
//
//        JLabel titleLabel = new JLabel("Change brightness:");
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        brightnessSlider = new JSlider(JSlider.HORIZONTAL, -255, 255, 0);
//        brightnessSlider.setMajorTickSpacing(85);
//        brightnessSlider.setPaintTicks(true);
//        brightnessSlider.setPaintLabels(true);
//
//        brightnessSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, brightnessSlider.getPreferredSize().height));
//
//        originalMatrix = photoPanel.getImageMatrix();
//        saveUndoState();
//
//        brightnessSlider.addChangeListener(e -> {
//            int offset = brightnessSlider.getValue();
//            applyBrightnessOffset(offset);
//        });
//
//        JLabel rangeLabel = new JLabel("Extend brightness range to (N1, N2):");
//        rangeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JPanel rangeInputsPanel = new JPanel();
//        rangeInputsPanel.setLayout(new BoxLayout(rangeInputsPanel, BoxLayout.X_AXIS));
//        rangeInputsPanel.setMaximumSize(new Dimension(300, 30));
//
//        JSpinner n1Spinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
//        JSpinner n2Spinner = new JSpinner(new SpinnerNumberModel(255, 0, 255, 1));
//
//        rangeInputsPanel.add(new JLabel("N1: "));
//        rangeInputsPanel.add(n1Spinner);
//        rangeInputsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
//        rangeInputsPanel.add(new JLabel("N2: "));
//        rangeInputsPanel.add(n2Spinner);
//
//        JButton applyRangeBtn = new JButton("Apply");
//        applyRangeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        applyRangeBtn.addActionListener(e -> {
//            saveUndoState();
//            int n1 = (int) n1Spinner.getValue();
//            int n2 = (int) n2Spinner.getValue();
//            applyBrightnessRange(n1, n2);
//        });
//
//        this.add(titleLabel);
//        this.add(Box.createVerticalStrut(10));
//        this.add(brightnessSlider);
//
//        this.add(Box.createVerticalStrut(40));
//
//        this.add(rangeLabel);
//        this.add(Box.createVerticalStrut(10));
//        this.add(rangeInputsPanel);
//        this.add(Box.createVerticalStrut(10));
//        this.add(applyRangeBtn);
//
//        this.add(Box.createVerticalGlue());
//
//        this.revalidate();
//        this.repaint();
//    }
//
//    public void refreshOnImport() {
//        originalMatrix = photoPanel.getImageMatrix();
//
//        if (brightnessSlider != null) {
//            brightnessSlider.setValue(0);
//        }
//
//        isGrayscaleApplied = false;
//        if (grayscaleBtn != null) grayscaleBtn.setEnabled(true);
//        if (spinnerLabel != null) spinnerLabel.setEnabled(false);
//        if (thresholdSpinner != null) thresholdSpinner.setEnabled(false);
//        if (applyBinarizationBtn != null) applyBinarizationBtn.setEnabled(false);
//    }
//
//    public void onContrast() {
//        this.removeAll();
//        this.setPreferredSize(new Dimension(400, 0));
//        Window window = SwingUtilities.getWindowAncestor(this);
//        if (window != null) window.revalidate();
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
//
//        JLabel titleLabel = new JLabel("Gamma correction:");
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JLabel valueLabel = new JLabel("Gamma factor: 1.0");
//        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
//
//        JSlider gammaSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);
//        gammaSlider.setMajorTickSpacing(10);
//        gammaSlider.setPaintTicks(true);
//        gammaSlider.setPaintLabels(true);
//
//        java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
//        labelTable.put(0, new JLabel("0"));
//        labelTable.put(10, new JLabel("1"));
//        labelTable.put(20, new JLabel("2"));
//        labelTable.put(30, new JLabel("3"));
//        labelTable.put(40, new JLabel("4"));
//        labelTable.put(50, new JLabel("5"));
//
//        gammaSlider.setLabelTable(labelTable);
//        gammaSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, gammaSlider.getPreferredSize().height));
//
//        originalMatrix = photoPanel.getImageMatrix();
//        saveUndoState();
//
//        gammaSlider.addChangeListener(e -> {
//            double gammaValue = gammaSlider.getValue() / 10.0;
//            valueLabel.setText(String.format("Gamma factor: %.1f", gammaValue));
//            applyContrastPower(gammaValue);
//        });
//
//        JButton applyGammaBtn = new JButton("Apply");
//        applyGammaBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        applyGammaBtn.addActionListener(e -> {
//            originalMatrix = photoPanel.getImageMatrix();
//            gammaSlider.setValue(10);
//        });
//
//        JLabel logLabel = new JLabel("Log correction:");
//        logLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JButton applyLogBtn = new JButton("Apply");
//        applyLogBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        applyLogBtn.addActionListener(e -> {
//            originalMatrix = photoPanel.getImageMatrix();
//            applyContrastLog();
//            originalMatrix = photoPanel.getImageMatrix();
//
//            gammaSlider.setValue(10);
//        });
//
//        this.add(titleLabel);
//        this.add(Box.createVerticalStrut(15));
//        this.add(valueLabel);
//        this.add(Box.createVerticalStrut(10));
//        this.add(gammaSlider);
//        this.add(Box.createVerticalStrut(10));
//        this.add(applyGammaBtn);
//
//        this.add(Box.createVerticalStrut(40));
//
//        this.add(logLabel);
//        this.add(Box.createVerticalStrut(10));
//        this.add(applyLogBtn);
//
//        this.add(Box.createVerticalGlue());
//
//        this.revalidate();
//        this.repaint();
//    }
//
//    public void onBinarization() {
//        this.removeAll();
//        this.setPreferredSize(new Dimension(400, 0));
//        Window window = SwingUtilities.getWindowAncestor(this);
//        if (window != null) window.revalidate();
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
//
//        isGrayscaleApplied = false;
//        originalMatrix = photoPanel.getImageMatrix();
//        saveUndoState();
//
//        JLabel titleLabel = new JLabel("Binarization:");
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        grayscaleBtn = new JButton("Convert to Grayscale");
//        grayscaleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        spinnerLabel = new JLabel("Set your own threshold:");
//        spinnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        spinnerLabel.setEnabled(false);
//
//        thresholdSpinner = new JSpinner(new SpinnerNumberModel(128.0, 0.0, 255.0, 1.0));
//        thresholdSpinner.setMaximumSize(new Dimension(100, 30));
//        thresholdSpinner.setEnabled(false);
//
//        applyBinarizationBtn = new JButton("Apply Segmentation");
//        applyBinarizationBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        applyBinarizationBtn.setEnabled(false);
//
//        grayscaleBtn.addActionListener(e -> {
//            saveUndoState();
//            applyGrayscale();
//
//            spinnerLabel.setEnabled(true);
//            thresholdSpinner.setEnabled(true);
//            applyBinarizationBtn.setEnabled(true);
//        });
//
//        applyBinarizationBtn.addActionListener(e -> {
//            saveUndoState();
//            double t = (Double) thresholdSpinner.getValue();
//            applySegmentation(t);
//            originalMatrix = photoPanel.getImageMatrix();
//        });
//
//        this.add(titleLabel);
//        this.add(Box.createVerticalStrut(20));
//        this.add(grayscaleBtn);
//        this.add(Box.createVerticalStrut(30));
//        this.add(spinnerLabel);
//        this.add(Box.createVerticalStrut(10));
//        this.add(thresholdSpinner);
//        this.add(Box.createVerticalStrut(10));
//        this.add(applyBinarizationBtn);
//        this.add(Box.createVerticalGlue());
//
//        this.revalidate();
//        this.repaint();
//    }
//
//    private void applyAveragingFilter(int size, int centerWeight) {
//        double[][] mask = new double[size][size];
//
//        // mask of all 1s
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                mask[i][j] = 1.0;
//            }
//        }
//
//        // centered weight
//        int centerIndex = size / 2;
//        mask[centerIndex][centerIndex] = (double) centerWeight;
//
//        applyConvolution(mask);
//    }
//
//    private void applyGaussianFilter(double sigma) {
//        // calculate size and ensure it's odd
//        int size = (int) Math.ceil(6 * sigma);
//        if (size % 2 == 0) {
//            size++;
//        }
//
//        double[][] mask = new double[size][size];
//        int offset = size / 2;
//
//        for (int y = 0; y < size; y++) {
//            for (int x = 0; x < size; x++) {
//                int cx = x - offset;
//                int cy = y - offset;
//                mask[y][x] = (1.0 / (2.0 * Math.PI * sigma * sigma)) * Math.exp(-(cx * cx + cy * cy) / (2.0 * sigma * sigma));
//            }
//        }
//        applyConvolution(mask);
//    }
//
//    public void onBlurring() {
//        this.removeAll();
//        this.setPreferredSize(new Dimension(400, 0));
//        Window window = SwingUtilities.getWindowAncestor(this);
//        if (window != null) window.revalidate();
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
//
//        JLabel titleLabel = new JLabel("Blurring Filters:");
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        // dropdown to select the blur type
//        JPanel typePanel = new JPanel();
//        typePanel.setMaximumSize(new Dimension(300, 40));
//
//        String[] blurTypes = {"Box Blur", "Gaussian Blur"};
//        JComboBox<String> typeComboBox = new JComboBox<>(blurTypes);
//
//        typePanel.add(new JLabel("Type: "));
//        typePanel.add(typeComboBox);
//
//        // CardLayout for input panels
//        JPanel cardsPanel = new JPanel(new CardLayout());
//        cardsPanel.setMaximumSize(new Dimension(300, 80));
//
//        // --- Box Blur ---
//        JPanel boxPanel = new JPanel(new GridLayout(2, 2, 5, 5));
//        JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 21, 2));
//        JSpinner weightSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
//
//        boxPanel.add(new JLabel("Mask Size (NxN): "));
//        boxPanel.add(sizeSpinner);
//        boxPanel.add(new JLabel("Center Weight: "));
//        boxPanel.add(weightSpinner);
//
//        // --- Gaussian Blur ---
//        JPanel gaussianPanel = new JPanel();
//        JSpinner sigmaSpinner = new JSpinner(new SpinnerNumberModel(1.4, 0.1, 10.0, 0.1));
//
//        gaussianPanel.add(new JLabel("Sigma (\u03C3): "));
//        gaussianPanel.add(sigmaSpinner);
//
//        // add both panels to card layout
//        cardsPanel.add(boxPanel, "Box Blur");
//        cardsPanel.add(gaussianPanel, "Gaussian Blur");
//
//        // listener to swap cards when the dropdown changes
//        typeComboBox.addActionListener(e -> {
//            CardLayout cl = (CardLayout) (cardsPanel.getLayout());
//            cl.show(cardsPanel, (String) typeComboBox.getSelectedItem());
//        });
//
//        // apply button logic
//        JButton applyBtn = new JButton("Apply");
//        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        applyBtn.addActionListener(e -> {
//            originalMatrix = photoPanel.getImageMatrix();
//            saveUndoState();
//
//            String selectedType = (String) typeComboBox.getSelectedItem();
//
//            if ("Box Blur".equals(selectedType)) {
//                int size = (int) sizeSpinner.getValue();
//                int centerWeight = (int) weightSpinner.getValue();
//                applyAveragingFilter(size, centerWeight);
//            } else if ("Gaussian Blur".equals(selectedType)) {
//                double sigma = (double) sigmaSpinner.getValue();
//                applyGaussianFilter(sigma);
//            }
//
//            originalMatrix = photoPanel.getImageMatrix();
//        });
//
//        this.add(titleLabel);
//        this.add(Box.createVerticalStrut(20));
//        this.add(typePanel);
//        this.add(Box.createVerticalStrut(10));
//        this.add(cardsPanel);
//        this.add(Box.createVerticalStrut(10));
//        this.add(applyBtn);
//        this.add(Box.createVerticalGlue());
//
//        this.revalidate();
//        this.repaint();
//    }
//
//    public void onSharpening() {
//        this.removeAll();
//        this.setPreferredSize(new Dimension(400, 0));
//        Window window = SwingUtilities.getWindowAncestor(this);
//        if (window != null) window.revalidate();
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
//
//        JLabel titleLabel = new JLabel("Sharpening Filter:");
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JPanel typePanel = new JPanel();
//        typePanel.setMaximumSize(new Dimension(300, 40));
//
//        String[] options = {"Standard", "Strong"};
//        JComboBox<String> typeComboBox = new JComboBox<>(options);
//
//        typePanel.add(new JLabel("Intensity: "));
//        typePanel.add(typeComboBox);
//
//        JButton applyBtn = new JButton("Apply");
//        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        applyBtn.addActionListener(e -> {
//            originalMatrix = photoPanel.getImageMatrix();
//            saveUndoState();
//            String type = (String) typeComboBox.getSelectedItem();
//            applySharpeningFilter(type);
//            originalMatrix = photoPanel.getImageMatrix();
//        });
//
//        this.add(titleLabel);
//        this.add(Box.createVerticalStrut(20));
//        this.add(typePanel);
//        this.add(Box.createVerticalStrut(10));
//        this.add(applyBtn);
//        this.add(Box.createVerticalGlue());
//
//        this.revalidate();
//        this.repaint();
//    }
//
//    private void applySharpeningFilter(String type) {
//        double[][] mask;
//
//        if ("Strong".equals(type)) {
//            mask = new double[][] {
//                    {-1, -1, -1},
//                    {-1,  9, -1},
//                    {-1, -1, -1}
//            };
//        } else {
//            mask = new double[][] {
//                    { 0, -1,  0},
//                    {-1,  5, -1},
//                    { 0, -1,  0}
//            };
//        }
//
//        applyConvolution(mask);
//    }
//
//
//
//    private BoundaryMode currentBoundaryMode = BoundaryMode.REPLICATE;
//
//    public void setBoundaryMode(BoundaryMode mode) {
//        this.currentBoundaryMode = mode;
//    }
//
//    public void onCustomMask() {
//        this.removeAll();
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        this.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
//
//        JLabel titleLabel = new JLabel("Custom Mask Filter:");
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JPanel sizePanel = new JPanel();
//        sizePanel.setMaximumSize(new Dimension(300, 40));
//        JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 9, 2)); // Odd numbers up to 15
//
//        sizePanel.add(new JLabel("Mask Size (NxN): "));
//        sizePanel.add(sizeSpinner);
//
//        JPanel gridContainer = new JPanel();
//        gridContainer.setLayout(new BoxLayout(gridContainer, BoxLayout.Y_AXIS));
//
//        final JFormattedTextField[][][] fieldsHolder = new JFormattedTextField[1][][];
//
//        Runnable buildGrid = () -> {
//            gridContainer.removeAll();
//            int size = (int) sizeSpinner.getValue();
//            JPanel gridPanel = new JPanel(new GridLayout(size, size, 2, 2));
//
//            // dynamic resizing
//            // 50 pixels per field + 60 pixels of padding/margins
//            int requiredGridWidth = size * 50;
//            int requiredPanelWidth = Math.max(400, requiredGridWidth + 60);
//
//            // Expand the sidebar if needed
//            this.setPreferredSize(new Dimension(requiredPanelWidth, 0));
//
//            Window window = SwingUtilities.getWindowAncestor(this);
//            if (window != null) {
//                window.revalidate();
//            }
//
//            gridPanel.setMaximumSize(new Dimension(requiredGridWidth, requiredGridWidth));
//
//            java.text.DecimalFormat df = new java.text.DecimalFormat("0.#");
//
//            JFormattedTextField[][] fields = new JFormattedTextField[size][size];
//            for (int r = 0; r < size; r++) {
//                for (int c = 0; c < size; c++) {
//                    fields[r][c] = new JFormattedTextField(df);
//                    fields[r][c].setValue(0.0);
//                    fields[r][c].setHorizontalAlignment(JTextField.CENTER);
//                    gridPanel.add(fields[r][c]);
//                }
//            }
//            fields[size / 2][size / 2].setValue(1.0);
//            fieldsHolder[0] = fields;
//
//            gridContainer.add(gridPanel);
//            gridContainer.revalidate();
//            gridContainer.repaint();
//        };
//
//        sizeSpinner.addChangeListener(e -> buildGrid.run());
//        buildGrid.run();
//
//        JButton applyBtn = new JButton("Apply");
//        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        applyBtn.addActionListener(e -> {
//            int size = (int) sizeSpinner.getValue();
//            double[][] customMask = new double[size][size];
//            JFormattedTextField[][] fields = fieldsHolder[0];
//
//            for (int r = 0; r < size; r++) {
//                for (int c = 0; c < size; c++) {
//                    Number num = (Number) fields[r][c].getValue();
//                    customMask[r][c] = num != null ? num.doubleValue() : 0.0;
//                }
//            }
//
//            originalMatrix = photoPanel.getImageMatrix();
//            saveUndoState();
//
//            applyConvolution(customMask);
//
//            originalMatrix = photoPanel.getImageMatrix();
//        });
//
//        this.add(titleLabel);
//        this.add(Box.createVerticalStrut(20));
//        this.add(sizePanel);
//        this.add(Box.createVerticalStrut(15));
//        this.add(gridContainer);
//        this.add(Box.createVerticalStrut(20));
//        this.add(applyBtn);
//        this.add(Box.createVerticalGlue());
//
//        this.revalidate();
//        this.repaint();
//    }
//
//}