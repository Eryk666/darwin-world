package agh.ics.oop.model;

import java.util.List;
import java.util.Random;

public class FunkyAnimal extends Animal{
    public FunkyAnimal(Vector2d position, int energy, List<Integer> genes) {
        super(position, energy, genes);
        //negative index means that animal is executing genes in reverse order
        //for example: -7 means that is on sixth (because of index 0) gene and going left
        //yep math.random is ass because of 0 my beloved number
        //replaced with nextInt
        setCurrentGene((new Random()).nextInt(this.getGenes().size()*2) - this.getGenes().size());
    }

    @Override
    public int getGene() {
        int gene;
        if(this.getCurrentGene() < 0){
            //negative numbers
            gene = this.getGenes().get((this.getCurrentGene()*(-1)) - 1);
        }else{
            //positive numbers
            gene = this.getGenes().get(this.getCurrentGene());
        }
        return gene;
    }

    @Override
    public void nextGene() {
        this.setCurrentGene(this.getCurrentGene()+1);
        if(this.getCurrentGene() == getGenes().size()){
            this.setCurrentGene(this.getCurrentGene()*(-1));
        }
    }
}
