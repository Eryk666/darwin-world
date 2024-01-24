package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.*;
import agh.ics.oop.model.animal.Animal;

import java.util.*;

public abstract class AbstractWorldMap {
    protected final Boundary mapBoundary;
    protected final List<Animal> animals;
    protected final List<Animal> deadAnimals;
    protected final Map<Vector2d, Grass> grasses;
    protected final ArrayList<MapChangeListener> observers;
    protected final int reproductionEnergyMinimum;
    protected boolean paused;
    public AbstractWorldMap(Boundary mapBoundary, int reproductionEnergyMinimum) {
        this.mapBoundary = mapBoundary;
        this.animals = new ArrayList<>();
        this.grasses = new HashMap<>();
        this.observers = new ArrayList<>();
        this.deadAnimals = new ArrayList<>();
        this.reproductionEnergyMinimum = reproductionEnergyMinimum;
    }

    public int getReproductionEnergyMinimum(){
        return  this.reproductionEnergyMinimum;
    }

    public void setPaused(boolean paused){
        this.paused = paused;
    }
    public boolean getPaused(){
        return this.paused;
    }

    public Boundary getMapBoundary() {
        return mapBoundary;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    public void placeAnimal(Animal animal) {
        animals.add(animal);
    }

    public void growGrass(int grassAmount) {}

    public void removeDeadAnimals(int daysPassed) {
        this.animals.forEach(animal -> {
            if(animal.getEnergy() <= 0){
                deadAnimals.add(animal);
                animal.setDayOfDeath(daysPassed);
            }
        });
        this.animals.removeIf(animal -> animal.getEnergy() <= 0);
    }

    public void movementPhase() {
        this.animals.forEach(animal -> {
            try {
                animal.move();

                // Fix caused by EarthMap variant
                if (animal.getPosition().x() > mapBoundary.upperRight().x()) {
                    animal.setPosition(new Vector2d(mapBoundary.bottomLeft().x(), animal.getPosition().y()));
                } else if (animal.getPosition().x() < mapBoundary.bottomLeft().x()) {
                    animal.setPosition(new Vector2d(mapBoundary.upperRight().x(), animal.getPosition().y()));
                }

                if (
                    animal.getPosition().y() > mapBoundary.upperRight().y() ||
                    animal.getPosition().y() < mapBoundary.bottomLeft().y()
                ) {
                    animal.rotateAnimal(4);
                }
            } catch (GeneOutOfRangeException ex){
                ex.printStackTrace();
            }
        });
    }

    public void feedingPhase(int energyPerGrass){

        Map<Vector2d, Animal> strongestAnimals = getStrongestAnimals();

        // Attempting to eat grass for strongest animals
        strongestAnimals.forEach((position, animal) -> {
            // Checking if there is any grass on that position
            if (this.grasses.get(position) != null) {
                animal.eatGrass(energyPerGrass);
                // Removing the grass
                this.grasses.remove(position);
            }
        });
    }

    public Map<Vector2d,Animal> getStrongestAnimals(){
        // Finding the strongest animal for each position
        Map<Vector2d, Animal> strongestAnimals = new HashMap<>();

        this.animals.forEach(animal -> {
            Vector2d position = animal.getPosition();
            Animal currentStrongestAnimal = strongestAnimals.get(position);

            if (currentStrongestAnimal == null) {
                strongestAnimals.put(position, animal);
            } else if (animal.takesPrecedence(currentStrongestAnimal)) {
                strongestAnimals.put(position, animal);
            }
        });
        return strongestAnimals;
    }

    public void reproductionPhase(int reproductionEnergyCost) {
        Map<Vector2d, ArrayList<Animal>> sortedAnimals = new HashMap<>();

        this.animals.forEach(animal -> {
            Vector2d position = animal.getPosition();

            if (sortedAnimals.get(position) == null) {
                sortedAnimals.put(position, new ArrayList<>());
                sortedAnimals.get(position).add(animal);
            } else {
                for (int i = 0; i < sortedAnimals.get(position).size(); i++) {
                    if (animal.takesPrecedence(sortedAnimals.get(position).get(i))) {
                        sortedAnimals.get(position).add(i, animal);
                        break;
                    }
                }
            }
        });

        // Use reproduce function in pairs of animals (1,2),(3,4),...
        sortedAnimals.forEach((position, animalsAtPosition) -> {
            Iterator<Animal> animalsAtPositionIterator = animalsAtPosition.iterator();
            while (animalsAtPositionIterator.hasNext()) {
                Animal rat1 = animalsAtPositionIterator.next();
                if (!animalsAtPositionIterator.hasNext()) {
                    break;
                }
                Animal rat2 = animalsAtPositionIterator.next();
                if (rat2.getEnergy() < reproductionEnergyMinimum || rat1.getEnergy() < reproductionEnergyMinimum) {
                    break;
                }
                placeAnimal(rat1.reproduce(rat2, reproductionEnergyCost));
            }
        });
    }

    public void registerObserver(MapChangeListener observer){
        this.observers.add(observer);
    }

    public void unregisterObserver(MapChangeListener observer){
        this.observers.remove(observer);
    }

    public synchronized void updateMap(){
        for (MapChangeListener observer : this.observers){
            observer.mapChanged(this,"position");
        }
    }

    public int countEmptySpaces(){
        int returnValue = 0;
        for (int i = this.mapBoundary.bottomLeft().x(); i < this.mapBoundary.upperRight().x() ; i++) {
            for (int j = this.mapBoundary.bottomLeft().y(); j < this.mapBoundary.upperRight().y() ; j++) {
                Vector2d position = new Vector2d(i,j);
                boolean isEmpty = true;
                for (Animal animal : animals) {
                    if(animal.getPosition().equals(position)){
                        isEmpty = false;
                        break;
                    }
                }
                if(grasses.containsKey(position)){
                    isEmpty = false;
                }
                if(isEmpty){
                    returnValue++;
                }
            }
        }
        return returnValue;
    }

    public List<Integer> commonGenes(){
        Map<List<Integer>, Integer> geneCounts = new HashMap<>();

        for (Animal animal : animals) {
            List<Integer> genes = animal.getGenes();

            geneCounts.put(genes, geneCounts.getOrDefault(genes, 0) + 1);
        }

        // Step 4: Find the gene list with the maximum occurrence
        List<Integer> mostOccurringGenes = null;
        int maxOccurrences = 0;

        for (Map.Entry<List<Integer>, Integer> entry : geneCounts.entrySet()) {
            if (entry.getValue() > maxOccurrences) {
                maxOccurrences = entry.getValue();
                mostOccurringGenes = entry.getKey();
            }
        }
        return mostOccurringGenes;
    }

    public double averageEnergy(){
        double animalEnergy = 0;
        for (Animal animal : this.animals) {
            animalEnergy += animal.getEnergy();
        }
        return animalEnergy/this.animals.size();
    }

    public double averageDeadAge(){
        double animalAge = 0;
        for (Animal deadAnimal : deadAnimals) {
            animalAge += deadAnimal.getAge();
        }
        return  animalAge/deadAnimals.size();
    }

    public double averageAlivePredecessors(){
        double animalChildren = 0;
        for (Animal animal : this.animals) {
            animalChildren += animal.getDescendantsAmount(new ArrayList<>());
        }
        return animalChildren/this.animals.size();
    }

    public abstract ArrayList<Vector2d> generatePreferredGrassSpaces();

}
