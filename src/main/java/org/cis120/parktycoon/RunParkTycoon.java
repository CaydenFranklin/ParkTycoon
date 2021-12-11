package org.cis120.parktycoon;
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * 
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing

import org.cis120.mushroom.GameCourt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunParkTycoon implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Park Tycoon");
        frame.setLocation(300, 300);


        final JDialog dialog = new JDialog(frame);

        //Create instructions
        JLabel l = new JLabel("Welcome to Park Tycoon! \n In this game you will place tiles on the board until " +
                "you have exhausted your deck of 64 tiles. If you win, you will get the chance to expand your board." +
                "You lose when you can no longer place tiles. Each Tile has specific rules: \n" +
                "Forest: Forests can be placed anywhere, but must border 2 other adjacent Forest Tiles. \n" +
                "If a forest is placed by itself, your next two must be placed adjacent to it. \n" +
                "Path: Path Tiles (and their sub-category Shop tiles), must connect to other paths \n" +
                " and be placed adjacent to a tile already on the board. \n" +
                "Grass: Grass tiles must be placed adjacent to other tiles but have no other rules");

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        //Tile holder
        final TileHolder holder = new TileHolder();
        frame.add(holder, BorderLayout.EAST);

        // Main playing area
        final GameBoard board = new GameBoard(status, holder);
        frame.add(board, BorderLayout.CENTER);


        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        board.reset();
    }
}