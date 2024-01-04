package agh.ics.oop.model;

import agh.ics.oop.model.animal.Animal;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    //possible problem with randomness in testing

    @Test
    void testRandomGene(){
        //so I just will generate statistically sufficient sample size so random generation is between bounds
        for (int i = 0; i < 10000; i++) {
            ArrayList<Integer> genes = new ArrayList<>();
            //1 gene so between -1 and 0
            genes.add(7);
            Animal pet = new Animal(new Vector2d(0,0),100,genes);
            assertEquals(0, pet.getCurrentGene());
        }
        for (int i = 0; i < 10000; i++) {
            ArrayList<Integer> genes = new ArrayList<>();
            //2 genes so between -2 and 1
            genes.add(7);
            genes.add(7);
            Animal pet = new Animal(new Vector2d(0,0),100,genes);
            assertTrue(pet.getCurrentGene() >= 0 && pet.getCurrentGene() <= 1);
        }
        for (int i = 0; i < 10000; i++) {
            ArrayList<Integer> genes = new ArrayList<>();
            //1000 genes so between -1000 and 999
            for (int j = 0; j < 1000; j++) {
                genes.add(7);
            }
            Animal pet = new Animal(new Vector2d(0,0),100,genes);
            assertTrue(pet.getCurrentGene() >= 0 && pet.getCurrentGene() <= 999);
        }
    }
    @Test
    void testRotateAnimal(){
        //initializing animal
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(7);
        Animal pet = new Animal(new Vector2d(0,0),100,genes);
        //removing randomness;
        pet.setDirection(MapDirection.NORTH);

        //can't test rotating as it is private method, so I'll just move it once and check mapDirection
        try {
            pet.move();
        }catch (GeneOutOfRangeException e){
            e.printStackTrace();
        }

        assertEquals(MapDirection.NORTH_WEST, pet.getDirection());
    }

    @Test
    void testThrowGeneOutOfRangeException(){
        //initializing animal
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(-1);
        Animal rat = new Animal(new Vector2d(0,0),10,genes);
        assertThrows(GeneOutOfRangeException.class, rat::move);
    }

    @Test
    void testMove(){
        //initializing animal
        ArrayList<Integer> genes = new ArrayList<>();
        genes.add(1);
        genes.add(2);
        genes.add(3);
        Animal rat = new Animal(new Vector2d(0,0), 10, genes);
        //removing randomness
        rat.setDirection(MapDirection.NORTH);
        rat.setCurrentGene(0);

        try {
            //0
            rat.move();
            assertEquals(new Vector2d(1, 1), rat.getPosition());
            assertEquals(MapDirection.NORTH_EAST, rat.getDirection());
            assertEquals(9, rat.getEnergy());
            assertEquals(1,rat.getCurrentGene());

            //1
            rat.move();
            assertEquals(new Vector2d(2,0), rat.getPosition());
            assertEquals(MapDirection.SOUTH_EAST, rat.getDirection());
            assertEquals(8, rat.getEnergy());
            assertEquals(2,rat.getCurrentGene());

            //2
            rat.move();
            assertEquals(new Vector2d(1,0), rat.getPosition());
            assertEquals(MapDirection.WEST, rat.getDirection());
            assertEquals(7, rat.getEnergy());
            assertEquals(0,rat.getCurrentGene());

            //0
            rat.move();
            assertEquals(new Vector2d(0,1), rat.getPosition());
            assertEquals(MapDirection.NORTH_WEST, rat.getDirection());
            assertEquals(6, rat.getEnergy());
            assertEquals(1,rat.getCurrentGene());

            //1
            rat.move();
            assertEquals(new Vector2d(1,2), rat.getPosition());
            assertEquals(MapDirection.NORTH_EAST, rat.getDirection());
            assertEquals(5, rat.getEnergy());
            assertEquals(2,rat.getCurrentGene());

            //2
            rat.move();
            assertEquals(new Vector2d(1,1), rat.getPosition());
            assertEquals(MapDirection.SOUTH, rat.getDirection());
            assertEquals(4, rat.getEnergy());
            assertEquals(0,rat.getCurrentGene());

        }catch (GeneOutOfRangeException e){
            e.printStackTrace();
        }
    }
}
