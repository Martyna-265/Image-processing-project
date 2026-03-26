package core;

import optionspanels.HistogramPanel.HistogramMode;
import optionspanels.HistogramPanel;

import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {

    public enum BoundaryMode {
        CROP, KEEP_ORIGINAL, PAD_BLACK, PAD_WHITE, PAD_GRAY, REPLICATE, MIRROR
    }

    private PhotoPanel photoPanel;
    private EditMenu editMenu;
    private BoundaryMode currentBoundaryMode = BoundaryMode.REPLICATE;
    private JPanel toolArea;
    private HistogramPanel histogramPanel;

    public OptionPanel(PhotoPanel photoPanel) {
        this.photoPanel = photoPanel;

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 0));

        toolArea = new JPanel(new BorderLayout());
        this.add(toolArea, BorderLayout.NORTH);

        histogramPanel = new HistogramPanel(photoPanel, this);
        this.add(histogramPanel, BorderLayout.CENTER);
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

    public void saveUndoState(int[][][] matrixToSave) {
        if (editMenu != null && matrixToSave != null) {
            editMenu.setLastImageMatrix(matrixToSave);
        }
    }

    public void refreshOnImport() {
        toolArea.removeAll();
        if (histogramPanel != null) {
            histogramPanel.refreshHistograms();
        }
        this.revalidate();
        this.repaint();
    }

    public void loadToolPanel(JPanel toolPanel) {
        toolArea.removeAll();

        // always reset width to 400
        //this.setPreferredSize(new Dimension(400, 0));

        toolArea.add(toolPanel, BorderLayout.CENTER);

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.revalidate();
        }

        toolArea.revalidate();
        toolArea.repaint();
    }

    public void updateHistogram() {
        if (histogramPanel != null) {
            histogramPanel.refreshHistograms();
        }
    }

    public void setHistogramMode(HistogramMode mode) {
        if (histogramPanel != null) {
            histogramPanel.setMode(mode);
        }
        updateHistogram();
    }
}