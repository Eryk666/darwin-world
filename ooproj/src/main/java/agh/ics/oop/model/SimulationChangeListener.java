package agh.ics.oop.model;

import agh.ics.oop.model.worldmap.AbstractWorldMap;

public interface SimulationChangeListener {
    void update(AbstractWorldMap worldMap);
}
