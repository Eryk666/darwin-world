package agh.ics.oop.model;


import java.util.List;

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
        this.currentGene = (int)(Math.random()*genes.size()*2) - genes.size();
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


    //method used to rotate the animal clockwise
    private void rotateAnimal(int times){
        MapDirection tmpDirection = this.direction;
        for(int i = 0 ; i < times; i++){
            tmpDirection = tmpDirection.next();
        }
        this.direction = tmpDirection;
    }

    //method that  used to rotate animal and move him by unit of direction
    public void move(){
        //rotation
        if(this.currentGene < 0){
            //negative numbers
            rotateAnimal(this.genes.get((currentGene*(-1)) - 1));
        }else{
            //positive numbers
            rotateAnimal(this.genes.get(this.currentGene));
        }

        //movement (variant "Kula ziemska" accounted in map class)
        this.position = this.position.add(this.direction.toUnitVector());

        //energy usage
        this.energy--;

        //changing the gene
        this.currentGene++;
        if(this.currentGene == genes.size() + 1){
            this.currentGene = this.currentGene*(-1);
        }
    }

}
