package org.jungle.runner.myGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Blast extends Attack {
    public static final String IMG_FILE = "files/electricity.png"; // change
    private static BufferedImage img;
    private long time = 0;

    public Blast(int uses, int keyCode) {
        super(uses, keyCode);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public void fire(Player player, Square[][] board, MyGameCourt gs) {
        if (!canBeUsed()) {
            return;
        }
        setIsExecuting(true);
        time = System.currentTimeMillis();
        setPx(player.getPx() - 64);
        setPy(player.getPy() - 64);

        // kill the zombies by looping through the board
        int playeri = player.getPy() / MyGameCourt.SPRITE_SIZE;
        int playerj = player.getPy() / MyGameCourt.SPRITE_SIZE;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (playeri - 1 == i || playeri + 1 == i || playeri == i
                        && playerj - 1 == j || playerj + 1 == j || playerj == j) {
                    board[i][j].setZombie(null);
                }
            }
        }
        decreaseUses();
    }

    public void draw(Graphics g) {
        if (!getIsExecuting()) {
            return;
        }
        if (System.currentTimeMillis() - time > 1000) {
            setIsExecuting(false);
            return;
        }
        g.drawImage(img, this.getPx(), this.getPy(), WIDTH, HEIGHT, null);
    }

    public String getAttackName() {
        return "Blast";
    }
}
