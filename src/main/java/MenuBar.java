import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MenuBar extends JMenuBar {
    private PhotoPanel photoPanel;
    private int[][][] lastImageMatrix;
    private JFrame frame;

    public MenuBar(JFrame frame, PhotoPanel photoPanel) {
        this.photoPanel = photoPanel;
        this.frame = frame;
        this.lastImageMatrix = photoPanel.getImageMatrix();

        JMenu fileMenu = setupFileMenu();
        JMenu displayMenu = setupDisplayMenu();
        JMenu editMenu = setupEditMenu();

        add(fileMenu);
        add(displayMenu);
        add(editMenu);
    }

    private JMenu setupFileMenu(){
        JMenu fileMenu = new JMenu("File");

        JMenuItem importItem = new JMenuItem("Import");
        JMenuItem saveItem = new JMenuItem("Save");

        importItem.addActionListener(e -> onImport());
        saveItem.addActionListener(e -> onSave());

        fileMenu.add(importItem);
        fileMenu.add(saveItem);

        return fileMenu;
    }

    private JMenu setupDisplayMenu(){
        JMenu displayMenu = new JMenu("Display");

        return displayMenu;
    }

    private JMenu setupEditMenu(){
        JMenu editMenu = new JMenu("Edit");

        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem grayScaleItem = new JMenuItem("Convert to gray scale");
        JMenuItem negativeItem = new JMenuItem("Create a negative");

        undoItem.addActionListener(e -> onUndo());
        grayScaleItem.addActionListener(e -> onGrayScale());
        negativeItem.addActionListener(e -> onNegative());

        editMenu.add(undoItem);
        editMenu.add(grayScaleItem);
        editMenu.add(negativeItem);

        return editMenu;
    }

    private void onImport() {
        JFileChooser fc = new JFileChooser();

        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "bmp");
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);

        int result = fc.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            String filepath = selectedFile.getAbsolutePath();
            String filename = selectedFile.getName();
            photoPanel.changeImage(filepath);
            frame.setTitle(filename);
            lastImageMatrix = photoPanel.getImageMatrix();
        }
    }

    private void onSave() {
        System.out.println("Save clicked");
    }

    private void onUndo() {
        int[][][] temp = photoPanel.getImageMatrix();
        photoPanel.setImageMatrix(lastImageMatrix);
        lastImageMatrix = temp;
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
    }
}