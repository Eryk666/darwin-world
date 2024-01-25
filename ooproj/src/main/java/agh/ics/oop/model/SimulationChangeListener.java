package agh.ics.oop.model;

import agh.ics.oop.model.worldmap.AbstractWorldMap;

import java.util.UUID;

public interface SimulationChangeListener {
    void update(Simulation simulation);
}
