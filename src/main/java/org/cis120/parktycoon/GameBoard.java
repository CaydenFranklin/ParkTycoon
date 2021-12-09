package org.cis120.parktycoon;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private TycoonManager tm; // model for the game
    private JLabel status; // current status text

    public static final int INITIAL_TILE_NUM = 9;

    private boolean showPreviewTile = false;

    private Tile previewTile;


    private int tileNum;


    public static final int BORDER_WIDTH = 100;
    // Game constants
    private static int BOARD_WIDTH = (int) Math.sqrt(INITIAL_TILE_NUM)*Tile.SIZE;
    private static int BOARD_HEIGHT = (int) Math.sqrt(INITIAL_TILE_NUM)*Tile.SIZE;
    public static final int MAX_BOARD_WIDTH = 1000;
    public static final int MAX_BOARD_HEIGHT = 1000;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit, TileHolder tileHolder) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        tm = new TycoonManager(INITIAL_TILE_NUM, tileHolder); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        tileNum = INITIAL_TILE_NUM;
        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                Point newP = TycoonManager.coordToTile(p.x, p.y);
                boolean b = tm.checkTileValidity(newP.x, newP.y, false);
                if(b){
                    updateStatus(); // updates the status JLabel
                    repaint(); // repaints the game board
                }

            }
        });


        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                // checks if a blank tile is at the current location
                showPreviewTile = tm.blankTileAtPoint(p.x, p.y);
                updatePreviewTile(p);
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() == 'x') {
                    tm.switchTiles();
                }
                super.keyReleased(e);
                updatePreviewTile(getMousePosition());
                repaint();
            }
        });

        this.setBackground(Color.white);

    }

    public int getTileNum() {
        return tileNum;
    }

    public void setTileNum(int tileNum) {
        this.tileNum = tileNum;
    }

    public void updatePreviewTile(Point p){
        previewTile = tm.getCurrentTileToPlay();
        if(showPreviewTile){
            Point newP = TycoonManager.coordToTile(p.x, p.y);
            Point xyVals = TycoonManager.tileToCoord(newP.x, newP.y);
            if(previewTile.getPx() != xyVals.x || previewTile.getPy() != xyVals.y){
                previewTile.setPx(xyVals.x);
                previewTile.setPy(xyVals.y);
            }
        }
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        tm.reset();
        status.setText("Player 1's Turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    private void makeBoardLarger(){
        int currentTileNum = this.getTileNum();
        int nextTileNum = (int) Math.sqrt(currentTileNum) + 2;
        if(Tile.SIZE * nextTileNum < MAX_BOARD_WIDTH){
            BOARD_WIDTH = Tile.SIZE * BOARD_WIDTH;
            BOARD_HEIGHT = Tile.SIZE * BOARD_HEIGHT;
        }
        else{
            Tile.SIZE = BOARD_WIDTH/nextTileNum;
        }
        tm.growBoard(nextTileNum);
        repaint();
        revalidate();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        int winner = tm.checkGameOver();
        System.out.println(winner);
        if (winner == 1) {
            makeBoardLarger();
        }
        else if (winner == -1) {
            status.setText("You have lost!");
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);

        if(showPreviewTile){
            previewTile.draw(g, Tile.SIZE);
        }
    }

    public void drawBoundingBox(int i, int j, Graphics g, int width){
        if(width == 1){
            g.drawLine(i*Tile.SIZE+1, j*Tile.SIZE+1, i*Tile.SIZE+Tile.SIZE-1, j*Tile.SIZE+1);
            g.drawLine(i*Tile.SIZE+Tile.SIZE-1, j*Tile.SIZE+1, i*Tile.SIZE+Tile.SIZE-1,
                    j*Tile.SIZE+Tile.SIZE-1);
            g.drawLine(i*Tile.SIZE+Tile.SIZE-1, j*Tile.SIZE+Tile.SIZE-1, i*Tile.SIZE+1,
                    j*Tile.SIZE+Tile.SIZE-1);
            g.drawLine(i*Tile.SIZE+1, j*Tile.SIZE+Tile.SIZE-1, i*Tile.SIZE+1,
                    j*Tile.SIZE+1);
        }
        else{
            g.fillRect(i*Tile.SIZE+width, j*Tile.SIZE+width, Tile.SIZE - 2*width, width);
            g.fillRect(i*Tile.SIZE+Tile.SIZE-2*width, j*Tile.SIZE+width, width,
                                                                Tile.SIZE - 2*width);
            g.fillRect(i*Tile.SIZE+width, j*Tile.SIZE+Tile.SIZE-2*width,
                                                        Tile.SIZE - 2*width, width);
            g.fillRect(i*Tile.SIZE+width, j*Tile.SIZE+width,
                    width, Tile.SIZE - 2*width);
        }

    }



    public void drawGrid(Graphics g){
        ArrayList<Tile> validTiles = tm.getValidTiles();
        int numTiles = tm.getTiles().length;
        for(int i = 0; i < numTiles; i++){
            for(int j=0; j < numTiles; j++) {
                Tile t = tm.getTile(i,j);
                g.setColor(Color.BLACK);
                t.draw(g);
                drawBoundingBox(i, j, g, 1);
            }
        }

        for(Tile t : tm.getValidTiles()){
            //This is a nice shade of dark green
            g.setColor(new Color(102, 204, 0));
            drawBoundingBox(t.getCol_val(), t.getRow_val(), g, 4);
        }
        g.setColor(Color.BLACK);
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        int currentSize = ((int) Math.sqrt(this.tileNum))*Tile.SIZE;
        int width = Math.min(Math.max(currentSize, BOARD_WIDTH), MAX_BOARD_WIDTH);
        int height = Math.min(Math.max(currentSize, BOARD_HEIGHT), MAX_BOARD_HEIGHT);
        return new Dimension(width + tm.returnTileHolderSize().width, height);
    }
}
