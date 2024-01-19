package agh.ics.oop;
import agh.ics.oop.front.SimulationApp;
import agh.ics.oop.model.*;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    public static void main(String[] args) {
        Application.launch(SimulationApp.class,args);
//        final int initialPopulation = 10;
//        final int initialAnimalEnergy = 100;
//        final int reproductionEnergyMinimum = 50;
//        final int reproductionEnergyCost = 30;
//        final int genesLength = 8;
//        final int initialGrassNumber = 5;
//        final int energyPerGrass = 10;
//        final int grassGrownPerDay = 5;
//        final int mapWidth = 50;
//        final int mapHeight = 50;
//        final int animalType = 0;
//        final int worldType = 0;
//
//        AbstractWorldMap worldMap = generateWorldMap(
//            worldType, mapWidth, mapHeight
//        );
//
//        List<Animal> initialAnimals = generateAnimals(
//            animalType, initialPopulation, initialAnimalEnergy, genesLength, mapWidth, mapHeight
//        );
//
//        Simulation simulation = new Simulation(
//            worldMap,
//            initialAnimals,
//            reproductionEnergyMinimum,
//            reproductionEnergyCost,
//            initialGrassNumber,
//            grassGrownPerDay,
//            energyPerGrass
//        );
//
//        simulation.run();
    }
/*
    private static AbstractWorldMap generateWorldMap(
        int worldType,
        int mapWidth,
        int mapHeight
    ) {
        Boundary mapBoundary = new Boundary(
            new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1)
        );

        if (worldType != 1) {
            return new EarthMap(mapBoundary);
        } else {
            return new FunkyMap(mapBoundary);
        }
    }

    private static List<Animal> generateAnimals(
        int animalType,
        int initialPopulation,
        int initialAnimalEnergy,
        int genesLength,
        int mapWidth,
        int mapHeight
    ) {
        Random random = new Random();
        List<Animal> animals = new ArrayList<>();

        for (int i = 0; i < initialPopulation; i++) {
            Vector2d position = new Vector2d(
                random.nextInt(mapWidth), random.nextInt(mapHeight)
            );

            List<Integer> genes = new ArrayList<>();
            for (int j = 0; j < genesLength; j++) {
                genes.add(random.nextInt(8));
            }

            if (animalType != 1) {
                animals.add(new Animal(position, initialAnimalEnergy, genes));
            } else {
                animals.add(new FunkyAnimal(position, initialAnimalEnergy, genes));
            }
        }

        return animals;
    }*/
}
