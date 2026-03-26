package core;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MenuBar extends JMenuBar {
    private PhotoPanel photoPanel;
    private int[][][] lastImageMatrix;
    private int[][][] originalImageMatrix;
    private JFrame frame;
    private OptionPanel optionPanel;
    private EditMenu editMenu;
    private String currentFilename = "untitled_image";

    public MenuBar(JFrame frame, PhotoPanel photoPanel, OptionPanel optionPanel) {
        this.photoPanel = photoPanel;
        this.frame = frame;
        this.lastImageMatrix = photoPanel.getImageMatrix();
        this.originalImageMatrix = photoPanel.getImageMatrix();
        this.optionPanel = optionPanel;

        JMenu fileMenu = setupFileMenu();
        JMenu displayMenu = setupDisplayMenu();
        editMenu = new EditMenu("Edit", photoPanel, lastImageMatrix, optionPanel, originalImageMatrix);
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
        importItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

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

            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex > 0) {
                currentFilename = filename.substring(0, dotIndex) + "_edited";
            } else {
                currentFilename = filename + "_edited";
            }

            photoPanel.changeImage(filepath);
            frame.setTitle(filename);
            lastImageMatrix = photoPanel.getImageMatrix();
            originalImageMatrix = photoPanel.getImageMatrix();
            editMenu.setLastImageMatrix(lastImageMatrix);
            editMenu.setOriginalImageMatrix(originalImageMatrix);
            optionPanel.refreshOnImport();
        }
    }

    private void onSave() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save Image");

        // filetype filters
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Image (*.png)", "png");
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPEG Image (*.jpg; *.jpeg)", "jpg", "jpeg");
        FileNameExtensionFilter bmpFilter = new FileNameExtensionFilter("BMP Image (*.bmp)", "bmp");

        fc.addChoosableFileFilter(pngFilter);
        fc.addChoosableFileFilter(jpgFilter);
        fc.addChoosableFileFilter(bmpFilter);

        // png as default
        fc.setFileFilter(pngFilter);
        fc.setAcceptAllFileFilterUsed(false);

        fc.setSelectedFile(new File(currentFilename));

        int result = fc.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fc.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            javax.swing.filechooser.FileFilter selectedFilter = fc.getFileFilter();
            String format = "png";
            if (selectedFilter == jpgFilter) {
                format = "jpg";
            } else if (selectedFilter == bmpFilter) {
                format = "bmp";
            }

            // add file extension
            if (!filePath.toLowerCase().endsWith("." + format)) {
                fileToSave = new File(filePath + "." + format);
            }

            try {
                java.awt.image.BufferedImage imageToSave = photoPanel.getBufferedImage();

                if (imageToSave != null) {
                    ImageIO.write(imageToSave, format, fileToSave);
                    JOptionPane.showMessageDialog(frame,
                            "Image saved successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "There is no image to save.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Error saving image: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
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