package org.cis120.parktycoon;

import java.awt.*;
import java.nio.file.Path;
import java.util.*;

public class TycoonManager {
    private static Tile [][] tiles;
    private static LinkedList<Tile> deck;

    private static HashMap<Tile.TileType, Double> typePercentage;

    public final double PATH_PERCENTAGE = .7;
    public final double GRASS_PERCENTAGE = .1;
    public final double FOREST_PERCENTAGE = .1;
    public final double SHOP_PERCENTAGE = .1;

    private ArrayList<Tile> validTiles;
    private boolean validTilesNeedsUpdate;
    private ArrayList<Node<PathTile>> paths;
    private ArrayList<Guest> guests;
    private ArrayList<HashSet<Tile>> forests;

    private TileHolder th;

    public TycoonManager (int numTiles, TileHolder th){
        typePercentage = new HashMap<Tile.TileType, Double>();
        typePercentage.put(Tile.TileType.PATH, PATH_PERCENTAGE);
        typePercentage.put(Tile.TileType.GRASS, GRASS_PERCENTAGE);
        typePercentage.put(Tile.TileType.FOREST, FOREST_PERCENTAGE);
        typePercentage.put(Tile.TileType.SHOP, SHOP_PERCENTAGE);
        typePercentage.put(Tile.TileType.BLANK, 0.0);
        validTiles = new ArrayList<Tile>();
        validTilesNeedsUpdate = true;

        int cols = (int)Math.sqrt(numTiles);

        tiles = new Tile[cols][cols];

        for (int i = 0; i < cols; i++){
            for(int j = 0; j < cols; j++){
                Tile t = new Tile(Tile.TileType.BLANK, i*Tile.SIZE, j*Tile.SIZE);
                t.setCol_val(i);
                t.setRow_val(j);
                tiles[i][j] = t;
            }
        }
        deck = Tile.generateTileDeck(GameBoard.INITIAL_TILE_NUM-1, typePercentage);
        this.th = th;
        th.addTile(deck.remove(0), deck.size());
        th.addTile(deck.remove(1), deck.size());

        int col = (int)(Math.sqrt(GameBoard.INITIAL_TILE_NUM)/2);
        placeTile(getTile(col, col), new GrassTile(), true);

        forests = new ArrayList<HashSet<Tile>>();
        paths = new ArrayList<Node<PathTile>>();
        guests = new ArrayList<Guest>();
        validTiles = checkValidTiles();
    }


    public static void reset(){

    }

    public void growBoard(int numCol){
        int newNumTiles = (int) Math.pow(numCol, 2);
        Tile [][] oldTiles = tiles.clone();
        Tile [][] newTiles = new Tile[numCol][numCol];

        for (int i = 0; i < numCol-2; i++){
            for(int j = 0; j < numCol-2; j++){
                newTiles[i+1][j+1] = oldTiles[i][j];
                newTiles[i+1][j+1].setCol_val(i+1);
                newTiles[i+1][j+1].setRow_val(j+1);
                newTiles[i+1][j+1].setPx((i+1)*Tile.SIZE);
                newTiles[i+1][j+1].setPy((j+1)*Tile.SIZE);
            }
        }

        tiles = newTiles;
        for(int i = 0; i<numCol; i++){
            addTileToBoard(i,0);
            addTileToBoard(0,i);
            addTileToBoard(i,numCol-1);
            addTileToBoard(numCol-1,i);
        }
        int difference = newNumTiles - (int)Math.pow(oldTiles.length,2);
        deck = Tile.generateTileDeck(difference, typePercentage);
        th.addTile(deck.getFirst(), deck.size());
        th.addTile(deck.get(1), deck.size());
    }

    public void addTileToBoard(int col, int row){
        Tile t = new Tile(Tile.TileType.BLANK, col*Tile.SIZE, row*Tile.SIZE);
        t.setCol_val(col);
        t.setRow_val(row);
        tiles[col][row] = t;
    }

    public void switchTiles(){
        if(th.getTileToPlay() == 0){
            th.setTileToPlay(1);
        }
        else{
            th.setTileToPlay(0);
        }
        checkValidTiles();
    }


    public boolean blankTileAtPoint(int pX, int pY) {
        Point p = coordToTile(pX, pY);
        //Avoids issues with mouse pointer being on game board border
        //Clamps tile values between 0 and sqrt(TILE_NUM)-1
        int safeX = (int) Math.min(Math.max(0,p.x), Math.sqrt(GameBoard.getTileNum())-1);
        int safeY = (int) Math.min(Math.max(0,p.y), Math.sqrt(GameBoard.getTileNum())-1);
        Tile targetTile = getTiles()[safeX][safeY];
        return (targetTile.getType() == Tile.TileType.BLANK);
    }

    public static Point coordToTile(int pX, int pY){
        double columns = Math.sqrt(GameBoard.getTileNum());
        int safeX = (int) Math.min(Math.max(0,pX), Tile.SIZE * columns);
        int safeY = (int) Math.min(Math.max(0,pY), Tile.SIZE * columns);
        safeX = (int) Math.min(Math.floor(safeX/Tile.SIZE), columns-1);
        safeY = (int) Math.min(Math.floor(safeY/Tile.SIZE), columns-1);
        return new Point(safeX, safeY);
    }

    public static Point tileToCoord(int row, int col){
        int xVal = row*Tile.SIZE;
        int yVal = col*Tile.SIZE;
        return new Point(xVal, yVal);
    }

    public boolean checkTileValidity(int col, int row, boolean check) {
        Tile targetTile = getTiles()[col][row];
        Tile tileToPlace = th.getCurrentTile();
        if (targetTile.getType() == Tile.TileType.BLANK) {
            if (tileToPlace.getType() == Tile.TileType.FOREST) {
                if (tileToPlace.isValid(getNeighbors(col, row), forests, targetTile, tileToPlace)) {
                    if(!check) {
                        placeTile(targetTile, false);
                        checkForestState(tileToPlace);
                    }
                    return true;
                }
                return false;
            }

            else {
                if (tileToPlace.isValid(getNeighbors(col, row), targetTile, tileToPlace)) {
                    if(!check){
                        placeTile(targetTile, false);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper function to check the current state of forests on the board
     * and remove finished forests, as well as update forests that have not met
     * minimum neighbor specification.
     */
    public void checkForestState(Tile tileToPlace){
        int col = tileToPlace.getCol_val();
        int row = tileToPlace.getRow_val();
        for(HashSet<Tile> forest : forests) {
            for (Tile t : forest) {
                Tile [] neighbors = getNeighbors(t.getCol_val(), t.getRow_val());
                if(Arrays.asList(neighbors).contains(tileToPlace)) {
                    forest.add(tileToPlace);
                    return;
                }
            }
        }
        HashSet<Tile> hs = new HashSet<Tile>();
        hs.add(tileToPlace);
        forests.add(hs);
        }

    public int countAllNeighbors(int colVal, int rowVal, Tile test) {
        Tile []  neighbors = getNeighbors(colVal, rowVal);
        int count = 0;
        for (int k = 0; k < neighbors.length; k++){
            if(neighbors[k] != null){
                if (neighbors[k].getType() == test.getType() && !neighbors[k].equals(test))
                    count++;
                    count+= countAllNeighbors(neighbors[k].getCol_val(), neighbors[k].getRow_val(), neighbors[k]);
            }
        }
        return count;
    }

    public void placeTile(Tile targetTile, boolean first){
        Tile t = th.getCurrentTile();
        placeTile(targetTile, t, first);
    }

    public void updatePaths(PathTile p){
        Tile [] neighbors = (getNeighbors(p.getCol_val(), p.getRow_val()));
        if(PathTile.touchingEdge(neighbors, p) && paths.size() == 0) {
            Node<PathTile> newTree = calculatePaths(new Node<PathTile>(p), p);
            paths.add(newTree);
            if(newTree.size() > 1){
                guests.add(new Guest(newTree));
            }
        }

        else if(paths.size() != 0){
            Node<PathTile> newTree = calculatePaths(new Node<PathTile>(p), p);
            boolean add = true;
            for(Node<PathTile> n : paths) {
                if (n.overlap(newTree)) {
                    if (n.getTileSet().size() <
                            newTree.getTileSet().size()) {
                        add = false;
                        paths.set(paths.indexOf(n), newTree);
                    }
                }
            }
            if(add) {
                paths.add(newTree);
                if(newTree.size() > 1){
                    guests.add(new Guest(newTree));
                }
            }
        }
    }


    public void placeTile(Tile targetTile, Tile tileToPlace, boolean first){
        if(!first){
            th.removeTile();
            if(deck.size() != 0){
                th.addTile(deck.removeFirst(), deck.size());
            }
        }
        tileToPlace.setPx(targetTile.getPx());
        tileToPlace.setPy(targetTile.getPy());
        tileToPlace.setRow_val(targetTile.getRow_val());
        tileToPlace.setCol_val(targetTile.getCol_val());
        tiles[targetTile.getCol_val()][targetTile.getRow_val()] = tileToPlace;
        if(tileToPlace.getType() == Tile.TileType.PATH){
            updatePaths((PathTile) tileToPlace);
            for(Node<PathTile> n : paths){
                System.out.println("Set of Paths: " +  n.toString());
            }
        }
        if(!first){
            checkValidTiles();
        }

    }

    public Node<PathTile> calculatePaths(Node<PathTile> tree, PathTile p){
        Tile[] neighbors =  getNeighbors(p.getCol_val(), p.getRow_val());
        for(PathTile.ConnectionPoints cp : PathTile.ConnectionPoints.values()){
            int neighborDirection = Arrays.asList(PathTile.ConnectionPoints.values()).indexOf(cp);
            if(neighbors[neighborDirection] != null &&
                    neighbors[neighborDirection].getType() == Tile.TileType.PATH){
                PathTile neighborToCheck = (PathTile) neighbors[neighborDirection];
                    if(PathTile.pathConnected(neighborDirection, neighborToCheck, p)
                        && !tree.contains(neighborToCheck)){
                        Node<PathTile> branch = new Node<PathTile>(neighborToCheck);
                        tree.addNode(cp, branch);
                        tree.addNode(cp, calculatePaths(branch, neighborToCheck));
                    }
            }
        }
        return tree;
    }

    public ArrayList<Tile> checkValidTiles(){
        ArrayList<Tile> list = new ArrayList<Tile>();
        int numTiles = tiles.length;
        for(int i = 0; i < numTiles; i++) {
            for (int j = 0; j < numTiles; j++) {
                boolean checkCell = checkTileValidity(i, j, true);
                if (checkCell)
                    list.add(getTile(i, j));
            }
        }
        validTilesNeedsUpdate = !validTiles.equals(list);
        if(validTilesNeedsUpdate){
            validTiles = list;
        }
        return list;
    }

    public boolean getValidTilesNeedsUpdate(){
        return validTilesNeedsUpdate;
    }

    public void setValidTilesNeedsUpdate(boolean needsUpdate) {
        validTilesNeedsUpdate = needsUpdate;
    }



    public static Tile[] getNeighbors(int col, int row){
        Tile [] neighbors = new Tile[]{null, null, null, null};
        if(row > 0)
            neighbors[0] = tiles[col][row-1];
        if(col < tiles.length-1)
            neighbors[1] = tiles[col+1][row];
        if(row < tiles.length-1)
            neighbors[2] = tiles[col][row+1];
        if(col > 0)
            neighbors[3] = tiles[col-1][row];
        return neighbors;
    }

    public ArrayList<Node<PathTile>> getPaths(){
        return this.paths;
    }

    public Tile[][] getTiles(){
        return tiles.clone();
    }

    public Tile getTile(int col, int row){
        return tiles[col][row];
    }


    public Tile getCurrentTileToPlay(){
        return th.getCurrentTile();
    }

    public ArrayList<Tile> getValidTiles(){
        if(validTilesNeedsUpdate){
            validTiles = checkValidTiles();
        }
        return (ArrayList<Tile>) validTiles.clone();
    }

    public Dimension returnTileHolderSize(){
        return th.getPreferredSize();
    }

    public ArrayList<Guest> getGuests(){
        return this.guests;
    }

    public void tick(){
        for(Guest g: guests) {
            g.updatePosition();
        }
    }

    /**
     * check if either the player has won by exhausting their deck
     * or their are not valid remaining positions
     * @return an int representing if the game is won (1), lost (-1), or continues(0)
     */
    public int checkGameOver(){
        if(deck.size() == 0){
            if(th.isEmpty()){
                return 1;
            }
            return 0;
        }
        boolean validPositionsRemaining = false;

        Tile tileToPlace = deck.getFirst();

        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j< tiles.length; j++){
                if(!tiles[i][j].getType().equals(Tile.TileType.BLANK)){
                    continue;
                }
                if(checkTileValidity(i, j, true)){
                   validPositionsRemaining = true;
                }
            }
        }
        if (!validPositionsRemaining){
            return -1;
        }
        return 0;
    }


}
