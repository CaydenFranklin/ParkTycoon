package org.cis120.parktycoon;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GrassTile extends Tile{
    public final String IMG_FILE = "files/GrassTileTest.png";

    public GrassTile(){
        super(TileType.GRASS);
        this.setImg(IMG_FILE);
    }


    public GrassTile(int px, int py){
        super(TileType.GRASS, px, py);
        this.setImg(IMG_FILE);
    }

    public boolean isValid(Tile[] neighbors, Tile targetTile, Tile tileToPlace){
        return super.isValid(neighbors, targetTile, tileToPlace);
    }
}
