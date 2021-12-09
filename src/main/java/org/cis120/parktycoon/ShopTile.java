package org.cis120.parktycoon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ShopTile extends LPathTile{
    public static final String [] IMG_FILES = new String[]{"files/shopTileN.png", "files/shopTileE.png",
            "files/shopTileS.png", "files/shopTileW.png"};

    public static enum ConnectionPoints {NORTH, EAST, SOUTH, WEST};

    private PathTile.ConnectionPoints[] connections;

    private int rotation;

    public ShopTile(int rotation){
        super(rotation);
        this.setType(TileType.SHOP);
        setImgAndRotation(rotation);

    }

    public ShopTile(int rotation, int px, int py){
        super(rotation, px, py);
        this.setType(TileType.SHOP);
        setImgAndRotation(rotation);
    }

    private void setImgAndRotation(int rotation) {
        this.setImg(IMG_FILES[rotation]);
        this.setRotation(rotation);
        this.setType(TileType.SHOP);
        switch (this.getRotation()) {
            //North Tile
            case 0:
                this.setConnections(new PathTile.ConnectionPoints[]{PathTile.ConnectionPoints.NORTH});
                break;
            //East Tile
            case 1:
                this.setConnections(new PathTile.ConnectionPoints[]{PathTile.ConnectionPoints.EAST});
                break;
            //South Tile
            case 2:
                this.setConnections(new PathTile.ConnectionPoints[]{PathTile.ConnectionPoints.SOUTH});
                break;
            //West Tile
            case 3:
                this.setConnections(new PathTile.ConnectionPoints[]{PathTile.ConnectionPoints.WEST});
                break;
            default:
                break;
        }
    }

    public boolean isValid(Tile[] neighbors, ArrayList<HashSet<Tile>> forests, Tile targetTile, Tile tileToPlace) {
        int failingConnections = 0;
        Tile northNeighbor = neighbors[0];
        Tile eastNeighbor = neighbors[1];
        Tile southNeighbor = neighbors[2];
        Tile westNeighbor = neighbors[3];
        switch (this.getRotation()) {
            //North Shop Tile
            case 0:
                if (southNeighbor != null && southNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) southNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(PathTile.ConnectionPoints.NORTH)) {
                        failingConnections++;
                    }
                }
                break;

            //East Tile
            case 1:
                if (eastNeighbor != null && eastNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) eastNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(PathTile.ConnectionPoints.WEST)) {
                        failingConnections++;
                    }
                }
                break;

            //South Tile
            case 2:
                if (northNeighbor != null && northNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) northNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(PathTile.ConnectionPoints.SOUTH)) {
                        failingConnections++;
                    }
                }
                break;

            //West Shop Tile
            case 3:
                if (westNeighbor != null && westNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) westNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(PathTile.ConnectionPoints.EAST)) {
                        failingConnections++;
                    }
                }
                break;
            default:
                failingConnections++;
                break;
        }
        return (failingConnections == 0);
    }



}
