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
    private int[][][] imageMatrix;

    public PhotoPanel() {
        super();

        File imageFile = new File("src/testImage.jpg");
        try {
            image = ImageIO.read(imageFile);
            createImageMatrix();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                maxDimension = new Dimension(getWidth(), getHeight());
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
            createImageMatrix();
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }

    private void createImageMatrix() {
        int width = image.getWidth();
        int height = image.getHeight();

        imageMatrix = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int rgb = image.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                imageMatrix[y][x][0] = r;
                imageMatrix[y][x][1] = g;
                imageMatrix[y][x][2] = b;
            }
        }
    }

    public int[][][] getImageMatrix() {
        return imageMatrix;
    }

    public void setImageMatrix(int[][][] imageMatrix) {
        this.imageMatrix = imageMatrix;
        int height = imageMatrix.length;
        int width = imageMatrix[0].length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = imageMatrix[y][x][0];
                int g = imageMatrix[y][x][1];
                int b = imageMatrix[y][x][2];

                int rgb = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgb);
            }
        }

        repaint();
    }

}
