package agh.ics.oop.model.worldmap;


import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;

import java.util.*;



public class EarthMap extends AbstractWorldMap {
    public EarthMap(
            Boundary mapBoundary,
            int reproductionEnergyMinimum
    ) {
        super(mapBoundary, reproductionEnergyMinimum);
    }

    @Override
    public void growGrass(int grassAmount){
        //1. Generate equator area

        Boundary equator = getEquator();

        ArrayList<Vector2d> nonGrassEquatorPositions = generatePreferredGrassSpaces();


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

        int lowerHeight = equator.bottomLeft().y()-this.mapBoundary.bottomLeft().y();
        int upperHeight = this.mapBoundary.upperRight().y()-equator.upperRight().y();
        int width = this.mapBoundary.upperRight().x()-this.mapBoundary.bottomLeft().x()+1;
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
                        equator.upperRight().y() + j+1);
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

    private Boundary getEquator() {
        int mapHeight = mapBoundary.upperRight().y()-mapBoundary.bottomLeft().y()+1;
        Vector2d lowerBound = new Vector2d(this.mapBoundary.bottomLeft().x(),
                        (int) Math.round(mapHeight*0.4) + 1);

        Vector2d upperBound = new Vector2d(this.mapBoundary.upperRight().x(),
                (int) Math.round(mapHeight*0.6));

        return new Boundary(lowerBound,upperBound);
    }

    @Override
    public ArrayList<Vector2d> generatePreferredGrassSpaces() {
        Boundary equator = getEquator();

        //2. Create Array of non grass spaces inside equator
        int width = this.mapBoundary.upperRight().x()-this.mapBoundary.bottomLeft().x()+1;
        int equatorHeight = equator.upperRight().y()-equator.bottomLeft().y()+1;
        ArrayList<Vector2d> nonGrassEquatorPositions = new ArrayList<>();
        for (int i = 0; i < width; i++){
            for (int j = 0; j < equatorHeight; j++){
                Vector2d currPosition = new Vector2d(this.mapBoundary.bottomLeft().x()+i,
                        equator.bottomLeft().y()+j);
                if(this.grasses.get(currPosition) == null ){
                    nonGrassEquatorPositions.add(currPosition);
                }
            }
        }
        return nonGrassEquatorPositions;
    }
}