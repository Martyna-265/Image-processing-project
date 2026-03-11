import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Image processing app");
            frame.setSize(800, 600);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setLayout(new BorderLayout());

            PhotoPanel photoPanel = new PhotoPanel();


            MenuBar menuBar = new MenuBar(photoPanel);
            frame.setJMenuBar(menuBar);

            frame.add(photoPanel, BorderLayout.CENTER);
            frame.addWindowStateListener(e -> {
                photoPanel.recalculateSize();
            });

            frame.setVisible(true);
        });
    }
}