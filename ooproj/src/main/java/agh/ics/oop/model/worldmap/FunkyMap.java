package agh.ics.oop.model.worldmap;



import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;

import java.util.*;

public class FunkyMap extends EarthMap{
    public FunkyMap(Boundary mapBoundary, int reproductionEnergyMinimum) {
        super(mapBoundary, reproductionEnergyMinimum);
    }

    //everything the same apart from the grass growing
    @Override
    public void growGrass(int grassAmount) {
        //in case of grasses being empty
        if(this.grasses.isEmpty()){
            super.growGrass(grassAmount);
            return;
        }
        //bounds
        Boundary bound = getMapBoundary();

        //more popular positions
        ArrayList<Vector2d> junglePositions = generatePreferredGrassSpaces();

        ArrayList<Vector2d> steppesPositions = new ArrayList<>();
        for(int x = bound.bottomLeft().x(); x < bound.upperRight().x(); x++){
            for(int y = bound.bottomLeft().y(); y < bound.upperRight().y(); y++){
                Vector2d currPos = new Vector2d(x,y);
                if (this.grasses.get(currPos) != null){
                    continue;
                }
                if (!isGrassNexTo(currPos,this.grasses)) {
                    steppesPositions.add(currPos);
                }
            }
        }

        //Grow 80% on good spaces and 20% on bad ones
        growGrassOn(junglePositions, grassAmount*0.8);

        growGrassOn(steppesPositions,grassAmount*0.2);
    }

    @Override
    public ArrayList<Vector2d> generatePreferredGrassSpaces() {
        if(this.grasses.isEmpty()){ return super.generatePreferredGrassSpaces(); }
        //just bruteforce it
        ArrayList<Vector2d> junglePositions = new ArrayList<>();
        for(int x = this.mapBoundary.bottomLeft().x(); x <= this.mapBoundary.upperRight().x(); x++){
            for(int y = this.mapBoundary.bottomLeft().y(); y <= this.mapBoundary.upperRight().y(); y++){
                Vector2d currPos = new Vector2d(x,y);
                if (this.grasses.get(currPos) != null){
                    continue;
                }
                if (isGrassNexTo(currPos,this.grasses)){
                    junglePositions.add(currPos);
                }
            }
        }
        return junglePositions;
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