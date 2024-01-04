package agh.ics.oop.model;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.FunkyAnimal;
import agh.ics.oop.model.worldmap.AbstractWorldMap;
import agh.ics.oop.model.worldmap.EarthMap;
import agh.ics.oop.model.worldmap.FunkyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    private final int initialPopulation;
    private final int initialAnimalEnergy;
    private final int reproductionEnergyMinimum;
    private final int reproductionEnergyCost;
    private final int genesLength;
    private final int initialGrassNumber;
    private final int energyPerGrass;
    private final int grassGrownPerDay;
    private final int mapWidth;
    private final int mapHeight;
    private final int animalType;
    private final int worldType;
    private final AbstractWorldMap worldMap;

    public Simulation(
        int initialPopulation,
        int initialAnimalEnergy,
        int reproductionEnergyMinimum,
        int reproductionEnergyCost,
        int genesLength,
        int initialGrassNumber,
        int energyPerGrass,
        int grassGrownPerDay,
        int mapWidth,
        int mapHeight,
        int worldType,
        int animalType
    ) {
        this.initialPopulation = initialPopulation;
        this.initialAnimalEnergy = initialAnimalEnergy;
        this.reproductionEnergyMinimum = reproductionEnergyMinimum;
        this.reproductionEnergyCost = reproductionEnergyCost;
        this.genesLength = genesLength;
        this.initialGrassNumber = initialGrassNumber;
        this.energyPerGrass = energyPerGrass;
        this.grassGrownPerDay = grassGrownPerDay;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.animalType = animalType;
        this.worldType = worldType;

        Boundary mapBoundary = new Boundary(
            new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1)
        );
        if (worldType != 1) {
            this.worldMap = new EarthMap(mapBoundary);
        } else {
            this.worldMap = new FunkyMap(mapBoundary);
        }
    }

    public void run() {
        populateWorld();
        worldMap.growGrass(initialGrassNumber);
        while (!worldMap.getAnimals().isEmpty()) {
            simulateDay();
        }
    }

    private void populateWorld() {
        Random random = new Random();

        for (int i = 0; i < initialPopulation; i++) {
            Animal animal;

            Vector2d position = new Vector2d(
                random.nextInt(mapWidth), random.nextInt(mapHeight)
            );

            List<Integer> genes = new ArrayList<>();
            for (int j = 0; j < genesLength; j++) {
                genes.add(random.nextInt(8));
            }

            if (this.animalType != 1) {
                animal = new Animal(position, initialAnimalEnergy, genes);
            } else {
                animal = new FunkyAnimal(position, initialAnimalEnergy, genes);
            }

            worldMap.placeAnimal(animal);
        }
    }

    private void simulateDay() {
        worldMap.removeDeadAnimals();
        worldMap.movementPhase();
        worldMap.feedingPhase(energyPerGrass);
        worldMap.reproductionPhase(reproductionEnergyCost, reproductionEnergyMinimum);
        worldMap.growGrass(grassGrownPerDay);
    }
}
