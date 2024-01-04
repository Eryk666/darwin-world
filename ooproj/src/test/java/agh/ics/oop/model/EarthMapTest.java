package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EarthMapTest {

    //all random variables were tested manually

    @Test
    void getMapBoundary() {
        EarthMap map = new EarthMap(new ArrayList<>(),10,10,10,
                new Boundary(new Vector2d(6,9),new Vector2d(21,37)));
        assertEquals(new Boundary(new Vector2d(6,9),new Vector2d(21,37)),map.getMapBoundary());
    }

    @Test
    void spawnAnimal() {

    }

    @Test
    void sex() {
    }

    @Test
    void growGrass() {
    }

    @Test
    void grimReaper() {
    }

    @Test
    void moveAnimals() {
    }

    @Test
    void attemptEatGrass() {
    }

    @Test
    void determineStrongestAnimal() {
    }

    @Test
    void attemptSex() {
    }
}
