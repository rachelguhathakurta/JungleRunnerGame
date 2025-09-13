package org.jungle.runner;

import org.jungle.runner.myGame.RunMyGame;

import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game. Initializes the runnable game
     * class of your choosing and runs it. IMPORTANT: Do NOT delete! You MUST
     * include a main method in your final submission.
     */
    public static void main(String[] args) {
        // Set the game you want to run here
        Runnable game = new RunMyGame();

        SwingUtilities.invokeLater(() -> instructionsPopUp());

        SwingUtilities.invokeLater(game);

    }

    private static void instructionsPopUp() {
        JFrame frame = new JFrame();
        String instructionsText = "------------------------------------\n" +
                "             Controls\n" +
                "------------------------------------\n" +
                "Use your arrow keys to move\n" +
                "\n" +
                "Press \"B\" to blast all enemies\n" +
                "in a one square radius around you\n" +
                "\n" +
                "Press \"T\" to teleport, but be wary,\n" +
                "you can teleport right next to deaths\n" +
                "door\n" +
                "------------------------------------\n" +
                "\n" +
                "Running zombies into each other will\n" +
                "annihilate them both\n" +
                "\n" +
                "Running a zombie into a puddle will\n" +
                "kill it\n" +
                "\n" +
                "Kill all the enemies to win!\n" +
                "Let them reach you and you die!\n" +
                "\n" +
                "PRESS OK TO START GAMEPLAY";
        JOptionPane.showMessageDialog(
                null,
                instructionsText,
                "Game Instructions",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
