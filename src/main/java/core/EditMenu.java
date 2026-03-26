package core;

import optionspanels.*;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class EditMenu extends JMenu {
    private PhotoPanel photoPanel;
    private int[][][] originalImageMatrix;
    private int[][][] lastImageMatrix;
    private OptionPanel optionPanel;

    public EditMenu(String s, PhotoPanel photoPanel, int[][][] lastImageMatrix, OptionPanel optionPanel, int[][][] originalImageMatrix) {
        super(s);
        this.photoPanel = photoPanel;
        this.lastImageMatrix = lastImageMatrix;
        this.optionPanel = optionPanel;
        this.originalImageMatrix = originalImageMatrix;

        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        JMenuItem revertItem = new JMenuItem("Revert to original state");

        // pixel operations menu
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

        // graphic filters menu
        JMenu graphicFiltersMenu = new JMenu("Graphic filters");
        JMenuItem blurringItem = new JMenuItem("Blurring");
        JMenuItem sharpeningItem = new JMenuItem("Sharpening");
        JMenuItem edgeDetectionItem = new JMenuItem("Edge Detection");
        JMenuItem customMaskItem = new JMenuItem("Custom Mask");

        graphicFiltersMenu.add(blurringItem);
        graphicFiltersMenu.add(sharpeningItem);
        graphicFiltersMenu.add(edgeDetectionItem);
        graphicFiltersMenu.add(customMaskItem);

        // instant actions
        undoItem.addActionListener(e -> onUndo());
        revertItem.addActionListener(e -> onRevert());
        grayScaleItem.addActionListener(e -> onGrayScale());
        negativeItem.addActionListener(e -> onNegative());

        brightnessItem.addActionListener(e -> optionPanel.loadToolPanel(new BrightnessPanel(photoPanel, optionPanel)));
        contrastItem.addActionListener(e -> optionPanel.loadToolPanel(new ContrastPanel(photoPanel, optionPanel)));
        binarizationItem.addActionListener(e -> optionPanel.loadToolPanel(new BinarizationPanel(photoPanel, optionPanel)));
        blurringItem.addActionListener(e -> optionPanel.loadToolPanel(new BlurringPanel(photoPanel, optionPanel)));
        sharpeningItem.addActionListener(e -> optionPanel.loadToolPanel(new SharpeningPanel(photoPanel, optionPanel)));
        edgeDetectionItem.addActionListener(e -> optionPanel.loadToolPanel(new EdgeDetectionPanel(photoPanel, optionPanel)));
        customMaskItem.addActionListener(e -> optionPanel.loadToolPanel(new CustomMaskPanel(photoPanel, optionPanel)));

        this.add(undoItem);
        this.add(revertItem);
        this.add(pixelOperationsMenu);
        this.add(graphicFiltersMenu);
    }

    private void onUndo() {
        int[][][] temp = photoPanel.getImageMatrix();
        photoPanel.setImageMatrix(lastImageMatrix);
        lastImageMatrix = temp;
        optionPanel.updateHistogram();
    }

    private void onRevert() {
        int[][][] temp = photoPanel.getImageMatrix();
        photoPanel.setImageMatrix(originalImageMatrix);
        lastImageMatrix = temp;
        optionPanel.updateHistogram();
    }

    private void onGrayScale() {
        lastImageMatrix = photoPanel.getImageMatrix();
        int[][][] newMatrix = ImageProcessor.applyGrayscale(lastImageMatrix);
        photoPanel.setImageMatrix(newMatrix);
        optionPanel.updateHistogram();
    }

    private void onNegative() {
        lastImageMatrix = photoPanel.getImageMatrix();
        int[][][] newMatrix = ImageProcessor.applyNegative(lastImageMatrix);
        photoPanel.setImageMatrix(newMatrix);
        optionPanel.updateHistogram();
    }

    public void setLastImageMatrix(int[][][] newMatrix) {
        this.lastImageMatrix = newMatrix;
    }

    public void setOriginalImageMatrix(int[][][] newMatrix) {
        this.originalImageMatrix = newMatrix;
    }
}
