package core;

import optionspanels.*;

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

        // --- PIXEL OPERATIONS MENU ---
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

        // --- GRAPHIC FILTERS MENU ---
        JMenu graphicFiltersMenu = new JMenu("Graphic filters");
        JMenuItem blurringItem = new JMenuItem("Blurring");
        JMenuItem sharpeningItem = new JMenuItem("Sharpening");
        JMenuItem customMaskItem = new JMenuItem("Custom Mask");

        graphicFiltersMenu.add(blurringItem);
        graphicFiltersMenu.add(sharpeningItem);
        graphicFiltersMenu.add(customMaskItem);

        // --- INSTANT ACTIONS ---
        undoItem.addActionListener(e -> onUndo());
        grayScaleItem.addActionListener(e -> onGrayScale());
        negativeItem.addActionListener(e -> onNegative());

        // --- TOOL PANEL ROUTING ---
        // Notice how we just instantiate the specific panel and hand it to OptionsPanels.OptionPanel!
        brightnessItem.addActionListener(e -> optionPanel.loadToolPanel(new BrightnessPanel(photoPanel, optionPanel)));
        contrastItem.addActionListener(e -> optionPanel.loadToolPanel(new ContrastPanel(photoPanel, optionPanel)));
        binarizationItem.addActionListener(e -> optionPanel.loadToolPanel(new BinarizationPanel(photoPanel, optionPanel)));

        blurringItem.addActionListener(e -> optionPanel.loadToolPanel(new BlurringPanel(photoPanel, optionPanel)));
        sharpeningItem.addActionListener(e -> optionPanel.loadToolPanel(new SharpeningPanel(photoPanel, optionPanel)));
        customMaskItem.addActionListener(e -> optionPanel.loadToolPanel(new CustomMaskPanel(photoPanel, optionPanel)));

        // --- ADD MENUS TO PARENT ---
        this.add(undoItem);
        this.add(pixelOperationsMenu);
        this.add(graphicFiltersMenu);
    }

    private void onUndo() {
        int[][][] temp = photoPanel.getImageMatrix();
        photoPanel.setImageMatrix(lastImageMatrix);
        lastImageMatrix = temp;
    }

    private void onGrayScale() {
        lastImageMatrix = photoPanel.getImageMatrix();
        // Delegate the math to our new core.ImageProcessor class
        int[][][] newMatrix = ImageProcessor.applyGrayscale(lastImageMatrix);
        photoPanel.setImageMatrix(newMatrix);
    }

    private void onNegative() {
        lastImageMatrix = photoPanel.getImageMatrix();
        // Delegate the math to our new core.ImageProcessor class
        int[][][] newMatrix = ImageProcessor.applyNegative(lastImageMatrix);
        photoPanel.setImageMatrix(newMatrix);
    }

    public void setLastImageMatrix(int[][][] newMatrix) {
        this.lastImageMatrix = newMatrix;
    }
}

//import javax.swing.*;
//
//public class core.EditMenu extends JMenu {
//    private core.PhotoPanel photoPanel;
//    private int[][][] lastImageMatrix;
//    private OptionsPanels.OptionPanel optionPanel;
//
//    public core.EditMenu(String s, core.PhotoPanel photoPanel, int[][][] lastImageMatrix, OptionsPanels.OptionPanel optionPanel) {
//        super(s);
//        this.photoPanel = photoPanel;
//        this.lastImageMatrix = lastImageMatrix;
//        this.optionPanel = optionPanel;
//
//        JMenuItem undoItem = new JMenuItem("Undo");
//
//        JMenu pixelOperationsMenu = new JMenu("Pixel operations");
//        JMenuItem grayScaleItem = new JMenuItem("Convert to gray scale");
//        JMenuItem negativeItem = new JMenuItem("Create a negative");
//        JMenuItem brightnessItem = new JMenuItem("Change brightness");
//        JMenuItem contrastItem = new JMenuItem("Change contrast");
//        JMenuItem binarizationItem = new JMenuItem("Apply binarization");
//
//        pixelOperationsMenu.add(grayScaleItem);
//        pixelOperationsMenu.add(negativeItem);
//        pixelOperationsMenu.add(brightnessItem);
//        pixelOperationsMenu.add(contrastItem);
//        pixelOperationsMenu.add(binarizationItem);
//
//        JMenu graphicFiltersMenu = new JMenu("Graphic filters");
//        JMenuItem blurringItem = new JMenuItem("Blurring");
//        JMenuItem sharpeningItem = new JMenuItem("Sharpening");
//        JMenuItem customMaskItem = new JMenuItem("Custom Mask");
//
//        graphicFiltersMenu.add(blurringItem);
//        graphicFiltersMenu.add(sharpeningItem);
//        graphicFiltersMenu.add(customMaskItem);
//
//        undoItem.addActionListener(e -> onUndo());
//        grayScaleItem.addActionListener(e -> onGrayScale());
//        negativeItem.addActionListener(e -> onNegative());
//        brightnessItem.addActionListener(e -> optionPanel.onBrightness());
//        contrastItem.addActionListener(e -> optionPanel.onContrast());
//        binarizationItem.addActionListener(e -> optionPanel.onBinarization());
//        blurringItem.addActionListener(e -> optionPanel.onBlurring());
//        sharpeningItem.addActionListener(e -> optionPanel.onSharpening());
//        customMaskItem.addActionListener(e -> optionPanel.onCustomMask());
//
//        this.add(undoItem);
//        this.add(pixelOperationsMenu);
//        this.add(graphicFiltersMenu);
//    }
//
//    private void onUndo() {
//        int[][][] temp = photoPanel.getImageMatrix();
//        photoPanel.setImageMatrix(lastImageMatrix);
//        lastImageMatrix = temp;
//        if (optionPanel != null) {
//            optionPanel.refreshOnImport();
//        }
//    }
//
//    private void onGrayScale() {
//        // weighted average
//        int[][][] imageMatrix = photoPanel.getImageMatrix();
//        lastImageMatrix = imageMatrix;
//        optionPanel.applyGrayscale();
//        if (optionPanel != null) {
//            optionPanel.refreshOnImport();
//        }
//    }
//
//    private void onNegative() {
//        int[][][] imageMatrix = photoPanel.getImageMatrix();
//        int height = imageMatrix.length;
//        int width = imageMatrix[0].length;
//        lastImageMatrix = imageMatrix;
//        int[][][] newImageMatrix = new int[height][width][3];
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                newImageMatrix[y][x][0] = 255 - imageMatrix[y][x][0];
//                newImageMatrix[y][x][1] = 255 - imageMatrix[y][x][1];
//                newImageMatrix[y][x][2] = 255 - imageMatrix[y][x][2];
//            }
//        }
//
//        photoPanel.setImageMatrix(newImageMatrix);
//        if (optionPanel != null) {
//            optionPanel.refreshOnImport();
//        }
//    }
//
//    public void setLastImageMatrix(int[][][] newMatrix) {
//        this.lastImageMatrix = newMatrix;
//    }
//}
