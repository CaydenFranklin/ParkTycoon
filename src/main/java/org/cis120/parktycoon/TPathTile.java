package org.cis120.parktycoon;

import java.util.Random;

public class TPathTile extends PathTile{
    public static final String [] IMG_FILES = {"files/TPathTileNES.png","files/TPathTileNEW.png",
                                                "files/TPathTileNWS.png","files/TPathTileSEW.png",
                                                "files/XPathTile.png"};
    public static final String [] IMG_VARIANTS = null;


    public TPathTile(int rotation){
        super(rotation);
        setImgAndRotation(rotation);

    }


    public TPathTile(int rotation, int px, int py){
        super(rotation, px, py);
        setImgAndRotation(rotation);
    }

    private void setImgAndRotation(int rotation) {
        this.setRotation(rotation);
        this.setImg(IMG_FILES[rotation]);
        switch (this.getRotation()) {
            //North East South T Tile
            case 0:
                this.setConnections(new ConnectionPoints[]{ConnectionPoints.NORTH, ConnectionPoints.EAST,
                                                                                    ConnectionPoints.SOUTH});
                break;
            //North East West L Tile
            case 1:
                this.setConnections(new ConnectionPoints[]{ConnectionPoints.NORTH, ConnectionPoints.EAST,
                        ConnectionPoints.WEST});
                break;
            //North West South L Tile
            case 2:
                this.setConnections(new ConnectionPoints[]{ConnectionPoints.NORTH, ConnectionPoints.WEST,
                        ConnectionPoints.SOUTH});
                break;

            //South East West L Tile
            case 3:
                this.setConnections(new ConnectionPoints[]{ConnectionPoints.SOUTH, ConnectionPoints.EAST,
                        ConnectionPoints.WEST});
                break;

            //X Tile
            case 4:
                this.setConnections(new ConnectionPoints[]{ConnectionPoints.NORTH, ConnectionPoints.SOUTH,
                        ConnectionPoints.EAST, ConnectionPoints.WEST});
                break;
            default:
                break;
        }
    }
}
