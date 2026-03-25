package core;

import optionspanels.*;

import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {

    public enum BoundaryMode {
        CROP, KEEP_ORIGINAL, PAD_BLACK, PAD_WHITE, PAD_GRAY, REPLICATE, MIRROR
    }

    private PhotoPanel photoPanel;
    private EditMenu editMenu;
    private BoundaryMode currentBoundaryMode = BoundaryMode.REPLICATE;

    public OptionPanel(PhotoPanel photoPanel) {
        this.photoPanel = photoPanel;

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 0));
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
        this.removeAll();
        this.setPreferredSize(new Dimension(400, 0));
        this.revalidate();
        this.repaint();
    }

    public void loadToolPanel(JPanel toolPanel) {
        this.removeAll();

        // Always reset width to 400 in case UIPanels.CustomMaskPanel previously expanded it
        this.setPreferredSize(new Dimension(400, 0));

        // Add the new tool panel to the center
        this.add(toolPanel, BorderLayout.CENTER);

        // Force the app window to visually acknowledge the size reset
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.revalidate();
        }

        this.revalidate();
        this.repaint();
    }
}