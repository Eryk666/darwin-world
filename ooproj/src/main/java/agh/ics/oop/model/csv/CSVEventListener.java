package agh.ics.oop.model.csv;

import agh.ics.oop.model.Simulation;

import java.io.File;
import java.util.List;

public interface CSVEventListener {
    void update(Simulation simulation);
}
