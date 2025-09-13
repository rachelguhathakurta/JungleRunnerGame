package org.jungle.runner.myGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Puddle extends MyGameObj {
    public static final String IMG_FILE = "files/Puddle.png"; // change
    private static BufferedImage img;

    public Puddle(int px, int py, int courtwidth, int courtheight) {
        super(px, py, courtwidth, courtheight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

}