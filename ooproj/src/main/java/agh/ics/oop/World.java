package agh.ics.oop;
import agh.ics.oop.model.*;
import java.util.ArrayList;

public class World {
    public static void main(String[] args) {
        //ONLY FOR DEBUGING!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //raczej działa, ale zrobić testy by się przydało
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(7);
        genes.add(6);
        genes.add(3);
        genes.add(0);
        Animal animal = new Animal(new Vector2d(0,0), 100, genes);
        /*while (true){
            try {
                animal.move();
            }catch (GeneOutOfRangeException e){
                System.err.println(e);
            }
        }*/
    }
}
