package org.jungle.runner.myGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Zombie extends MyGameObj {
    public static final String IMG_FILE = "files/fox.png"; // change
    private static BufferedImage img;

    public Zombie(int px, int py, int courtwidth, int courtheight) {
        super(px, py, courtwidth, courtheight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public Direction zombieMoveLogic(Player player) {
        int playerPx = player.getPx();
        int playerPy = player.getPy();

        int distancePx = playerPx - this.getPx();
        int distancePy = playerPy - this.getPy();

        if (Math.abs(distancePx) >= Math.abs(distancePy)) {
            if (distancePx > 0) {
                moveRight();
                return Direction.RIGHT;
            }
            if (distancePx < 0) {
                moveLeft();
                return Direction.LEFT;
            }
        }

        if (Math.abs(distancePx) < Math.abs(distancePy)) {
            if (distancePy > 0) {
                moveDown();
                return Direction.DOWN;
            }
            if (distancePy < 0) {
                moveUp();
                return Direction.UP;
            }
        }

        // doesn't do anything just makes compiler happy
        return Direction.DOWN;

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

}
