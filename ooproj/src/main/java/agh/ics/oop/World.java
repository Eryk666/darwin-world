package agh.ics.oop;
import agh.ics.oop.model.*;
import java.util.ArrayList;

public class World {
    public static void main(String[] args) {
        //ONLY FOR DEBUGING!!!!!!!!!!!!!!!!!!!!!!!!!!!
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(7);
        genes.add(6);
        genes.add(3);
        genes.add(0);
        Animal animal = new Animal(new Vector2d(0,0), 100, genes);
        while (true){
            animal.move();
        }
    }
}
