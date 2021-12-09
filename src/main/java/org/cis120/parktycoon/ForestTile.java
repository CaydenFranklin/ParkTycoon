package org.cis120.parktycoon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ForestTile extends Tile{
    public final String IMG_FILE = "files/forestTile.png";
    public static final int REQ_NEIGHBORS = 3;

    public ForestTile(){
        super(TileType.FOREST);
        this.setImg(IMG_FILE);
    }


    public ForestTile(int px, int py){
        super(TileType.FOREST, px, py);
        this.setImg(IMG_FILE);
    }

    public boolean isValid(Tile[] neighbors, ArrayList<HashSet<Tile>> forests, Tile targetTile, Tile tileToPlace){
        for(HashSet<Tile> forest : forests){
            if(forest.size() > 0 && forest.size() < REQ_NEIGHBORS) {
                HashSet<Tile> forestNeighbors = new HashSet<Tile>();
                for(Tile t : forest) {
                    Tile [] arr = TycoonManager.getNeighbors(t.getCol_val(), t.getRow_val());
                    forestNeighbors.addAll(Arrays.asList(arr));

                }
                if(forestNeighbors.contains(targetTile)) {
                    if(PathTile.pathAllowed(neighbors, tileToPlace, targetTile)){
                        System.out.println("Can build tree, unfinished forest");
                        return true;
                    }
                    System.out.println("Path blocking");
                    return false;

                }
                else {
                    System.out.println("Can't build here while unfinished forest exists");
                    return false;
                }

            }
        }
        return PathTile.pathAllowed(neighbors, tileToPlace, targetTile);
    }



}