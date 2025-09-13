package org.jungle.runner.myGame;

// put collision logic in here
public class Square {
    private Zombie zombie;
    private Player player;
    private Puddle puddle;
    private int x;
    private int y;

    public Square(int x, int y) {
        zombie = null;
        player = null;
        puddle = null;
        this.x = x;
        this.y = y;
    }

    // boolean has
    public boolean hasZombie() {
        return zombie != null;
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public boolean hasPuddle() {
        return puddle != null;
    }

    // setters
    public void setZombie(Zombie zombie) {
        if (hasPuddle()) {
            return;
        }
        if (hasZombie()) {
            if (this.zombie != zombie) {
                this.zombie = null;
                puddle = new Puddle(
                        x * MyGameCourt.SPRITE_SIZE, y * MyGameCourt.SPRITE_SIZE,
                        MyGameCourt.COURT_WIDTH, MyGameCourt.COURT_HEIGHT
                );
            }

        } else {
            this.zombie = zombie;
            this.player = null;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;

    }

    public void setPuddle(Puddle puddle) {
        this.puddle = puddle;
    }

    // getters
    public Zombie getZombie() {
        return zombie;
    }

    public Puddle getPuddle() {
        return puddle;
    }

    public Player getPlayer() {
        return player;
    }
}
