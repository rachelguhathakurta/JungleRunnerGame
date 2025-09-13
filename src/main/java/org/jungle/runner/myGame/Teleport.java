package org.jungle.runner.myGame;

import java.awt.*;
import java.util.Random;

public class Teleport extends Attack {
    public Teleport(int uses, int keyCode) {
        super(uses, keyCode);
    }

    public void fire(Player player, Square[][] board, MyGameCourt gs) {
        if (!canBeUsed()) {
            return;
        }
        setIsExecuting(true);
        int randx = new Random().nextInt(MyGameCourt.SQUARE_SIZE);
        int randy = new Random().nextInt(MyGameCourt.SQUARE_SIZE);
        while (board[randy][randx].hasZombie() || board[randy][randx].hasPlayer()) {
            randx = new Random().nextInt(MyGameCourt.SQUARE_SIZE);
            randy = new Random().nextInt(MyGameCourt.SQUARE_SIZE);
        }
        int oldPx = player.getPx() / MyGameCourt.SPRITE_SIZE;
        int oldPy = player.getPy() / MyGameCourt.SPRITE_SIZE;
        board[oldPy][oldPx].setPlayer(null);
        player.setPx(randx * MyGameCourt.SPRITE_SIZE);
        player.setPy(randy * MyGameCourt.SPRITE_SIZE);

        board[randy][randx].setPlayer(player);
        decreaseUses();
    }

    public void draw(Graphics g) {
        return;
    }

    public String getAttackName() {
        return "Teleport";
    }

}
