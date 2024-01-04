package agh.ics.oop.model;


import java.util.List;
import java.util.Random;

public class Animal {
    private Vector2d position;
    private MapDirection direction;
    private int energy;
    private final List<Integer> genes;
    private int age;
    private int currentGene;
    private int childrenAmount;

    public Animal(Vector2d position, int energy, List<Integer> genes) {
        this.position = position;
        this.energy = energy;
        this.genes = genes;
        this.direction = MapDirection.NORTH;
        this.age = 0;
        this.childrenAmount = 0;
        rotateAnimal((int)(Math.random()*8)); //random number between 0 and 7


        this.currentGene = (new Random()).nextInt(this.genes.size());
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

    public int getCurrentGene() {
        return currentGene;
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

    public void setCurrentGene(int currentGene) {
        this.currentGene = currentGene;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setChildrenAmount(int childrenAmount){
        this.childrenAmount = childrenAmount;
    }

    //method used to rotate the animal clockwise
    public void rotateAnimal(int times){
        MapDirection tmpDirection = this.direction;
        for(int i = 0 ; i < times; i++){
            tmpDirection = tmpDirection.next();
        }
        this.direction = tmpDirection;
    }

    //method that used to rotate animal and move him by unit of direction
    public void move() throws GeneOutOfRangeException {

        //getting gene
        int gene = getGene();

        //rotation
        if(gene >= 0 && gene <= 7) {
            rotateAnimal(gene);
        }else{
            throw new GeneOutOfRangeException(gene);
        }

        //movement (variant "Kula ziemska" accounted in map class)
        this.position = this.position.add(this.direction.toUnitVector());

        //energy usage
        this.energy--;

        //aging
        this.age++;

        //changing the gene
        nextGene();
    }

    public int getGene(){
        return this.genes.get(this.currentGene);
    }

    public void nextGene(){
        this.currentGene++;
        if(this.currentGene == genes.size()){
            this.currentGene = 0;
        }
    }

    public void eatGrass(int energy){
        this.energy += energy;
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
}
