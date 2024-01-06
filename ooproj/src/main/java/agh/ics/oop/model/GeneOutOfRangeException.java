package agh.ics.oop.model;

public class GeneOutOfRangeException extends Exception {
    public GeneOutOfRangeException(int Gene){
        super("Gene" + Gene + "out of range between 0 and 7");
    }
}
