package agh.ics.oop.model;


import java.util.*;



public class EarthMap {
    private final int energyPerGrass;

    private final int energyPerParentInSex;

    private final int grassGrowAmount;

    private final Map<Vector2d,Animal> animals;

    private final Map<Vector2d,Grass> grasses;

    private final Boundary mapBoundary;



    public EarthMap(List<Animal> animals,int energyPerGrass, int energyPerParentInSex, int grassGrowAmount, Boundary mapBoundary) {
        this.animals = new HashMap<>();
        animals.forEach(animal -> this.animals.put(animal.getPosition(),animal));
        this.energyPerGrass = energyPerGrass;
        this.energyPerParentInSex = energyPerParentInSex;
        this.grassGrowAmount = grassGrowAmount;
        this.mapBoundary = mapBoundary;
        this.grasses = new HashMap<>();
        growGrass();
    }
    //public are debatable here
    public void spawnAnimal(Vector2d position, int energy, List<Integer> genes){
        Animal pet = new Animal(position,energy,genes);
        animals.put(position,pet);
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
        //Now to the fun stuff 😳
        spawnAnimal(nonBinaryParent1.getPosition(),this.energyPerParentInSex*2, genes);
        nonBinaryParent1.setEnergy(nonBinaryParent1.getEnergy()-energyPerParentInSex);
        nonBinaryParent2.setEnergy(nonBinaryParent2.getEnergy()-energyPerParentInSex);
    }

    public void growGrass(){
        Random r = new Random();
        //calculating equator
        int divider = mapBoundary.upperRight().y()-mapBoundary.bottomLeft().y()/5;
        Boundary equator = new Boundary(new Vector2d(this.mapBoundary.bottomLeft().x(),
                                            this.mapBoundary.bottomLeft().y()+divider*2)
                                      ,new Vector2d(this.mapBoundary.upperRight().x(),
                                            this.mapBoundary.bottomLeft().y()+divider*3));
        for (int i = 0; i < this.grassGrowAmount; i++) {
            Vector2d position;
            if(i % 5 == 0){
                //outside equator
                if(r.nextBoolean()){
                    position = new Vector2d(r.nextInt(this.mapBoundary.bottomLeft().x(),this.mapBoundary.upperRight().x()),
                                            r.nextInt(this.mapBoundary.bottomLeft().y(),equator.bottomLeft().y()));
                }else{
                    position = new Vector2d(r.nextInt(this.mapBoundary.bottomLeft().x(),this.mapBoundary.upperRight().x()),
                                            r.nextInt(equator.upperRight().y(),this.mapBoundary.upperRight().y()));
                }
            }else{
                //inside equator
                position = new Vector2d(r.nextInt(equator.bottomLeft().x(),equator.upperRight().x()),
                        r.nextInt(equator.bottomLeft().y(),equator.upperRight().y()));
            }
            this.grasses.put(position, new Grass(position));
        }
    }

    //opposite of sex
    public void GrimReaper(){
        this.animals.forEach((position,animal)->{
            if(animal.getEnergy() <= 0){
                this.animals.remove(position,animal);
            }
        });
    }

    public void moveAnimals(){
        this.animals.forEach((position,animal) -> {
            try {
                animal.move();
            }catch (GeneOutOfRangeException ex){
                System.err.println(ex);
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
        //todo:
        //1. create order in positions (best by determineStrongestAnimal)
        //2. use sex function in pairs of animals (1,2),(3,4),...

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
        sortedAnimals.forEach((position,list) -> {
            //do point nr 2.
        });
    }
}
