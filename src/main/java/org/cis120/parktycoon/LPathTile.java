package org.cis120.parktycoon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class LPathTile extends PathTile {
    public static final String [] IMG_FILES = {"files/LPathTileNE.png","files/LPathTileNW.png",
                                                "files/LPathTileSE.png","files/LPathTileSW.png"};

    public final String [] IMG_VARIANTS = {"files/LCurvedPathTileNE.png","files/LCurvedPathTileNW.png",
                                            "files/LCurvedPathTileSE.png", "files/LCurvedPathTileSW.png"};


    public LPathTile(int rotation){
        super(rotation);
        setImgAndRotation(rotation);
    }


    public LPathTile(int rotation, int px, int py){
        super(rotation, px, py);
        setImgAndRotation(rotation);
    }

    private void setImgAndRotation(int rotation) {
        this.setRotation(rotation);
        if(this.IMG_VARIANTS != null){
            Random r = new Random();
            if(r.nextBoolean())
                this.setImg(IMG_VARIANTS[rotation]);
            else
                this.setImg(IMG_FILES[rotation]);
        }
        switch (this.getRotation()) {
            //North East L Tile
            case 0:
                this.setConnections(new ConnectionPoints[]{ConnectionPoints.NORTH, ConnectionPoints.EAST});
                break;
                //North West L Tile
            case 1:
                this.setConnections(new ConnectionPoints[]{ConnectionPoints.NORTH, ConnectionPoints.WEST});
                break;
                //South East L Tile
            case 2:
                this.setConnections(new ConnectionPoints[]{ConnectionPoints.SOUTH, ConnectionPoints.EAST});
                break;
                //South West L Tile
            case 3:
                this.setConnections(new ConnectionPoints[]{ConnectionPoints.SOUTH, ConnectionPoints.WEST});
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isValid(Tile[] neighbors, ArrayList<HashSet<Tile>> forests, Tile targetTile, Tile tileToPlace) {
        int failingConnections = 0;
        Tile northNeighbor = neighbors[0];
        Tile eastNeighbor = neighbors[1];
        Tile southNeighbor = neighbors[2];
        Tile westNeighbor = neighbors[3];
        switch (this.getRotation()) {
            //North East L Tile
            case 0:
                if (northNeighbor != null && northNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) northNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.SOUTH)) {
                        failingConnections++;
                    }
                }
                if (eastNeighbor != null && eastNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) eastNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.WEST)) {
                        failingConnections++;
                    }
                }
                break;

            //North West L Tile
            case 1:
                if (northNeighbor != null && northNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) northNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.SOUTH)) {
                        failingConnections++;
                    }
                }
                if (westNeighbor != null && westNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) westNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.EAST)) {
                        failingConnections++;
                    }
                }
                break;

            //South East L Tile
            case 2:
                if (southNeighbor != null && southNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) southNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.NORTH)) {
                        failingConnections++;
                    }
                }
                if (eastNeighbor != null && eastNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) eastNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.WEST)) {
                        failingConnections++;
                    }
                }
                break;

            //South West L Tile
            case 3:
                if (southNeighbor != null && southNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) southNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.NORTH)) {
                        failingConnections++;
                    }
                }
                if (westNeighbor != null && westNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) westNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.EAST)) {
                        failingConnections++;
                    }
                }
                break;

            case 4:
                if (northNeighbor != null && northNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) northNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.SOUTH)) {
                        failingConnections++;
                    }
                }
                if (southNeighbor != null && southNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) southNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.NORTH)) {
                        failingConnections++;
                    }
                }
                if (eastNeighbor != null && eastNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) eastNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.WEST)) {
                        failingConnections++;
                    }
                }
                if (westNeighbor != null && westNeighbor.getType().equals(TileType.PATH)) {
                    PathTile p = (PathTile) westNeighbor;
                    if (!Arrays.asList(p.getConnections()).contains(ConnectionPoints.EAST)) {
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
