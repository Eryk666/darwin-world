package agh.ics.oop.model.worldmap;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.GeneOutOfRangeException;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animal.Animal;

import java.util.*;

public abstract class AbstractWorldMap {
    protected final Boundary mapBoundary;
    protected final Map<Vector2d, Animal> animals;
    protected final Map<Vector2d, Grass> grasses;

    public AbstractWorldMap(Boundary mapBoundary) {
        this.mapBoundary = mapBoundary;
        this.animals = new HashMap<>();
        this.grasses = new HashMap<>();
    }

    // Getters
    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    public Boundary getMapBoundary() {
        return mapBoundary;
    }

    public Map<Vector2d, Animal> getAnimals() {
        return animals;
    }

    //public are debatable here
    public void placeAnimal(Animal animal){
        animals.put(animal.getPosition(), animal);
    }

    //opposite of sex
    public void removeDeadAnimals(){
        this.animals.forEach((position,animal)->{
            if(animal.getEnergy() <= 0){
                this.animals.remove(position,animal);
            }
        });
    }

    public void movementPhase(){
        this.animals.forEach((position,animal) -> {
            try {
                this.animals.remove(position,animal);
                animal.move();
                //fix caused by EarthMap variant
                if (animal.getPosition().x() > mapBoundary.upperRight().x()){
                    animal.setPosition(new Vector2d(mapBoundary.bottomLeft().x(),animal.getPosition().y()));
                }else if (animal.getPosition().x() < mapBoundary.bottomLeft().x()){
                    animal.setPosition(new Vector2d(mapBoundary.upperRight().x(),animal.getPosition().y()));
                }
                if (animal.getPosition().y() > mapBoundary.upperRight().y() ||
                        animal.getPosition().y() < mapBoundary.bottomLeft().y()){
                    animal.rotateAnimal(4);
                }
                this.animals.put(animal.getPosition(), animal);
            }catch (GeneOutOfRangeException ex){
                ex.printStackTrace();
            }
        });
    }

    //mmm yummy
    public void feedingPhase(int energyPerGrass){
        //finding the strongest animal for each position
        Map<Vector2d, Animal> strongestAnimals = new HashMap<>();
        this.animals.forEach((position,animal) -> {
            if(strongestAnimals.get(position) == null){
                strongestAnimals.put(position,animal);
            } else if (determineStrongestAnimal(strongestAnimals.get(position),animal)) {
                strongestAnimals.remove(position);
                strongestAnimals.put(position,animal);
            }
        });

        //attempting to eat grass for strongest animals
        strongestAnimals.forEach((position,animal)->{
            //checking if there is any grass on that position
            if(this.grasses.get(position) != null){
                animal.eatGrass(energyPerGrass);
                //removing the grass
                this.grasses.remove(position);
            }
        });
    }

    //returns True if Pretender should take the spot as the strongest
    public boolean determineStrongestAnimal(Animal rat, Animal ratPretender){
        //energy
        if(rat.getEnergy() > ratPretender.getEnergy()){
            return false;
        } else if (rat.getEnergy() < ratPretender.getEnergy()) {
            return true;
        }
        //age
        if(rat.getAge() > ratPretender.getAge()){
            return false;
        } else if (rat.getAge() < ratPretender.getAge()){
            return true;
        }
        //children Amount
        if(rat.getChildrenAmount() > ratPretender.getChildrenAmount()){
            return false;
        } else if (rat.getChildrenAmount() < ratPretender.getChildrenAmount()) {
            return true;
        }
        //random
        return (new Random()).nextBoolean();
    }

    public void reproductionPhase(int reproductionEnergyCost, int reproductionEnergyMinimum){
        Map<Vector2d, ArrayList<Animal>> sortedAnimals = new HashMap<>();
        this.animals.forEach((position,animal) -> {
            if (sortedAnimals.get(position) == null) {
                sortedAnimals.put(position,new ArrayList<>());
                sortedAnimals.get(position).add(animal);
            } else {
                for(int i = 0; i < sortedAnimals.get(position).size(); i++) {
                    if (determineStrongestAnimal(sortedAnimals.get(position).get(i), animal)) {
                        sortedAnimals.get(position).add(i, animal);
                        break;
                    }
                }
            }
        });

        //use reproduce function in pairs of animals (1,2),(3,4),...
        sortedAnimals.forEach((position,list) -> {
            Iterator<Animal> iterator = list.iterator();
            while(iterator.hasNext()){
                Animal rat1 = iterator.next();
                if(!iterator.hasNext()){
                    break;
                }
                Animal rat2 = iterator.next();
                if (rat2.getEnergy() < reproductionEnergyMinimum || rat1.getEnergy() < reproductionEnergyMinimum) {
                    break;
                }
                placeAnimal(rat1.reproduce(rat2, reproductionEnergyCost));
            }
        });
    }

    public void growGrass(int grassAmount) {}
}
