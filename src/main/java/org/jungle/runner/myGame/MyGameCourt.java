package org.jungle.runner.myGame;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class MyGameCourt extends JPanel {

    private Square[][] board;
    private Player player;
    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."
    private final JLabel attackUses;
    private boolean eventHandeled = false;
    private boolean isKeyDown = false;
    private int currKey;
    private ArrayList<Attack> attacks;
    private Square[][] tempBoard;
    private static BufferedImage backgroundImage;

    // Game constants
    public static final int SQUARE_SIZE = 8; // how many squares are ON THE BOARD
    public static final int SPRITE_SIZE = 64;
    public static final int COURT_WIDTH = SPRITE_SIZE * SQUARE_SIZE; // an 8 by 8 grid
    public static final int COURT_HEIGHT = SPRITE_SIZE * SQUARE_SIZE;
    public static final int BLAST_USES = 10;
    public static final int TELEPORT_USES = 1000000;

    public static final String SAVED_GAME = "savedGame.txt";
    public static final String BACKGROUND = "files/background.jpg";

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

    public MyGameCourt(JLabel status, JLabel attackUses) {

        try {
            if (backgroundImage == null) {
                backgroundImage = ImageIO.read(new File(BACKGROUND));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (isKeyDown) {
                    return;
                }
                int key = e.getKeyCode();
                boolean matchesAttackKey = false;
                for (Attack a : attacks) {
                    if (a.getKeyCode() == key) {
                        matchesAttackKey = true;
                    }
                }
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT
                        || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP || matchesAttackKey) {
                    currKey = key;
                    isKeyDown = true;
                    eventHandeled = false;
                }
            }

            public void keyReleased(KeyEvent e) {
                if (isKeyDown && currKey == e.getKeyCode()) {
                    isKeyDown = false;
                    eventHandeled = false;
                    currKey = 0;
                }
            }
        });

        // Im initializing the player
        this.player = new Player(0 * SPRITE_SIZE, 0 * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT);
        this.status = status;
        this.attackUses = attackUses;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        board = initializeBoard();
        attacks = new ArrayList<Attack>();
        attacks.add(new Blast(BLAST_USES, KeyEvent.VK_B));
        attacks.add(new Teleport(TELEPORT_USES, KeyEvent.VK_T));
        player = new Player(6 * SPRITE_SIZE, 7 * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT);
        board[7][6].setPlayer(player);
        board[0][0]
                .setZombie(new Zombie(0 * SPRITE_SIZE, 0 * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT));
        board[0][1]
                .setZombie(new Zombie(1 * SPRITE_SIZE, 0 * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT));
        board[2][5]
                .setZombie(new Zombie(5 * SPRITE_SIZE, 2 * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT));
        board[5][6]
                .setZombie(new Zombie(6 * SPRITE_SIZE, 5 * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT));
        board[6][5]
                .setZombie(new Zombie(5 * SPRITE_SIZE, 6 * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT));
        playing = true;
        status.setText("Running...");
        updateAttackUses();

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        repaint();
    }

    // handels button for save
    public void save() {
        try {
            FileWriter fw = new FileWriter(SAVED_GAME);
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    boolean hz = board[i][j].hasZombie();
                    boolean hpl = board[i][j].hasPlayer();
                    boolean hpu = board[i][j].hasPuddle();

                    if (hz && !hpl && !hpu) {
                        fw.write('Z');
                    } else if (hpl && !hz && !hpu) {
                        fw.write('P');
                    } else if (hpu && !hz && !hpl) {
                        fw.write('U');
                    } else if (hz && hpu && !hpl) {
                        fw.write('Q');
                    } else if (hpl && hpu && !hz) {
                        fw.write('T');
                    } else if (hpl && hz && !hpu) {
                        fw.write('O');
                    } else if (hpu && hz && hpl) {
                        fw.write('A');
                    } else {
                        fw.write('_');
                    }
                    fw.write(' ');
                }
                fw.write('\n');
            }

            for (Attack a : attacks) {
                fw.write(a.getUses() + "\n");
            }

            fw.close();
        } catch (IOException e) {
            errorPopUp("Error Saving File... game has not been saved");
        }

        requestFocusInWindow();
        repaint();
    }

    public void load() {

        board = initializeBoard();
        try {
            player = null;
            FileReader fr = new FileReader(SAVED_GAME);
            int letter = 0;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    letter = fr.read();
                    if (letter == 'Z' || letter == 'Q' || letter == 'O' || letter == 'A') {
                        board[i][j].setZombie(
                                new Zombie(
                                        j * SPRITE_SIZE, i * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT
                                )
                        );
                    }
                    if (letter == 'P' || letter == 'T' || letter == 'O' || letter == 'A') {
                        if (player != null) {
                            throw new IllegalArgumentException("We found two players...");
                        } else {
                            player = new Player(
                                    j * SPRITE_SIZE, i * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT
                            );
                            board[i][j].setPlayer(player);
                        }
                    }

                    if (letter == 'U' || letter == 'Q' || letter == 'T' || letter == 'A') {
                        board[i][j].setPuddle(
                                new Puddle(
                                        j * SPRITE_SIZE, i * SPRITE_SIZE, COURT_WIDTH, COURT_HEIGHT
                                )
                        );
                    }

                    if (letter != 'Z' && letter != 'Q' && letter != 'O' && letter != 'A'
                            && letter != 'P' &&
                            letter != 'T' && letter != 'U' && letter != '_') {
                        throw new IllegalArgumentException("This file is invalid");
                    }

                    letter = fr.read();
                }
                letter = fr.read();

            }

            if (player == null) {
                throw new IllegalArgumentException("There is no player in this file");
            }

            attacks = new ArrayList<Attack>();
            attacks.add(new Blast(BLAST_USES, KeyEvent.VK_B));
            attacks.add(new Teleport(TELEPORT_USES, KeyEvent.VK_T));

            BufferedReader br = new BufferedReader(fr);
            for (Attack a : attacks) {
                int in = Integer.parseInt(br.readLine());
                a.setUses(in);
            }

            updateAttackUses();

            fr.close();
        } catch (IOException | IllegalArgumentException e) {
            errorPopUp("Error Loading File... please quit the game and delete savedGame.txt");
        }

        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        repaint();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {

            if (isKeyDown && !eventHandeled) { // moves players

                copyBoard();

                if (currKey == KeyEvent.VK_LEFT) {
                    player.moveLeft();
                } else if (currKey == KeyEvent.VK_RIGHT) {
                    player.moveRight();
                } else if (currKey == KeyEvent.VK_DOWN) {
                    player.moveDown();
                } else if (currKey == KeyEvent.VK_UP) {
                    player.moveUp();
                } else {
                    for (Attack a : attacks) {
                        if (a.getKeyCode() == currKey) {
                            a.fire(player, board, this);
                            updateAttackUses();
                        }
                    }
                }

                eventHandeled = true;

                int newPlayerX = getBoardLocationX(player);
                int newPlayerY = getBoardLocationY(player);
                if (board[newPlayerY][newPlayerX].hasZombie()) {
                    playing = false;
                    status.setText("You lose!");
                    repaint();
                    return;
                }

                updateBoard();

                // CHECK WIN CONDITIONS
                boolean hasWon = true;
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        if (board[i][j].hasZombie()) {
                            hasWon = false;
                        }
                    }
                }
                if (hasWon) {
                    status.setText("You win!");
                }
            }

            // update the display
            repaint();
        }
    }

    public Square[][] initializeBoard() {
        Square[][] newBoard = new Square[SQUARE_SIZE][SQUARE_SIZE];
        for (int i = 0; i < SQUARE_SIZE; i++) {
            for (int j = 0; j < SQUARE_SIZE; j++) {
                newBoard[i][j] = new Square(j, i);
            }
        }
        return newBoard;
    }

    private int getBoardLocationX(MyGameObj obj) {
        return obj.getPx() / SPRITE_SIZE;
    }

    private int getBoardLocationY(MyGameObj obj) {
        return obj.getPy() / SPRITE_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].hasPuddle()) {
                    board[i][j].getPuddle().draw(g);
                }
                if (board[i][j].hasZombie()) {
                    board[i][j].getZombie().draw(g);
                }
                if (board[i][j].hasPlayer()) {
                    board[i][j].getPlayer().draw(g);
                }
            }
        }

        for (Attack a : attacks) {
            a.draw(g);
        }

    }

    // move zombies: for each square if it contains a zombie then we move the zombie
    public void updateBoard() {

        //

        int newPlayerX = getBoardLocationX(player);
        int newPlayerY = getBoardLocationY(player);
        // puts the player on the board
        tempBoard[newPlayerY][newPlayerX].setPlayer(player);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].hasZombie()) {
                    Zombie holder = board[i][j].getZombie();
                    Direction direction = holder.zombieMoveLogic(player);
                    int newi = 0;
                    int newj = 0;
                    if (direction == Direction.RIGHT) {
                        newi = i;
                        newj = j + 1;
                    }
                    if (direction == Direction.LEFT) {
                        newi = i;
                        newj = j - 1;
                    }
                    if (direction == Direction.UP) {
                        newi = i - 1;
                        newj = j;
                    }

                    if (direction == Direction.DOWN) {
                        newi = i + 1;
                        newj = j;
                    }
                    // if where we want to move the zombie has a player then game over
                    if (tempBoard[newi][newj].hasPlayer()) {
                        playing = false;
                        status.setText("You lose!");
                        repaint();
                        return;
                    }

                    tempBoard[newi][newj].setZombie(holder);
                }
            }
        }
        board = tempBoard;
    }

    public void copyBoard() {
        tempBoard = initializeBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].hasPuddle()) {
                    tempBoard[i][j].setPuddle(board[i][j].getPuddle());
                }
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Square[][] getBoard() {
        return board;
    }

    public ArrayList<Attack> getAttacks() {
        return attacks;
    }

    public void updateAttackUses() {
        if (attacks == null) {
            attackUses.setText(" ");
            return;
        }
        String attackUsesString = "";
        for (Attack a : attacks) {
            attackUsesString += a.getAttackName() + " ";
            attackUsesString += String.valueOf(a.getUses()) + " Uses  ";
        }

        attackUses.setText(attackUsesString);
    }

    public void errorPopUp(String errorMsg) {
        JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}