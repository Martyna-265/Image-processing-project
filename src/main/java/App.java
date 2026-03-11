import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class App {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Default Image");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            PhotoPanel photoPanel = new PhotoPanel();


            MenuBar menuBar = new MenuBar(frame, photoPanel);
            frame.setJMenuBar(menuBar);

            frame.add(photoPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);

            frame.addWindowStateListener(e -> {
                photoPanel.recalculateSize();
            });

            frame.setVisible(true);

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setResizable(false);

        });


    }
}