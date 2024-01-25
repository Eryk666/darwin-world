package agh.ics.oop.model;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.worldmap.AbstractWorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Simulation implements Runnable {
    private final UUID simulationID = UUID.randomUUID();
    private final AbstractWorldMap worldMap;
    private final List<Animal> initialAnimals;
    private final int reproductionEnergyMinimum;
    private final int reproductionEnergyCost;
    private final int initialGrassNumber;
    private final int energyPerGrass;
    private final int grassGrownPerDay;
    private final List<SimulationChangeListener> listeners = new ArrayList<>();
    private int daysPassed = 0;

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
        notifyListeners();

        // Simulation ends when all animals are dead
        while (!worldMap.getAnimals().isEmpty()) {
            if (worldMap.getPaused()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                continue;
            }
            simulateDay();
            daysPassed += 1;
            notifyListeners();
        }
    }

    private void simulateDay()  {
        worldMap.removeDeadAnimals(daysPassed);
        worldMap.movementPhase();
        worldMap.feedingPhase(energyPerGrass);
        worldMap.reproductionPhase(reproductionEnergyCost);
        worldMap.growGrass(grassGrownPerDay);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public UUID getSimulationID() {
        return simulationID;
    }

    public AbstractWorldMap getWorldMap() {
        return worldMap;
    }

    public int getDaysPassed() {
        return daysPassed;
    }

    public void subscribe(SimulationChangeListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(SimulationChangeListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (SimulationChangeListener listener : listeners) {
            listener.update(this.simulationID, this.worldMap);
        }
    }
}
