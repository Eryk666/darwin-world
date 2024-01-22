package agh.ics.oop.model;

import agh.ics.oop.model.worldmap.AbstractWorldMap;

import java.util.ArrayList;

public interface MapChangeListener {
    void mapChanged(AbstractWorldMap map, String message);
}
