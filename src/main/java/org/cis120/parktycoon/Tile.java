package org.cis120.parktycoon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Tile extends GameObj{
    public static enum TileType {BLANK, GRASS, PATH, FOREST, SHOP};

    public final String IMG_FILE = "files/BlankTile.png";
    public static int SIZE = 100;
    public static final int INIT_POS_X = 130;
    public static final int INIT_POS_Y = 130;
    private int col_val;
    private int row_val;

    //L Tiles occur 2 times less likely than straight pieces
    public static final int L_TILE_FREQ = 5;
    public static final int STRAIGHT_TILE_FREQ = 3;
    public static final int T_TILE_FREQ = 1;

    private TileType type;
    private BufferedImage img;

    public Tile(){
        super(INIT_POS_X, INIT_POS_Y, SIZE, SIZE);
        this.type = TileType.BLANK;

        try {
            img = ImageIO.read(new File(IMG_FILE));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public Tile(TileType type){
        super(INIT_POS_X, INIT_POS_Y, SIZE, SIZE);
        this.type = type;

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public Tile(TileType type, int px, int py){
        super(px, py, SIZE, SIZE);
        this.type = type;

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public Tile(Tile t){
        super(t.getPx(), t.getPy(), SIZE, SIZE);
        this.type = t.getType();

        try {
            if (img == null) {
                img = ImageIO.read(new File(t.IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public int getRow_val() {
        return row_val;
    }

    public void setRow_val(int row_val) {
        this.row_val = row_val;
    }

    public int getCol_val() {
        return col_val;
    }

    public void setCol_val(int col_val) {
        this.col_val = col_val;
    }

    public static Tile randTile(TileType type){
        Tile t = null;
        if (type == TileType.GRASS){
            t = new GrassTile();
        }
        else if (type == TileType.SHOP){
            Random r = new Random();
            int rotation = r.nextInt(ShopTile.IMG_FILES.length);
            t = new ShopTile(rotation);
        }
        else if (type == TileType.FOREST){
            t = new ForestTile();
        }
        else if (type == TileType.PATH){
            Random r = new Random();
            int pathTypeL = r.nextInt(L_TILE_FREQ + STRAIGHT_TILE_FREQ + T_TILE_FREQ);
            if (pathTypeL<T_TILE_FREQ){
                int rotation = r.nextInt(TPathTile.IMG_FILES.length);
                t = new TPathTile(rotation);
            }
            else if (pathTypeL<L_TILE_FREQ){
                int rotation = r.nextInt(LPathTile.IMG_FILES.length);
                t = new LPathTile(rotation);
            }
            else{
                int rotation = r.nextInt(2);
                t = new PathTile(rotation);
            }
        }
        return t;
    }

    public static LinkedList<Tile> generateTileDeck(int tilesToGenerate, HashMap<TileType, Double> percentages){
        LinkedList<Tile> deck = new LinkedList<Tile>();
        int sum = 0;

        for(TileType type : TileType.values()){
            double percent = percentages.get(type);
            int num_type = (int)(percent*tilesToGenerate);
            System.out.println("Number of tiles of " + type + " generated: " + num_type);
            sum += num_type;
            for(int i = 0; i < num_type; i++){
                deck.add(randTile(type));
            }
        }

        while(sum < tilesToGenerate){
            sum++;
            deck.add(new GrassTile());
        }

        Collections.shuffle(deck);

        return deck;
    }


    public void draw(Graphics g) {
        draw(g, this.getWidth());
    }

    public void draw(Graphics g, int size) {
        g.drawImage(img, this.getPx(), this.getPy(), size, size, null);
        g.setColor(Color.BLACK);
        String debugTag = this.col_val + "," + this.row_val;
        g.drawString(debugTag, this.getPx()+ this.SIZE/4, this.getPy()+ this.SIZE/4);
    }

    public String toString(){
        return this.type.toString() + " at (" + this.getCol_val() + "," + this.getRow_val()  +")";
    }

    public boolean isValid(Tile[] neighbors, ArrayList<HashSet<Tile>> forests, Tile targetTile, Tile tileToPlace){
        return isValid(neighbors, targetTile, tileToPlace);

    }

    public boolean isValid(Tile[] neighbors, Tile targetTile, Tile tileToPlace) {
        for (Tile t : neighbors) {
            if (t != null && t.getType() != TileType.BLANK) {
                return targetTile.getType() == TileType.BLANK
                && PathTile.pathAllowed(neighbors, tileToPlace, targetTile);
            }
        }
        return false;
    }

    public TileType getType(){
        return type;
    }

    public void setType(TileType type){
        this.type = type;
    }

    public BufferedImage getImg(){
        return this.img;
    }

    public void setImg(String fileString){
        try {
            img = ImageIO.read(new File(fileString));
        } catch (IOException e) {
            System.out.println("Internal Error: " + e.getMessage() + " " + fileString);
        }
    }

    public boolean equals(Tile t){
        if (this.getPx() == t.getPx() && this.getPy() == t.getPy()){
            return true;
        }
        return false;
    }

    public Point getCenter(){
        return new Point(this.getPx()+Tile.SIZE, this.getPy()+Tile.SIZE);
    }




}
