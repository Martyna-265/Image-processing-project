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
        JMenuItem grayScaleItem = new JMenuItem("Convert to gray scale");
        JMenuItem negativeItem = new JMenuItem("Create a negative");
        JMenuItem brightnessItem = new JMenuItem("Change brightness");
        JMenuItem contrastItem = new JMenuItem("Change contrast");

        undoItem.addActionListener(e -> onUndo());
        grayScaleItem.addActionListener(e -> onGrayScale());
        negativeItem.addActionListener(e -> onNegative());
        brightnessItem.addActionListener(e -> onBrightness());
        contrastItem.addActionListener(e -> onContrast());

        this.add(undoItem);
        this.add(grayScaleItem);
        this.add(negativeItem);
        this.add(brightnessItem);
        this.add(contrastItem);
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
        int height = imageMatrix.length;
        int width = imageMatrix[0].length;
        lastImageMatrix = imageMatrix;
        int[][][] newImageMatrix = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = imageMatrix[y][x][0];
                int g = imageMatrix[y][x][1];
                int b = imageMatrix[y][x][2];
                int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);

                newImageMatrix[y][x][0] = gray;
                newImageMatrix[y][x][1] = gray;
                newImageMatrix[y][x][2] = gray;
            }
        }

        photoPanel.setImageMatrix(newImageMatrix);
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

    private void onBrightness() {
        optionPanel.onBrightness();
    }

    private void onContrast() {
        optionPanel.onContrast();
    }

    public void setLastImageMatrix(int[][][] newMatrix) {
        this.lastImageMatrix = newMatrix;
    }
}
