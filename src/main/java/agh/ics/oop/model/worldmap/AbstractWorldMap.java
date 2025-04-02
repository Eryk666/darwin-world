package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.*;
import agh.ics.oop.model.animal.Animal;

import java.util.*;

public abstract class AbstractWorldMap {
    protected final Boundary mapBoundary;
    protected final List<Animal> animals;
    protected final List<Animal> deadAnimals;
    protected final Map<Vector2d, Grass> grasses;
    protected boolean paused;
    protected int reproductionEnergyMinimum;

    public AbstractWorldMap(Boundary mapBoundary, int reproductionEnergyMinimum) {
        this.mapBoundary = mapBoundary;
        this.animals = new ArrayList<>();
        this.grasses = new HashMap<>();
        this.deadAnimals = new ArrayList<>();
        this.reproductionEnergyMinimum = reproductionEnergyMinimum;
    }

    public List<Animal> getDeadAnimals(){
        return this.deadAnimals;
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
                Vector2d animalPosition = animal.getPosition();
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
                    animal.setPosition(animalPosition);
                }
            } catch (GeneOutOfRangeException e) {
                System.out.println(e.getMessage());
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


    public abstract ArrayList<Vector2d> generatePreferredGrassSpaces();

    protected void growGrassOn(ArrayList<Vector2d> positions, double amount){
        Collections.shuffle(positions);
        //3. add grass to first 80%*grassAmount or maximum possible grass spaces
        Iterator<Vector2d> positionsIterator = positions.iterator();
        int addedGrass = 0;
        while (positionsIterator.hasNext() && addedGrass < amount){
            Vector2d currPos = positionsIterator.next();
            this.grasses.put(currPos,new Grass(currPos));
            addedGrass++;
        }
    }

}
