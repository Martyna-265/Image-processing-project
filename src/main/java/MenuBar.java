import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MenuBar extends JMenuBar {
    private PhotoPanel photoPanel;

    public MenuBar(PhotoPanel photoPanel) {
        this.photoPanel = photoPanel;

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

        return editMenu;
    }

    private void onImport() {
        JFileChooser fc = new JFileChooser();

        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "bmp");
        fc.setFileFilter(filter);

        int result = fc.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            String filepath = selectedFile.getAbsolutePath();
            photoPanel.changeImage(filepath);
        }
    }

    private void onSave() {
        System.out.println("Save clicked");
    }
}