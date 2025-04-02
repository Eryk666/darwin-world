package agh.ics.oop.model;

import agh.ics.oop.model.csv.CSVManager;
import agh.ics.oop.model.worldmap.AbstractWorldMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SimulationDataRecorder implements SimulationChangeListener {
    @Override
    public void update(UUID simulationID, AbstractWorldMap worldMap) {
        try {
            Files.createDirectories(Paths.get("logs/"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        String filePath = "logs/simulation_" + simulationID.toString() + ".csv";
        Statistics dataCollector = new Statistics(worldMap);

        // If file did not exist we must append column names at the start
        if (!Files.exists(Path.of(filePath))) {
            CSVManager.writeToFile(filePath, Collections.singletonList(dataCollector.getColumnNames()));
        }

        CSVManager.writeToFile(filePath, Collections.singletonList(dataCollector.collectData()));
    }
}
