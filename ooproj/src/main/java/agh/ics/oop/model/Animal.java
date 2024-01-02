package agh.ics.oop.model;


import java.util.List;
import java.util.Random;

public class Animal {
    private Vector2d position;
    private MapDirection direction;
    private int energy;
    private final List<Integer> genes;

    private int currentGene;

    public Animal(Vector2d position, int energy, List<Integer> genes) {
        this.position = position;
        this.energy = energy;
        this.genes = genes;
        this.direction = MapDirection.NORTH;
        rotateAnimal((int)(Math.random()*8)); //random number between 0 and 7

        //negative index means that animal is executing genes in reverse order
        //for example: -7 means that is on sixth (because of index 0) gene and going left
        //yep math.random is ass because of 0 my beloved number
        //replaced with nextInt
        this.currentGene = (new Random()).nextInt(this.genes.size()*2) - this.genes.size();
    }


    //getters
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

    //method used to rotate the animal clockwise
    private void rotateAnimal(int times){
        MapDirection tmpDirection = this.direction;
        for(int i = 0 ; i < times; i++){
            tmpDirection = tmpDirection.next();
        }
        this.direction = tmpDirection;
    }

    //method that  used to rotate animal and move him by unit of direction
    public void move() throws GeneOutOfRangeException {

        //getting gene
        int gene;
        if(this.currentGene < 0){
            //negative numbers
            gene = this.genes.get((this.currentGene*(-1)) - 1);
        }else{
            //positive numbers
            gene = this.genes.get(this.currentGene);
        }

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

        //changing the gene
        this.currentGene++;
        if(this.currentGene == genes.size()){
            this.currentGene = this.currentGene*(-1);
        }
    }

    public void eatGrass(int energy){
        this.energy += energy;
    }

}
