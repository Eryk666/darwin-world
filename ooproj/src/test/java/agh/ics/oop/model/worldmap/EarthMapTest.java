package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animal.Animal;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EarthMapTest {

    //all random variables were tested manually

    @Test
    void getMapBoundary() {
        EarthMap map = new EarthMap(
                new Boundary(new Vector2d(6,9),new Vector2d(21,37)),0);
        assertEquals(new Boundary(new Vector2d(6,9),new Vector2d(21,37)),map.getMapBoundary());
    }

    @Test
    void spawnAnimal() {
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(69);
        Animal animal = new Animal(new Vector2d(7,10),69,genes,10,15);
        Animal animal1 = new Animal(new Vector2d(7,10),699,genes,10,15);
        EarthMap map = new EarthMap(
                new Boundary(new Vector2d(6,9),new Vector2d(21,37)),0);
        map.placeAnimal(animal);
        map.placeAnimal(animal1);
        assertTrue(map.animals.contains(animal));
        assertTrue(map.animals.contains(animal1));
        assertFalse(map.animals.contains(new Animal(new Vector2d(7,10),6123,genes,10,15)));
    }

    @Test
    void reproduce() {
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(69);
        genes.add(69);
        genes.add(69);
        genes.add(69);
        genes.add(69);
        genes.add(69);
        genes.add(69);
        genes.add(69);

        Animal animal = new Animal(new Vector2d(7,10),69,genes,2,8);
        Animal animal1 = new Animal(new Vector2d(7,10),699,genes,2,8);
        EarthMap map = new EarthMap(
                new Boundary(new Vector2d(6,9),new Vector2d(21,37)), 0);
        map.placeAnimal(animal);
        map.placeAnimal(animal1);
        map.reproductionPhase(10);
        assertEquals(3,map.animals.size());
    }

    @Test
    void growGrass() {
        EarthMap map = new EarthMap(
                new Boundary(new Vector2d(0,0),new Vector2d(1000,1000))
                ,0);
        System.out.println(map.getGrasses().size());
        map.growGrass(10);
        assertEquals(10,map.getGrasses().size());
    }

    @Test
    void grimReaperTest() {
        EarthMap map = new EarthMap(
                new Boundary(new Vector2d(0,0),new Vector2d(1000,1000))
                ,0);
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(69);
        Animal animal = new Animal(new Vector2d(7,10),0,genes,2,8);
        map.placeAnimal(animal);
        map.removeDeadAnimals(69);
        assertEquals(0,map.animals.size());
        assertEquals(69,map.deadAnimals.get(0).getDayOfDeath());
    }

    @Test
    void moveAnimals() {
        EarthMap map = new EarthMap(
                new Boundary(new Vector2d(0,0),new Vector2d(1000,1000))
                ,2);
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(2);
        Animal animal = new Animal(new Vector2d(7,10),0,genes,2,8);
        animal.setDirection(MapDirection.NORTH);
        map.placeAnimal(animal);

        map.movementPhase();

        assertEquals(new Vector2d(8,10),map.animals.get(0).getPosition());
    }

}
