package core;

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

        // always reset width to 400
        this.setPreferredSize(new Dimension(400, 0));

        this.add(toolPanel, BorderLayout.CENTER);

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.revalidate();
        }

        this.revalidate();
        this.repaint();
    }
}