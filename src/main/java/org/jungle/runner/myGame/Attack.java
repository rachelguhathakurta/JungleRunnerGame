package org.jungle.runner.myGame;

import java.awt.*;

public abstract class Attack {

    /* Size of object, in pixels. */
    public static final int WIDTH = MyGameCourt.SPRITE_SIZE * 3;
    public static final int HEIGHT = MyGameCourt.SPRITE_SIZE * 3;
    private int px;
    private int py;

    private int uses;
    private int keyCode;
    private boolean isExecuting = false;

    public Attack(int uses, int keyCode) {
        this.uses = uses;
        this.keyCode = keyCode;
    }

    public abstract void fire(Player player, Square[][] board, MyGameCourt gs);

    public int getPx() {
        return px;
    }

    public int getPy() {
        return py;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public int getUses() {
        return uses;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean getIsExecuting() {
        return isExecuting;
    }

    public void setIsExecuting(boolean isExecuting) {
        this.isExecuting = isExecuting;
    }

    public void decreaseUses() {
        uses--;
    }

    public boolean canBeUsed() {
        return uses > 0;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public abstract void draw(Graphics g);

    public abstract String getAttackName();
}
