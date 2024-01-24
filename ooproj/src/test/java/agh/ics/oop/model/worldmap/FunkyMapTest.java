package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunkyMapTest {
    @Test
    void growGrass() {
        //I will check if newly generated map have good amount of grass
        AbstractWorldMap map = new FunkyMap(new Boundary(new Vector2d(0,0),new Vector2d(100,100)), 0);
        //no grass
        map.growGrass(10); //because grass is empty I supper it from EarthMap
        map.growGrass(10); // now we do from FunkyMap method

        assertEquals(20,map.getGrasses().size());
    }
}
