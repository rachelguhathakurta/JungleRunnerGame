package org.jungle.runner.myGame;

import java.awt.Graphics;

/**
 * An object in the game.
 *
 * Game objects exist in the game court. They have a position, velocity, size
 * and bounds. Their velocity controls how they move; their position should
 * always be within their bounds.
 */
public abstract class MyGameObj {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *
     * Coordinates are given by the upper-left hand corner of the object. This
     * position should always be within bounds:
     * 0 <= px <= maxX 0 <= py <= maxY
     */
    private int px;
    private int py;

    /* Size of object, in pixels. */
    private final int width = 64;
    private final int height = 64;

    /*
     * Upper bounds of the area in which the object can be positioned. Maximum
     * permissible x, y positions for the upper-left hand corner of the object.
     */
    private final int maxX;
    private final int maxY;

    /**
     * Constructor
     */
    public MyGameObj(
            int px, int py, int courtwidth,
            int courtheight
    ) {
        this.px = px;
        this.py = py;

        // take the width and height into account when setting the bounds for
        // the upper left corner of the object.
        this.maxX = courtwidth - width;
        this.maxY = courtheight - height;
    }

    // **********************************************************************************
    // * GETTERS
    // **********************************************************************************
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    // **************************************************************************
    // * SETTERS
    // **************************************************************************
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }

    // **************************************************************************
    // * UPDATES AND OTHER METHODS
    // **************************************************************************

    /**
     * Prevents the object from going outside the bounds of the area
     * designated for the object (i.e. Object cannot go outside the active
     * area the user defines for it).
     */
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    /**
     * Moves the object by its velocity. Ensures that the object does not go
     * outside its bounds by clipping.
     */
    public void moveUp() {
        this.py -= height;
        clip();

    }

    public void moveDown() {
        this.py += height;
        clip();
    }

    public void moveRight() {
        this.px += width;
        clip();
    }

    public void moveLeft() {
        this.px -= width;
        clip();
    }

    public abstract void draw(Graphics g);
}