package agh.ics.oop.model;

import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;
    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //getters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d other){
        return other.getX() >= x && other.getY() >= y;
    }

    public boolean follows(Vector2d other){
        return other.getX() <= x && other.getY() <= y;
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(x + other.getX(), y + other.getY());
    }

    public Vector2d subtract(Vector2d other){
        return new Vector2d(x - other.getX(), y - other.getY());
    }

    public Vector2d upperRight(Vector2d other){
        return new Vector2d(Math.max(x,other.getX()), Math.max(y,other.getY()));
    }
    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(Math.min(x,other.getX()), Math.min(y,other.getY()));
    }

    //probably redundant, but left out in case
    /*public Vector2d opposite(){
        return new Vector2d(x*(-1), y*(-1));
    }*/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.getX() && y == vector2d.getY();
    }
    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
