package org.jungle.runner.myGame;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JUnitTest {
    @Test
    public void testGameCourtCreation() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        assertNotEquals(null, gs);
    }

    @Test
    public void testMoveLeft() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        gs.reset();
        gs.copyBoard();
        Player player = gs.getPlayer();
        int playerx = player.getPx();
        int playery = player.getPy();
        player.moveLeft();
        gs.updateBoard();
        int newPlayerx = player.getPx();
        int newPlayery = player.getPy();
        assertEquals(playerx - MyGameCourt.SPRITE_SIZE, newPlayerx);
        assertEquals(playery, newPlayery);
    }

    @Test
    public void testMoveLeftThenRight() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        gs.reset();
        gs.copyBoard();
        Player player = gs.getPlayer();
        int playerx = player.getPx();
        int playery = player.getPy();
        player.moveLeft();
        gs.updateBoard();
        int newPlayerx = player.getPx();
        int newPlayery = player.getPy();
        assertEquals(playerx - MyGameCourt.SPRITE_SIZE, newPlayerx);
        assertEquals(playery, newPlayery);
        gs.copyBoard();
        playerx = player.getPx();
        playery = player.getPy();
        player.moveRight();
        gs.updateBoard();
        newPlayerx = player.getPx();
        newPlayery = player.getPy();
        assertEquals(playerx + MyGameCourt.SPRITE_SIZE, newPlayerx);
        assertEquals(playery, newPlayery);
    }

    @Test
    // We are at the edge of the board so cannot move right
    public void testMoveRight() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        gs.reset();
        gs.copyBoard();
        Player player = gs.getPlayer();
        player.moveRight();
        gs.updateBoard();
        gs.copyBoard();
        int playerx = player.getPx();
        int playery = player.getPy();
        player.moveRight();
        gs.updateBoard();
        int newPlayerx = player.getPx();
        int newPlayery = player.getPy();
        assertEquals(playerx, newPlayerx);
        assertEquals(playery, newPlayery);
    }

    @Test
    public void testMoveUp() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        gs.reset();
        gs.copyBoard();
        Player player = gs.getPlayer();
        player.moveRight();
        gs.updateBoard();
        gs.copyBoard();
        int playerx = player.getPx();
        int playery = player.getPy();
        player.moveUp();
        gs.updateBoard();
        int newPlayerx = player.getPx();
        int newPlayery = player.getPy();
        assertEquals(playerx, newPlayerx);
        assertEquals(playery - MyGameCourt.SPRITE_SIZE, newPlayery);
    }

    @Test
    // We are at the bottom of the screen so we can't move down
    public void testMoveDown() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        gs.reset();
        gs.copyBoard();
        Player player = gs.getPlayer();
        int playerx = player.getPx();
        int playery = player.getPy();
        player.moveDown();
        gs.updateBoard();
        int newPlayerx = player.getPx();
        int newPlayery = player.getPy();
        assertEquals(playerx, newPlayerx);
        assertEquals(playery, newPlayery);
    }

    @Test
    public void testMoveUpThenDown() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        gs.reset();
        gs.copyBoard();
        Player player = gs.getPlayer();
        int playerx = player.getPx();
        int playery = player.getPy();
        player.moveUp();
        gs.updateBoard();
        int newPlayerx = player.getPx();
        int newPlayery = player.getPy();
        assertEquals(playerx, newPlayerx);
        assertEquals(playery - MyGameCourt.SPRITE_SIZE, newPlayery);
        gs.copyBoard();
        playerx = player.getPx();
        playery = player.getPy();
        player.moveDown();
        gs.updateBoard();
        newPlayerx = player.getPx();
        newPlayery = player.getPy();
        assertEquals(playerx, newPlayerx);
        assertEquals(playery + MyGameCourt.SPRITE_SIZE, newPlayery);
    }

    @Test
    public void testZombieDieAndPuddle() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        gs.reset();
        gs.copyBoard();
        Player player = gs.getPlayer();
        player.moveRight();
        gs.updateBoard();
        Square[][] board = gs.getBoard();
        assertEquals(board[6][6].hasPuddle(), true);
        assertEquals(board[6][6].hasZombie(), false);
    }

    @Test
    public void testSaveandLoad() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        gs.reset();
        gs.copyBoard();
        Player player = gs.getPlayer();
        player.moveRight();
        gs.updateBoard();
        gs.copyBoard();
        player.moveUp();
        gs.updateBoard();
        gs.copyBoard();
        player.moveUp();
        gs.updateBoard();
        Square[][] board = gs.getBoard();
        gs.save();
        gs.reset();
        gs.load();
        Square[][] anotherBoard = gs.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                assertEquals(board[i][j].hasPlayer(), anotherBoard[i][j].hasPlayer());
                assertEquals(board[i][j].hasPuddle(), anotherBoard[i][j].hasPuddle());
                assertEquals(board[i][j].hasZombie(), anotherBoard[i][j].hasZombie());
            }
        }
    }

    @Test
    public void testBlast() {
        JLabel status = new JLabel("Running...");
        JLabel uses = new JLabel(" ");
        MyGameCourt gs = new MyGameCourt(status, uses);
        gs.reset();
        gs.copyBoard();
        Player player = gs.getPlayer();
        Square[][] board = gs.getBoard();
        ArrayList<Attack> attacks = gs.getAttacks();
        attacks.get(0).fire(player, board, gs);
        gs.updateBoard();
        assertFalse(board[6][5].hasZombie());

    }
}