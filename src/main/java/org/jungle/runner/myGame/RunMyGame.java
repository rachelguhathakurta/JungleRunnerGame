package org.jungle.runner.myGame;

// imports necessary libraries for Java swing
import java.awt.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunMyGame implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("MY GAME");
        frame.setLocation(300, 300);

        final JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        // Number of Uses panel
        final JPanel uses_panel = new JPanel();
        bottomPanel.add(uses_panel);
        final JLabel empty = new JLabel(" ");
        uses_panel.add(empty);

        // Status panel
        final JPanel status_panel = new JPanel();
        bottomPanel.add(status_panel);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Main playing area
        final MyGameCourt court = new MyGameCourt(status, empty);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        control_panel.add(reset);

        // save button
        final JButton save = new JButton("Save");
        save.addActionListener(e -> court.save());
        control_panel.add(save);

        // load
        final JButton load = new JButton("Load");
        load.addActionListener(e -> court.load());
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
}