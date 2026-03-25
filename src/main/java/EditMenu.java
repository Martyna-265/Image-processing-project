import javax.swing.*;

public class EditMenu extends JMenu {
    private PhotoPanel photoPanel;
    private int[][][] lastImageMatrix;
    private OptionPanel optionPanel;

    public EditMenu(String s, PhotoPanel photoPanel, int[][][] lastImageMatrix, OptionPanel optionPanel) {
        super(s);
        this.photoPanel = photoPanel;
        this.lastImageMatrix = lastImageMatrix;
        this.optionPanel = optionPanel;

        JMenuItem undoItem = new JMenuItem("Undo");

        JMenu pixelOperationsMenu = new JMenu("Pixel operations");
        JMenuItem grayScaleItem = new JMenuItem("Convert to gray scale");
        JMenuItem negativeItem = new JMenuItem("Create a negative");
        JMenuItem brightnessItem = new JMenuItem("Change brightness");
        JMenuItem contrastItem = new JMenuItem("Change contrast");
        JMenuItem binarizationItem = new JMenuItem("Apply binarization");

        pixelOperationsMenu.add(grayScaleItem);
        pixelOperationsMenu.add(negativeItem);
        pixelOperationsMenu.add(brightnessItem);
        pixelOperationsMenu.add(contrastItem);
        pixelOperationsMenu.add(binarizationItem);

        JMenu graphicFiltersMenu = new JMenu("Graphic filters");
        JMenuItem blurringItem = new JMenuItem("Blurring");
        JMenuItem sharpeningItem = new JMenuItem("Sharpening");
        JMenuItem customMaskItem = new JMenuItem("Custom Mask");

        graphicFiltersMenu.add(blurringItem);
        graphicFiltersMenu.add(sharpeningItem);
        graphicFiltersMenu.add(customMaskItem);

        undoItem.addActionListener(e -> onUndo());
        grayScaleItem.addActionListener(e -> onGrayScale());
        negativeItem.addActionListener(e -> onNegative());
        brightnessItem.addActionListener(e -> optionPanel.onBrightness());
        contrastItem.addActionListener(e -> optionPanel.onContrast());
        binarizationItem.addActionListener(e -> optionPanel.onBinarization());
        blurringItem.addActionListener(e -> optionPanel.onBlurring());
        sharpeningItem.addActionListener(e -> optionPanel.onSharpening());
        customMaskItem.addActionListener(e -> optionPanel.onCustomMask());

        this.add(undoItem);
        this.add(pixelOperationsMenu);
        this.add(graphicFiltersMenu);
    }

    private void onUndo() {
        int[][][] temp = photoPanel.getImageMatrix();
        photoPanel.setImageMatrix(lastImageMatrix);
        lastImageMatrix = temp;
        if (optionPanel != null) {
            optionPanel.refreshOnImport();
        }
    }

    private void onGrayScale() {
        // weighted average
        int[][][] imageMatrix = photoPanel.getImageMatrix();
        lastImageMatrix = imageMatrix;
        optionPanel.applyGrayscale();
        if (optionPanel != null) {
            optionPanel.refreshOnImport();
        }
    }

    private void onNegative() {
        int[][][] imageMatrix = photoPanel.getImageMatrix();
        int height = imageMatrix.length;
        int width = imageMatrix[0].length;
        lastImageMatrix = imageMatrix;
        int[][][] newImageMatrix = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newImageMatrix[y][x][0] = 255 - imageMatrix[y][x][0];
                newImageMatrix[y][x][1] = 255 - imageMatrix[y][x][1];
                newImageMatrix[y][x][2] = 255 - imageMatrix[y][x][2];
            }
        }

        photoPanel.setImageMatrix(newImageMatrix);
        if (optionPanel != null) {
            optionPanel.refreshOnImport();
        }
    }

    public void setLastImageMatrix(int[][][] newMatrix) {
        this.lastImageMatrix = newMatrix;
    }
}
