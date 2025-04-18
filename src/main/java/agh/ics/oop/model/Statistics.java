package agh.ics.oop.model;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.worldmap.AbstractWorldMap;

import java.util.*;

public class Statistics {
    private final AbstractWorldMap worldMap;

    public Statistics(AbstractWorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public String countEmptySpaces(){
        int returnValue = 0;
        for (int i = this.worldMap.getMapBoundary().bottomLeft().x(); i < this.worldMap.getMapBoundary().upperRight().x() ; i++) {
            for (int j = this.worldMap.getMapBoundary().bottomLeft().y(); j < this.worldMap.getMapBoundary().upperRight().y() ; j++) {
                Vector2d position = new Vector2d(i,j);
                boolean isEmpty = true;
                for (Animal animal : this.worldMap.getAnimals()) {
                    if(animal.getPosition().equals(position)){
                        isEmpty = false;
                        break;
                    }
                }
                if(this.worldMap.getGrasses().containsKey(position)){
                    isEmpty = false;
                }
                if(isEmpty){
                    returnValue++;
                }
            }
        }
        return Integer.toString(returnValue);
    }

    public List<Integer> determineBestGenome(){
        Map<List<Integer>, Integer> geneCounts = new HashMap<>();

        for (Animal animal : this.worldMap.getAnimals()) {
            List<Integer> genes = animal.getGenes();

            List<Integer> immutableGenes = List.copyOf(genes);
            geneCounts.put(immutableGenes, geneCounts.getOrDefault(immutableGenes, 0) + 1);
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

    public String bestGenome(){
        List<Integer> mostOccurringGenes = determineBestGenome();
        assert mostOccurringGenes != null;
        return mostOccurringGenes.toString();
    }

    public String averageEnergy(){
        double animalEnergy = 0;
        for (Animal animal : this.worldMap.getAnimals()) {
            animalEnergy += animal.getEnergy();
        }
        return Double.toString(Round.round(animalEnergy/this.worldMap.getAnimals().size(),2));
    }

    public String averageDeadAge(){
        double animalAge = 0;
        for (Animal deadAnimal : this.worldMap.getDeadAnimals()) {
            animalAge += deadAnimal.getAge();
        }
        return Double.toString(Round.round(animalAge/this.worldMap.getDeadAnimals().size(),2));
    }

    public String averageAlivePredecessors(){
        double animalChildren = 0;
        for (Animal animal : this.worldMap.getAnimals()) {
            animalChildren += animal.getDescendantsAmount(new ArrayList<>());
        }
        return Double.toString(Round.round(animalChildren/this.worldMap.getAnimals().size(),2));
    }

    public String animalGenome(Animal animal){
        return "Animal genome: " + animal.getGenes().toString();
    }

    public String animalCurrentGenome(Animal animal){
        return "Animal energy: " + animal.getEnergy();
    }

    public String animalEnergy(Animal animal){
        return "Animal energy: " + animal.getEnergy();
    }

    public String animalPlantsEaten(Animal animal){
        return "Plants eaten amount: " + animal.getGrassEatenAmount();
    }

    public String animalPredecessors(Animal animal){
        return "Animal predecessors: " + animal.getDescendantsAmount(new ArrayList<>());
    }

    public String animalLifespan(Animal animal){
        return "Animal Lifespan: " + animal.getAge();
    }

    public String animalDayOfDeath(Animal animal){
        return "Day of death: " + animal.getDayOfDeath();
    }

    public String[] collectData() {
        return new String[] {
            Integer.toString(worldMap.getAnimals().size()),
            Integer.toString(worldMap.getGrasses().size()),
            countEmptySpaces(),
            bestGenome(),
            averageEnergy(),
            averageDeadAge(),
            averageAlivePredecessors()
        };
    }

    public String[] getColumnNames() {
        return new String[] {
            "Animals",
            "Grass",
            "Empty spaces",
            "Most popular genome",
            "Average energy",
            "Average dead animal lifespan",
            "Average alive animal predecessors"
        };
    }
}
