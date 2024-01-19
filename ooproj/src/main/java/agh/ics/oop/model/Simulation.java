package agh.ics.oop.model;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.worldmap.AbstractWorldMap;

import java.util.List;

public class Simulation implements Runnable {
    private final AbstractWorldMap worldMap;
    private final List<Animal> initialAnimals;
    private final int reproductionEnergyMinimum;
    private final int reproductionEnergyCost;
    private final int initialGrassNumber;
    private final int energyPerGrass;
    private final int grassGrownPerDay;

    public Simulation(
        AbstractWorldMap worldMap,
        List<Animal> initialAnimals,
        int reproductionEnergyMinimum,
        int reproductionEnergyCost,
        int initialGrassNumber,
        int grassGrownPerDay,
        int energyPerGrass
    ) {
        this.worldMap = worldMap;
        this.initialAnimals = initialAnimals;
        this.reproductionEnergyMinimum = reproductionEnergyMinimum;
        this.reproductionEnergyCost = reproductionEnergyCost;
        this.initialGrassNumber = initialGrassNumber;
        this.grassGrownPerDay = grassGrownPerDay;
        this.energyPerGrass = energyPerGrass;
    }

    public void run() {
        for (Animal animal : initialAnimals) {
            worldMap.placeAnimal(animal);
        }
        worldMap.growGrass(initialGrassNumber);

        // Simulation ends when all animals are dead
        while (!worldMap.getAnimals().isEmpty()) {
            simulateDay();
        }
    }

    private void simulateDay() {
        worldMap.removeDeadAnimals();
        worldMap.movementPhase();
        worldMap.feedingPhase(energyPerGrass);
        worldMap.reproductionPhase(reproductionEnergyCost);
        worldMap.growGrass(grassGrownPerDay);
        worldMap.updateMap();
    }
}
