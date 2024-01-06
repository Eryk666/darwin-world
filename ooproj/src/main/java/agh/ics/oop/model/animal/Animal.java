package agh.ics.oop.model.animal;


import agh.ics.oop.model.GeneOutOfRangeException;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;

import java.util.*;

public class Animal {
    protected Vector2d position;
    protected MapDirection direction;
    protected int energy;
    protected final List<Integer> genes;
    protected int currentGeneIndex;
    protected int age;
    protected int childrenAmount;

    public Animal(Vector2d position, int energy, List<Integer> genes) {
        this.position = position;
        this.direction = MapDirection.NORTH;
        this.energy = energy;
        this.genes = genes;
        this.currentGeneIndex = (new Random()).nextInt(this.genes.size());
        this.age = 0;
        this.childrenAmount = 0;
        rotateAnimal((int)(Math.random()*8)); // Random number between 0 and 7
    }

    //getters&setters
    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getDirection() {
        return direction;
    }

    public int getEnergy() {
        return energy;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public int getCurrentGene(){
        return genes.get(currentGeneIndex);
    }

    public int getCurrentGeneIndex() {
        return currentGeneIndex;
    }

    public int getAge(){
        return age;
    }

    public int getChildrenAmount(){
        return childrenAmount;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setCurrentGeneIndex(int currentGeneIndex) {
        this.currentGeneIndex = currentGeneIndex;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setChildrenAmount(int childrenAmount){
        this.childrenAmount = childrenAmount;
    }

    // Rotates animal clockwise
    public void rotateAnimal(int times) {
        MapDirection tmpDirection = this.direction;
        for(int i = 0 ; i < times; i++){
            tmpDirection = tmpDirection.next();
        }
        this.direction = tmpDirection;
    }

    // Rotates animal and moves it
    public void move() throws GeneOutOfRangeException {
        // Get gene
        int gene = getCurrentGene();

        // Rotate animal
        if (gene >= 0 && gene <= 7) {
            rotateAnimal(gene);
        } else {
            throw new GeneOutOfRangeException(gene);
        }

        // Move animal (variant "Kula ziemska" accounted in map class)
        this.position = this.position.add(this.direction.toUnitVector());

        // Energy usage
        this.energy--;

        // Aging
        this.age++;

        // Change the gene
        nextGene();
    }

    public void nextGene(){
        currentGeneIndex++;
        if (currentGeneIndex == genes.size()) {
            currentGeneIndex = 0;
        }
    }

    public void eatGrass(int energy){
        this.energy += energy;
    }

    public Animal reproduce(Animal mate, int reproductionEnergyCost) {
        List<Integer> genes = determineBabyGenes(mate);
        Animal babyAnimal = new Animal(this.position, reproductionEnergyCost * 2, genes);

        this.energy -= reproductionEnergyCost;
        mate.energy -= reproductionEnergyCost;

        return babyAnimal;
    }

    protected List<Integer> determineBabyGenes(Animal mate) {
        Random random = new Random();
        List<Integer> genes = new ArrayList<>();

        //ratio
        int ratio = (this.energy / mate.energy) * this.genes.size();

        //test if left and right give the same amounts of genes !!!!!!!!!!!!!!!!!!!!!!!
        if (random.nextBoolean()) {
            //left
            for (int i = 0; i < ratio; i++) {
                genes.add(this.genes.get(i));
            }
            for (int i = ratio; i < this.genes.size(); i++) {
                genes.add(mate.genes.get(i));
            }
        } else {
            //right
            for (int i = 0; i < this.genes.size() - ratio; i++) {
                genes.add(mate.genes.get(i));
            }
            for (int i = this.genes.size() - ratio; i < this.genes.size(); i++){
                genes.add(this.genes.get(i));
            }
        }

        //mutations
        int mutationAmount = random.nextInt(this.genes.size() + 1);
        ArrayList<Integer> indices = new ArrayList<>();

        for (int i = 0; i < this.genes.size(); i++) {
            indices.add(i);
        }

        Collections.shuffle(indices);

        for (int i = 0; i < mutationAmount; i++) {
            genes.set(indices.get(i), random.nextInt(8));
        }

        return genes;
    }

    @Override
    public String toString() {
        return switch (this.direction){
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        };
    }

    // True if this animal is stronger than the other,
    // False if its weaker and random True or False if their strength is equal
    public boolean takesPrecedence(Animal other) {
        // Compare energy
        if(this.energy > other.energy){
            return true;
        } else if (this.energy < other.energy) {
            return false;
        }

        // Compare age
        if(this.age > other.age){
            return true;
        } else if (this.age < other.age){
            return false;
        }

        // Compare children Amount
        if(this.childrenAmount > other.childrenAmount){
            return true;
        } else if (this.childrenAmount < other.childrenAmount) {
            return false;
        }

        // Choose random
        return (new Random()).nextBoolean();
    }
}
