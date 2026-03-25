package core;

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
        JMenu settingsMenu = setupSettingsMenu(optionPanel);

        optionPanel.setEditMenu(editMenu);

        add(fileMenu);
        add(displayMenu);
        add(editMenu);
        add(settingsMenu);
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

    private JMenu setupSettingsMenu(OptionPanel optionPanel) {
        JMenu settingsMenu = new JMenu("Settings");
        JMenu convMenu = new JMenu("Convolution filter options");

        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem cropItem = new JRadioButtonMenuItem("Crop the image");
        JRadioButtonMenuItem keepItem = new JRadioButtonMenuItem("Copy original pixels");
        JRadioButtonMenuItem blackItem = new JRadioButtonMenuItem("Assume outside is black");
        JRadioButtonMenuItem whiteItem = new JRadioButtonMenuItem("Assume outside is white");
        JRadioButtonMenuItem grayItem = new JRadioButtonMenuItem("Assume outside is gray");
        JRadioButtonMenuItem copyItem = new JRadioButtonMenuItem("Copy outer-most pixel");
        JRadioButtonMenuItem mirrorItem = new JRadioButtonMenuItem("Mirror pixels");

        // set default
        copyItem.setSelected(true);

        cropItem.addActionListener(e -> optionPanel.setBoundaryMode(OptionPanel.BoundaryMode.CROP));
        keepItem.addActionListener(e -> optionPanel.setBoundaryMode(OptionPanel.BoundaryMode.KEEP_ORIGINAL));
        blackItem.addActionListener(e -> optionPanel.setBoundaryMode(OptionPanel.BoundaryMode.PAD_BLACK));
        whiteItem.addActionListener(e -> optionPanel.setBoundaryMode(OptionPanel.BoundaryMode.PAD_WHITE));
        grayItem.addActionListener(e -> optionPanel.setBoundaryMode(OptionPanel.BoundaryMode.PAD_GRAY));
        copyItem.addActionListener(e -> optionPanel.setBoundaryMode(OptionPanel.BoundaryMode.REPLICATE));
        mirrorItem.addActionListener(e -> optionPanel.setBoundaryMode(OptionPanel.BoundaryMode.MIRROR));

        group.add(cropItem); group.add(keepItem); group.add(blackItem);
        group.add(whiteItem); group.add(grayItem); group.add(copyItem); group.add(mirrorItem);

        convMenu.add(cropItem); convMenu.add(keepItem);
        convMenu.addSeparator();
        convMenu.add(blackItem); convMenu.add(whiteItem); convMenu.add(grayItem);
        convMenu.addSeparator();
        convMenu.add(copyItem); convMenu.add(mirrorItem);

        settingsMenu.add(convMenu);
        return settingsMenu;
    }

}