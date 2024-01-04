package agh.ics.oop.model;

import java.util.List;

public interface MapInterface {
    void spawnAnimal(Vector2d position, int energy, List<Integer> genes);
    void growGrass(int grassAmount);

    void attemptSex();
    void sex(Animal nonBinaryParent1, Animal nonBinaryParent2);
    void grimReaper();

    void moveAnimals();

    void attemptEatGrass();

    boolean determineStrongestAnimal(Animal rat, Animal ratPretender);
}
