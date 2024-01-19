package agh.ics.oop.model;

import agh.ics.oop.model.worldmap.AbstractWorldMap;

public interface MapChangeListener {
    void mapChanged(AbstractWorldMap map, String message);
}
