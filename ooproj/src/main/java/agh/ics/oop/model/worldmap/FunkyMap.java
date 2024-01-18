package agh.ics.oop.model.worldmap;



import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;

import java.util.*;

public class FunkyMap extends EarthMap{
    public FunkyMap(
            Boundary mapBoundary
    ) {
        super(mapBoundary);
    }

    //everything the same apart from the grass growing
    @Override
    public void growGrass(int grassAmount) {

        //get grass Map
        Map<Vector2d, Grass> grassMap = getGrasses();
        Boundary bound = getMapBoundary();
        //in case of grasses being empty
        if(grassMap.isEmpty()){
            super.growGrass(grassAmount);
        }
        //1. add all grass neighbouring tiles to array and add other tiles to the other array
        //doing two arrays at the same time because it's WAY easier
        ArrayList<Vector2d> junglePositions = new ArrayList<>();
        ArrayList<Vector2d> steppesPositions = new ArrayList<>();
        for(int x = bound.bottomLeft().x(); x < bound.upperRight().x(); x++){
            for(int y = bound.bottomLeft().y(); y < bound.upperRight().y(); y++){
                Vector2d currPos = new Vector2d(x,y);
                if (grassMap.get(currPos) != null){
                    continue;
                }
                if (isGrassNexTo(currPos,grassMap)){
                    junglePositions.add(currPos);
                }else{
                    steppesPositions.add(currPos);
                }
            }
        }
        //2. shuffle them randomly
        Collections.shuffle(junglePositions);
        Collections.shuffle(steppesPositions);
        //3. add grass to first 80%*grassAmount or maximum possible grass spaces
        Iterator<Vector2d> jungleIterator = junglePositions.iterator();
        int addedGrass = 0;
        while (jungleIterator.hasNext() && addedGrass < 0.8*grassAmount){
            Vector2d currPos = jungleIterator.next();
            grassMap.put(currPos,new Grass(currPos));
            addedGrass++;
        }
        //4. add 20%*grassAmount or maximum possible grass spaces
        Iterator<Vector2d> steppesIterator = steppesPositions.iterator();
        addedGrass = 0;
        while (steppesIterator.hasNext() && addedGrass < 0.2*grassAmount){
            Vector2d currPos = steppesIterator.next();
            grassMap.put(currPos,new Grass(currPos));
            addedGrass++;
        }
    }

    private boolean isGrassNexTo(Vector2d position, Map<Vector2d,Grass> grassMap){
        //no idea how to do this other way for now;
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if(i == 0 && j == 0){
                    continue;
                }
                if(grassMap.get(position.add(new Vector2d(i,j))) != null){
                    return true;
                }
            }
        }
        return false;
    }
}