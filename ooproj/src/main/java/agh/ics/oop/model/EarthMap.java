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
        if(r.nextInt(2) == 1){
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
        //calculating equator
        int divider = mapBoundary.upperRight().y()-mapBoundary.bottomLeft().y()/5;
        Boundary middle = new Boundary(new Vector2d(this.mapBoundary.bottomLeft().x(),
                                            this.mapBoundary.bottomLeft().y()+divider*2)
                                      ,new Vector2d(this.mapBoundary.upperRight().x(),
                                            this.mapBoundary.bottomLeft().y()+divider*3));
        //finish - DEFAULT CONFIGURATION
    }
}
