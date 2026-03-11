import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PhotoPanel extends JPanel {

    private BufferedImage image;

    public PhotoPanel() {
        super();

        File imageFile = new File("src/java.jpg");
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Dimension dimension = new Dimension(image.getWidth()/10, image.getHeight()/10);
        //setSize(dimension);
        setSize(5, 5);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Image scaledImage = image.getScaledInstance(800, 400, Image.SCALE_SMOOTH);
        g2d.drawImage(scaledImage, 0, 0, this);
    }

//    @Override
//    public void paint(Graphics2D g2){
//        AffineTransform at = new AffineTransform();
//        at.translate((int)x + radius/2.5,(int)y + radius/2.5);
//        at.rotate(Math.PI/2 + angle);
//        at.translate(-iconeNave.getWidth()/2, -iconeNave.getHeight()/2);
//        g2.drawImage(iconeNave, at, null);
//    }
}
