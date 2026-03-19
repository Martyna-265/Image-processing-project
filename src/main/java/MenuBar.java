import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MenuBar extends JMenuBar {
    private PhotoPanel photoPanel;
    private int[][][] lastImageMatrix;
    private JFrame frame;
    private OptionPanel optionPanel;
    private EditMenu editMenu;

    public MenuBar(JFrame frame, PhotoPanel photoPanel, OptionPanel optionPanel) {
        this.photoPanel = photoPanel;
        this.frame = frame;
        this.lastImageMatrix = photoPanel.getImageMatrix();
        this.optionPanel = optionPanel;

        JMenu fileMenu = setupFileMenu();
        JMenu displayMenu = setupDisplayMenu();
        editMenu = new EditMenu("Edit", photoPanel, lastImageMatrix, optionPanel);

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
            editMenu.setLastImageMatrix(lastImageMatrix);
            optionPanel.refreshOnImport();
        }
    }

    private void onSave() {
        System.out.println("Save clicked");
    }

}