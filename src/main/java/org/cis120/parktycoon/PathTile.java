package org.cis120.parktycoon;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class PathTile extends Tile {
    public final String VERTICAL_IMG_FILE = "files/pathTileVertical.png";
    public final String HORIZONTAL_IMG_FILE = "files/pathTileHorizontal.png";

    public static enum ConnectionPoints {NORTH, EAST, SOUTH, WEST;
        public static ConnectionPoints getOppositeDirection(ConnectionPoints cp){
            if(cp == ConnectionPoints.NORTH)
                return ConnectionPoints.SOUTH;
            else if(cp == ConnectionPoints.EAST)
                return ConnectionPoints.WEST;
            else if(cp == ConnectionPoints.SOUTH)
                return ConnectionPoints.NORTH;
            else
                return ConnectionPoints.EAST;
        }

    };

    private ConnectionPoints [] connections;

    private int rotation;


    public PathTile(int rotation){
        super(Tile.TileType.PATH);
        this.setImg(IMG_FILE);
        this.rotation = rotation;
        if (rotation == 1){
            this.connections = new ConnectionPoints[]{ConnectionPoints.WEST, ConnectionPoints.EAST};
            this.setImg(HORIZONTAL_IMG_FILE);
        }
        else{
            this.connections = new ConnectionPoints[]{ConnectionPoints.NORTH, ConnectionPoints.SOUTH};
            this.setImg(VERTICAL_IMG_FILE);
        }

    }


    public PathTile(int rotation, int px, int py){
        super(TileType.PATH, px, py);
        this.setImg(IMG_FILE);
        if (rotation == 1){
            this.connections = new ConnectionPoints[]{ConnectionPoints.WEST, ConnectionPoints.EAST};
            this.setImg(HORIZONTAL_IMG_FILE);
        }
        else{
            this.connections = new ConnectionPoints[]{ConnectionPoints.NORTH, ConnectionPoints.SOUTH};
            this.setImg(VERTICAL_IMG_FILE);
        }
    }

    @Override
    public boolean isValid(Tile[] neighbors, Tile targetTile, Tile tileToPlace){
        return super.isValid(neighbors, targetTile, tileToPlace);
    }

    public ConnectionPoints[] getConnections(){
        return this.connections.clone();
    }

    public void setConnections(ConnectionPoints[] connections){
        this.connections = connections;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
       this.rotation = rotation;
    }


    public static boolean pathConnected(int neighborDirection, PathTile neighbor, PathTile tileToPlace){
        ConnectionPoints [] neighborConnectionSet = neighbor.getConnections();
        ConnectionPoints [] placeConnectionSet = tileToPlace.getConnections();

        ConnectionPoints touching = ConnectionPoints.values()[neighborDirection];
        ConnectionPoints opposite = ConnectionPoints.getOppositeDirection(touching);
        return Arrays.asList(placeConnectionSet).contains(touching) &&
                Arrays.asList(neighborConnectionSet).contains(opposite);
    }

    public static boolean pathConflict(int neighborDirection, PathTile neighbor, PathTile tileToPlace){
        ConnectionPoints [] neighborConnectionSet = neighbor.getConnections();
        ConnectionPoints [] placeConnectionSet = tileToPlace.getConnections();

        ConnectionPoints touching = ConnectionPoints.values()[neighborDirection];
        ConnectionPoints opposite = ConnectionPoints.getOppositeDirection(touching);
        if(Arrays.asList(neighborConnectionSet).contains(opposite)) {
            return (Arrays.asList(placeConnectionSet).contains(touching));
        }
        else {
            return (!Arrays.asList(placeConnectionSet).contains(touching));
        }
    }


    public static boolean pathAllowed(Tile[] neighbors, Tile tileToPlace, Tile targetTile){
        ConnectionPoints [] connectionSet = null;
        int failingConnections = 0;

        if(tileToPlace instanceof PathTile) {
            for (int i = 0; i < neighbors.length; i++) {
                Tile n = neighbors[i];
                if (n != null) {
                    if (n instanceof PathTile) {
                        if (!pathConflict(i, (PathTile) n, (PathTile) tileToPlace) &&
                                !pathConnected(i, (PathTile) n, (PathTile) tileToPlace)) {
                            failingConnections++;
                        }
                    }
                    else{
                        connectionSet = ((PathTile) tileToPlace).getConnections();
                        ConnectionPoints touching = ConnectionPoints.values()[i];
                        if(Arrays.asList(connectionSet).contains(touching) && n.getType() != TileType.BLANK){
                            failingConnections++;
                        }
                    }
                }
            }
        }
        else {
            //Iterate through neighbors and determine the connection they would need to
            //be improperly aligned, and then check if it would exist.
            for (int i = 0; i < neighbors.length; i++) {
                Tile n = neighbors[i];
                if (n != null) {
                    if(n instanceof PathTile){
                        ConnectionPoints touching = ConnectionPoints.values()[i];
                        ConnectionPoints opp = ConnectionPoints.getOppositeDirection(touching);
                        PathTile nP = (PathTile) n;
                        if(Arrays.asList(nP.getConnections()).contains(opp)){
                            failingConnections++;
                        }
                    }
                }
            }
        }
        return (failingConnections == 0);
    }
}
