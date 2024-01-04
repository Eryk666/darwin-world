package agh.ics.oop.model;


import java.util.*;



public class EarthMap implements MapInterface {
    private final int energyPerGrass;

    private final int energyPerParentInSex;

    private final Map<Vector2d,Animal> animals;

    private final Map<Vector2d,Grass> grasses;

    private final Boundary mapBoundary;


    public EarthMap(List<Animal> animals,int energyPerGrass, int energyPerParentInSex,
                    int initialGrowthAmount, Boundary mapBoundary) {
        this.animals = new HashMap<>();
        animals.forEach(animal -> this.animals.put(animal.getPosition(),animal));
        this.energyPerGrass = energyPerGrass;
        this.energyPerParentInSex = energyPerParentInSex;
        this.mapBoundary = mapBoundary;
        this.grasses = new HashMap<>();
        growGrass(initialGrowthAmount);
    }

    //Usable getters/setters


    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }
    public Boundary getMapBoundary() {
        return mapBoundary;
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

    public void growGrass(int grassAmount){
        //1. Generate equator area
        int divider = mapBoundary.upperRight().y()-mapBoundary.bottomLeft().y()/5;
        Boundary equator = new Boundary(new Vector2d(this.mapBoundary.bottomLeft().x(),
                                            this.mapBoundary.bottomLeft().y()+divider*2)
                                      ,new Vector2d(this.mapBoundary.upperRight().x(),
                                            this.mapBoundary.bottomLeft().y()+divider*3));

        //2. Create Array of non grass spaces inside equator
        int width = this.mapBoundary.upperRight().x()-this.mapBoundary.bottomLeft().x()+1;
        int equatorHeight = equator.upperRight().y()-equator.bottomLeft().y()+1;
        ArrayList<Vector2d> nonGrassEquatorPositions = new ArrayList<>();
        for (int i = 0; i < width; i++){
            for (int j =0; j < equatorHeight; j++){
                Vector2d currPosition = new Vector2d(this.mapBoundary.bottomLeft().x()+i,
                                                                equator.bottomLeft().y()+j);
                if(this.grasses.get(currPosition) == null ){
                    nonGrassEquatorPositions.add(currPosition);
                }
            }
        }
        //3. Shuffle it
        Collections.shuffle(nonGrassEquatorPositions);
        //4. add grass to first 80%*grassAmount or maximum possible grass spaces from the Array
        Iterator<Vector2d> equatorIterator = nonGrassEquatorPositions.iterator();
        int grassAdded = 0;
        while (equatorIterator.hasNext() && grassAdded < (grassAmount*8)/10){
            Vector2d currentGrass = equatorIterator.next();
            this.grasses.put(currentGrass,new Grass(currentGrass));
            grassAdded++;
        }

        //5. Create new Array of non grass spaces outside equator
        int lowerHeight = equator.bottomLeft().y()-this.mapBoundary.bottomLeft().y()+1;
        int upperHeight = this.mapBoundary.upperRight().y()-equator.bottomLeft().y()+1;
        ArrayList<Vector2d> nonGrassNonEquatorPositions = new ArrayList<>();
        for (int i = 0; i < width; i++){
            for (int j = 0; j < lowerHeight; j++) {
                Vector2d currPosition = new Vector2d(this.mapBoundary.bottomLeft().x() + i,
                        this.mapBoundary.bottomLeft().y() + j);
                if (this.grasses.get(currPosition) == null) {
                    nonGrassNonEquatorPositions.add(currPosition);
                }
            }
            for (int j = 0; j < upperHeight; j++) {
                Vector2d currPosition = new Vector2d(this.mapBoundary.bottomLeft().x() + i,
                        equator.upperRight().y() + j);
                if (this.grasses.get(currPosition) == null) {
                    nonGrassNonEquatorPositions.add(currPosition);
                }
            }
        }
        //6. Shuffle it
        Collections.shuffle(nonGrassNonEquatorPositions);
        //7. add grass to first 20%*grassAmount or maximum possible grass spaces from the Array
        Iterator<Vector2d> nonEquatorIterator = nonGrassNonEquatorPositions.iterator();
        grassAdded = 0;
        while(nonEquatorIterator.hasNext() && grassAdded < (grassAmount*8)/10){
            Vector2d currPosition = nonEquatorIterator.next();
            this.grasses.put(currPosition,new Grass(currPosition));
            grassAdded++;
        }
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


}
