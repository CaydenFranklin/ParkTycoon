package org.cis120.parktycoon;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.cis120.parktycoon.PathTile.ConnectionPoints;

public class Node<PathTile> {
    private PathTile path;

    private HashSet<PathTile> tileSet;
    private Node<PathTile> north;
    private Node<PathTile> south;
    private Node<PathTile> east;
    private Node<PathTile> west;

    public Node(PathTile path) {
        this.path = path;
        this.tileSet = new HashSet<PathTile>();
        tileSet.add(path);
        north = null;

    }

    public Node(Node<PathTile> n) {
        this.path = n.getPath();
        this.tileSet = n.getTileSet();
        this.north = n.getTileDirection(ConnectionPoints.NORTH);
        this.east = n.getTileDirection(ConnectionPoints.EAST);
        this.south = n.getTileDirection(ConnectionPoints.SOUTH);
        this.west = n.getTileDirection(ConnectionPoints.WEST);
    }

    public HashSet<PathTile> getTileSet() {
        return tileSet;
    }

    public void setTileSet(HashSet<PathTile> tileSet) {
        this.tileSet = tileSet;
    }

    public Node<PathTile> getTileDirection(ConnectionPoints cp){
        if(cp == ConnectionPoints.NORTH){
            if(this.north != null){
                return new Node<PathTile>(this.north);
            }
            else{
                return null;
            }
        }
        else if(cp == ConnectionPoints.EAST){
            if(this.east != null){
                return new Node<PathTile>(this.east);
            }
            else{
                return null;
            }
        }
        else if(cp == ConnectionPoints.SOUTH){
            if(this.south != null){
                return new Node<PathTile>(this.south);
            }
            else{
                return null;
            }
        }
        else if(cp == ConnectionPoints.WEST){
            if(this.west != null){
                return new Node<PathTile>(this.west);
            }
            else{
                return null;
            }
        }
        return null;
    }

    public void addPath(ConnectionPoints cp, PathTile p){
        if(cp == ConnectionPoints.NORTH){
            north = new Node<PathTile>(p);
        }
        else if(cp == ConnectionPoints.EAST){
            east = new Node<PathTile>(p);
        }
        else if(cp == ConnectionPoints.SOUTH){
            south = new Node<PathTile>(p);
        }
        else if(cp == ConnectionPoints.WEST){
            west = new Node<PathTile>(p);
        }
    }

    public void addNode(ConnectionPoints cp, Node<PathTile> n){
        if(cp == ConnectionPoints.NORTH){
            north = n;
        }
        else if(cp == ConnectionPoints.EAST){
            east = n;
        }
        else if(cp == ConnectionPoints.SOUTH){
            south = n;
        }
        else if(cp == ConnectionPoints.WEST){
            west = n;
        }
        tileSet.add(n.getPath());
        n.setTileSet(tileSet);
    }

    public boolean contains(PathTile p){
        return tileSet.contains(p);
    }

//    public HashSet<PathTile> setOfPaths(HashSet<PathTile> set){
//        if(path == null){
//            return set;
//        }
//        set.add(path);
//        for(ConnectionPoints cp : ConnectionPoints.values()){
//            Node connectedNode = getTileDirection(cp);
//            if(connectedNode != null && !set.contains(connectedNode.getPath())){
//                set.addAll(connectedNode.setOfPaths(set));
//            }
//        }
//        return set;
//    }

    public PathTile getPath(){
        return this.path;
    }

    public String toString(){
        String s = path.toString();
        if(north != null){
            s+= ", NORTH: " + north.toString();
        }
        if(east != null){
            s+= ", EAST: " + east.toString();
        }
        if(south != null){
            s+= ", SOUTH: " + south.toString();
        }
        if(west != null){
            s+= ", WEST: " + west.toString();
        }
        return s;
    }

    public int size(){
        int size = 1;
        if(this.north != null){
            size += north.size();
        }
        if(this.east != null){
            size += east.size();
        }
        if(this.south != null){
            size += south.size();
        }
        if(this.west != null){
            size += west.size();
        }
        return size;
    }





    public boolean overlap(Node<PathTile> n){
        HashSet<PathTile> currentSet = this.getTileSet();
        HashSet<PathTile> nodeSet = n.getTileSet();
        return !Collections.disjoint(currentSet, nodeSet);
    }

}

