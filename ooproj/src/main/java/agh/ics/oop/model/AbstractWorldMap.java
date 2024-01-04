package agh.ics.oop.model;

import java.util.*;

public abstract class AbstractWorldMap {
    protected final int energyPerGrass;
    protected final int energyPerParentInSex;
    protected final Map<Vector2d,Animal> animals;
    protected final Map<Vector2d,Grass> grasses;
    protected final Boundary mapBoundary;

    public AbstractWorldMap(
            List<Animal> animals,
            int energyPerGrass,
            int energyPerParentInSex,
            int initialGrowthAmount,
            Boundary mapBoundary
    ) {
        this.animals = new HashMap<>();
        animals.forEach(animal -> this.animals.put(animal.getPosition(),animal));
        this.energyPerGrass = energyPerGrass;
        this.energyPerParentInSex = energyPerParentInSex;
        this.mapBoundary = mapBoundary;
        this.grasses = new HashMap<>();
        growGrass(initialGrowthAmount);
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

    public int getEnergyPerGrass() {
        return energyPerGrass;
    }

    public int getEnergyPerParentInSex() {
        return energyPerParentInSex;
    }

    //public are debatable here
    public void spawnAnimal(Vector2d position, int energy, List<Integer> genes){
        Animal rat = new Animal(position,energy,genes);
        animals.put(position,rat);
    }

    //I have no idea how this works, I study at AGH ComputerScience
    public void sex(Animal nonBinaryParent1, Animal nonBinaryParent2){
        //first is stronger!!!!!!!!!!!
        Random r = new Random();

        //ratio
        int ratio = (nonBinaryParent1.getEnergy()/nonBinaryParent2.getEnergy())*nonBinaryParent1.getGenes().size();

        //left or right genes
        List<Integer> genes = new ArrayList<>();
        //test if left and right give the same amounts of genes !!!!!!!!!!!!!!!!!!!!!!!
        if(r.nextBoolean()){
            //left
            for (int i = 0; i < ratio; i++) {
                genes.add(nonBinaryParent1.getGenes().get(i));
            }
            for (int i = ratio; i < nonBinaryParent1.getGenes().size(); i++){
                genes.add(nonBinaryParent2.getGenes().get(i));
            }
        }else{
            //right
            for (int i = 0; i < nonBinaryParent1.getGenes().size() - ratio; i++) {
                genes.add(nonBinaryParent2.getGenes().get(i));
            }
            for (int i = nonBinaryParent1.getGenes().size() - ratio; i < nonBinaryParent1.getGenes().size(); i++){
                genes.add(nonBinaryParent1.getGenes().get(i));
            }
        }

        //mutations
        int mutationAmount = r.nextInt(nonBinaryParent1.getGenes().size());
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < nonBinaryParent1.getGenes().size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        for (int i = 0; i < mutationAmount; i++){
            genes.set(indices.get(i),r.nextInt(8));
        }
        //Now to the fun stuff
        spawnAnimal(nonBinaryParent1.getPosition(),this.energyPerParentInSex*2, genes);
        nonBinaryParent1.setEnergy(nonBinaryParent1.getEnergy()-energyPerParentInSex);
        nonBinaryParent2.setEnergy(nonBinaryParent2.getEnergy()-energyPerParentInSex);
    }

    //opposite of sex
    public void grimReaper(){
        this.animals.forEach((position,animal)->{
            if(animal.getEnergy() <= 0){
                this.animals.remove(position,animal);
            }
        });
    }

    public void moveAnimals(){
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
    public void attemptEatGrass(){
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
                animal.eatGrass(this.energyPerGrass);
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

    public void attemptSex(){
        Map<Vector2d, ArrayList<Animal>> sortedAnimals = new HashMap<>();
        this.animals.forEach((position,animal) -> {
            if(sortedAnimals.get(position) == null){
                sortedAnimals.put(position,new ArrayList<>());
                sortedAnimals.get(position).add(animal);
            }else{
                for(int i = 0; i < sortedAnimals.get(position).size(); i++) {
                    if (determineStrongestAnimal(sortedAnimals.get(position).get(i), animal)) {
                        sortedAnimals.get(position).add(i, animal);
                        break;
                    }
                }
            }
        });
        //use sex function in pairs of animals (1,2),(3,4),...
        sortedAnimals.forEach((position,list) -> {
            Iterator<Animal> iterator = list.iterator();
            while(iterator.hasNext()){
                Animal rat1 = iterator.next();
                if(!iterator.hasNext()){
                    break;
                }
                Animal rat2 = iterator.next();
                sex(rat1,rat2);
            }
        });
    }

    public void growGrass(int grassAmount) {};
}
