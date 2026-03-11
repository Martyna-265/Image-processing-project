import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Image processing app");
            frame.setSize(800, 600);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setLayout(new BorderLayout());

            JToolBar toolBar = new JToolBar();
            toolBar.add(new JButton("text"));
            frame.add(toolBar, BorderLayout.PAGE_START);

            PhotoPanel photoPanel = new PhotoPanel();
            frame.add(photoPanel, BorderLayout.CENTER);
            frame.addWindowStateListener(e -> {
                photoPanel.recalculateSize();
            });

            frame.setVisible(true);
        });
    }
}