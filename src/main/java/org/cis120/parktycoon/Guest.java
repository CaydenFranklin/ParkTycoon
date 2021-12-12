package org.cis120.parktycoon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Guest extends GameObj {
    public final String IMG_FILE = "files/Guest.png";
    public final String VARIANT_IMG_FILE = "files/GuestBlack.png";
    private BufferedImage img;
    public static final int VELOCITY = 1;

    private PathTile currentTile;
    private ArrayList<Point> nextPoints;
    private PathTile destinationTile;
    private Node<PathTile> pathTree;
    private Random r;
    private PathTile.ConnectionPoints directionFrom;

    public static final int SIZE = 50;

    public Guest(Node<PathTile> pathTree){
        super(pathTree.getPath().getCenter().x, pathTree.getPath().getCenter().y, SIZE, SIZE);
        this.pathTree = pathTree;
        this.destinationTile = null;
        this.currentTile = pathTree.getPath();
        this.nextPoints = new ArrayList<Point>();
        this.r = new Random();
        if(r.nextBoolean())
            setImg(IMG_FILE);
        else
            setImg(VARIANT_IMG_FILE);
        PathTile.ConnectionPoints [] currentConnectionSet = this.currentTile.getConnections();
        takeNextStep();
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

    private void takeNextStep(){
        ArrayList<PathTile.ConnectionPoints> options = new ArrayList<PathTile.ConnectionPoints>();
        for(PathTile.ConnectionPoints cp : currentTile.getConnections()){
            Node<PathTile> possibleNext = pathTree.getTileDirection(cp);
            if(possibleNext != null){
                options.add(cp);
            }
        }
        int choice = r.nextInt(options.size());
        pathTree = pathTree.getTileDirection(options.get(choice));
        destinationTile = pathTree.getPath();
        updatePointArray();
    }


    public void updatePointArray(){
        nextPoints.clear();
        nextPoints.add(currentTile.getCenter());
        nextPoints.add(destinationTile.getCenter());

    }

    public void adjustVisualPosition(int directionX, int directionY){
        this.setPx(this.getPx() + -1 * directionX * VELOCITY);
        this.setPy(this.getPy() + -1 * directionY * VELOCITY);
        System.out.println("Guest at " + this.getPx()  + " " + this.getPy());
    }

    public void updatePosition() {
        if(nextPoints.size() != 0 ) {
            if (this.getPx() != nextPoints.get(0).x || this.getPy() != nextPoints.get(0).y) {
                float deltaX = this.getPx() - (nextPoints.get(0).x);
                int directionX = (int) Math.signum(deltaX);
                float deltaY = this.getPy() - (nextPoints.get(0).y);
                int directionY = (int) Math.signum(deltaY);
                adjustVisualPosition(directionX, directionY);
            }
            else{
                nextPoints.remove(0);
                currentTile = destinationTile;
            }
        }

        if(nextPoints.size() == 0){
            takeNextStep();
        }

    }



    public void draw(Graphics g) {
        g.drawImage(img, this.getPx() - SIZE/2, this.getPy()- SIZE/2, SIZE, SIZE,
                null);
    }



}
