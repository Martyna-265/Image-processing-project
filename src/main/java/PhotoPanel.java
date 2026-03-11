import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PhotoPanel extends JPanel {

    private BufferedImage image;
    private Image scaledImage;
    private Dimension maxDimension;

    public PhotoPanel() {
        super();

        File imageFile = new File("src/testImage.jpg");
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                maxDimension = new Dimension(getWidth() - 500, getHeight() - 50);
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (maxDimension == null || image == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        scaleImage();
        g2d.drawImage(scaledImage, 0, 0, this);
    }

    public void recalculateSize() {
        maxDimension = new Dimension(getWidth() - 200, getHeight() - 50);
        revalidate();
        repaint();
    }


    private void scaleImage() {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double imageScale = (double) imageWidth / imageHeight;

        int newImageWidth = maxDimension.width;
        int newImageHeight = (int) Math.round(newImageWidth / imageScale);

        if (newImageHeight > maxDimension.height) {
            newImageHeight = maxDimension.height;
            newImageWidth = (int) Math.round(newImageHeight * imageScale);
        }

        scaledImage = image.getScaledInstance(newImageWidth, newImageHeight, Image.SCALE_SMOOTH);
    }

    public void changeImage(String filepath){
        File imageFile = new File(filepath);
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }
}
