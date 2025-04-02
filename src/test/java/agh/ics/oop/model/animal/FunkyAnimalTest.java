package agh.ics.oop.model.animal;

import agh.ics.oop.model.GeneOutOfRangeException;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FunkyAnimalTest {
    @Test
    void testRandomGene(){
        //so I just will generate statistically sufficient sample size so random generation is between bounds
        for (int i = 0; i < 10000; i++) {
            ArrayList<Integer> genes = new ArrayList<>();
            //1 gene so between -1 and 0
            genes.add(7);
            Animal pet = new FunkyAnimal(new Vector2d(0,0),100,genes,0,0);
            assertTrue(pet.getCurrentGeneIndex() >= -1 && pet.getCurrentGeneIndex() <= 0);
        }
        for (int i = 0; i < 10000; i++) {
            ArrayList<Integer> genes = new ArrayList<>();
            //2 genes so between -2 and 1
            genes.add(7);
            genes.add(7);
            Animal pet = new FunkyAnimal(new Vector2d(0,0),100,genes,0,0);
            assertTrue(pet.getCurrentGeneIndex() >= -2 && pet.getCurrentGeneIndex() <= 1);
        }
        for (int i = 0; i < 10000; i++) {
            ArrayList<Integer> genes = new ArrayList<>();
            //1000 genes so between -1000 and 999
            for (int j = 0; j < 1000; j++) {
                genes.add(7);
            }
            Animal pet = new FunkyAnimal(new Vector2d(0,0),100,genes,0,0);
            assertTrue(pet.getCurrentGeneIndex() >= -1000 && pet.getCurrentGeneIndex() <= 999);
        }
    }
    @Test
    void testMove(){
        //initializing animal
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(1);
        genes.add(2);
        genes.add(3);
        Animal rat = new FunkyAnimal(new Vector2d(0,0), 10, genes,0,0);
        //removing randomness
        rat.setDirection(MapDirection.NORTH);
        rat.setCurrentGeneIndex(0);

        try {
            //0
            rat.move();
            assertEquals(new Vector2d(1, 1), rat.getPosition());
            assertEquals(MapDirection.NORTH_EAST, rat.getDirection());
            assertEquals(9, rat.getEnergy());
            assertEquals(1,rat.getCurrentGeneIndex());

            //1
            rat.move();
            assertEquals(new Vector2d(2,0), rat.getPosition());
            assertEquals(MapDirection.SOUTH_EAST, rat.getDirection());
            assertEquals(8, rat.getEnergy());
            assertEquals(2,rat.getCurrentGeneIndex());

            //2
            rat.move();
            assertEquals(new Vector2d(1,0), rat.getPosition());
            assertEquals(MapDirection.WEST, rat.getDirection());
            assertEquals(7, rat.getEnergy());
            assertEquals(-3,rat.getCurrentGeneIndex());

            //-3
            rat.move();
            assertEquals(new Vector2d(2,1), rat.getPosition());
            assertEquals(MapDirection.NORTH_EAST, rat.getDirection());
            assertEquals(6, rat.getEnergy());
            assertEquals(-2,rat.getCurrentGeneIndex());

            //-2
            rat.move();
            assertEquals(new Vector2d(3,0), rat.getPosition());
            assertEquals(MapDirection.SOUTH_EAST, rat.getDirection());
            assertEquals(5, rat.getEnergy());
            assertEquals(-1,rat.getCurrentGeneIndex());

            //-1
            rat.move();
            assertEquals(new Vector2d(3,-1), rat.getPosition());
            assertEquals(MapDirection.SOUTH, rat.getDirection());
            assertEquals(4, rat.getEnergy());
            assertEquals(0,rat.getCurrentGeneIndex());

        }catch (GeneOutOfRangeException e){
            e.printStackTrace();
        }
    }

    @Test
    void testGetGene(){
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(6);
        genes.add(2);
        genes.add(4);
        Animal rat = new FunkyAnimal(new Vector2d(0,0), 10, genes,0,0);
        //removing randomness
        rat.setCurrentGeneIndex(0);

        assertEquals(6, rat.getCurrentGene());

        rat.setCurrentGeneIndex(1);
        assertEquals(2, rat.getCurrentGene());

        rat.setCurrentGeneIndex(2);
        assertEquals(4,rat.getCurrentGene());

        rat.setCurrentGeneIndex(-3);
        assertEquals(4,rat.getCurrentGene());

        rat.setCurrentGeneIndex(-2);
        assertEquals(2,rat.getCurrentGene());

        rat.setCurrentGeneIndex(-1);
        assertEquals(6,rat.getCurrentGene());
    }

    @Test
    void testNextGene(){
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(6);
        genes.add(2);
        genes.add(4);
        Animal rat = new FunkyAnimal(new Vector2d(0,0), 10, genes,0,0);
        rat.setCurrentGeneIndex(0);

        rat.nextGene();
        assertEquals(1,rat.getCurrentGeneIndex());

        rat.nextGene();
        assertEquals(2,rat.getCurrentGeneIndex());

        rat.nextGene();
        assertEquals(-3,rat.getCurrentGeneIndex());

        rat.nextGene();
        assertEquals(-2,rat.getCurrentGeneIndex());

        rat.nextGene();
        assertEquals(-1,rat.getCurrentGeneIndex());

        rat.nextGene();
        assertEquals(-0,rat.getCurrentGeneIndex());
    }
}
