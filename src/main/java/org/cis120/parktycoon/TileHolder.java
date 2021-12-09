package org.cis120.parktycoon;

import javax.swing.*;
import java.awt.*;

public class TileHolder extends JPanel {
    public static final int MAX_TILES = 2;
    private int tileToPlay;
    private Tile [] currentTiles;
    private JLabel lab1;
    public static final int DISPLAY_SIZE = 100;


    public TileHolder(){
        currentTiles = new Tile [MAX_TILES];
        for(int i = 0;  i < MAX_TILES; i++){
            currentTiles[i] = null;
        }
        tileToPlay = 0;
        setBorder(BorderFactory.createLineBorder(Color.RED));
        this.setBackground(Color.WHITE);

        String tileString = "Tiles Remaining: " + GameBoard.INITIAL_TILE_NUM;
        lab1 = new JLabel(tileString, JLabel.CENTER);
        this.setLayout(new FlowLayout());
        this.add(lab1);

    }

    public void addTile(Tile t, int tilesRemaining){
        if(currentTiles[0] == null){
            currentTiles[0] = currentTiles[1];
            currentTiles[1] = t;
        }
        else if(currentTiles[1] == null){
            currentTiles[1] = t;
        }
        else{
            throw new IllegalArgumentException("setTile called with two tiles remaining");
        }
        String tileString = "Tiles Remaining: " + tilesRemaining;
        lab1.setText(tileString);
        this.repaint();
    }

    public void setTile(Tile t, int index, int tilesRemaining){
        currentTiles[index] = t;
        String tileString = "Tiles Remaining: " + tilesRemaining;
        lab1.setText(tileString);
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(currentTiles[tileToPlay] != null){
            currentTiles[tileToPlay].setPx(this.getWidth()/2 - DISPLAY_SIZE/2);
            currentTiles[tileToPlay].setPy(this.getHeight()/2 - DISPLAY_SIZE);
            currentTiles[tileToPlay].draw(g, DISPLAY_SIZE);
        }
        int otherTile = getTileToDraw();
        if(currentTiles[otherTile] != null){
            currentTiles[otherTile].setPx(this.getWidth()/2 - DISPLAY_SIZE/2);
            int yBound = Math.min(this.getHeight()/2 - DISPLAY_SIZE + 2*DISPLAY_SIZE, this.getHeight()-DISPLAY_SIZE);
            currentTiles[otherTile].setPy(yBound);
            currentTiles[otherTile].draw(g, DISPLAY_SIZE);
        }
    }

    public void removeTile(){
        currentTiles[tileToPlay] = null;
        if(currentTiles[getTileToDraw()] != null){
            tileToPlay = getTileToDraw();
        }
    }

    public void setTileToPlay(int newTile){
        this.tileToPlay = newTile;
        repaint();
    }

    public Tile getCurrentTile(){
        Tile t = currentTiles[tileToPlay];
        return t;
    }

    public Tile removeCurrentTile(){
        Tile t = currentTiles[tileToPlay];
        currentTiles[tileToPlay] = null;
        return t;
    }

    public int getTileToPlay(){
        return this.tileToPlay;
    }


    public int getTileToDraw(){
        if(this.tileToPlay == 0)
            return 1;
        return 0;
    }

    public boolean isEmpty(){
        for (Tile currentTile : currentTiles) {
            if (currentTile != null)
                return false;
        }
        return true;
    }

    /**
     * Returns the size of the game TileHolder.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Math.max(lab1.getWidth() + 10, DISPLAY_SIZE), DISPLAY_SIZE);
    }


}
