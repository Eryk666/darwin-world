package agh.ics.oop.model.animal;

import agh.ics.oop.model.Vector2d;

import java.util.List;
import java.util.Random;

public class FunkyAnimal extends Animal{
    public FunkyAnimal(Vector2d position, int energy, List<Integer> genes,int minMutationAmount, int maxMutationAmount) {
        super(position, energy, genes, minMutationAmount, maxMutationAmount);
        //negative index means that animal is executing genes in reverse order
        //for example: -7 means that is on sixth (because of index 0) gene and going left
        //yep math.random is ass because of 0 my beloved number
        //replaced with nextInt
        setCurrentGeneIndex((new Random()).nextInt(this.getGenes().size()*2) - this.getGenes().size());
    }

    @Override
    public int getCurrentGene() {
        int gene;

        if (currentGeneIndex < 0) {
            // Negative numbers
            gene = genes.get((currentGeneIndex * (-1)) - 1);
        } else {
            // Positive numbers
            gene = genes.get(currentGeneIndex);
        }

        return gene;
    }

    @Override
    public void nextGene() {
        currentGeneIndex++;
        if(currentGeneIndex == genes.size()) {
            currentGeneIndex *= (-1);
        }
    }

    @Override
    public Animal reproduce(Animal mate, int reproductionEnergyCost) {
        List<Integer> genes = determineBabyGenes(mate);
        Animal babyAnimal = new FunkyAnimal(this.position, reproductionEnergyCost * 2, genes,
                                                        this.minMutationAmount, this.maxMutationAmount);

        this.energy -= reproductionEnergyCost;
        mate.energy -= reproductionEnergyCost;

        this.children.add(babyAnimal);
        mate.children.add(babyAnimal);

        return babyAnimal;
    }
}
