package agh.ics.oop.model;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.csv.CSVManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SimulationDataRecorder implements SimulationChangeListener {
    @Override
    public void update(Simulation simulation) {
        String filePath = "logs/simulation_" + simulation.getSimulationID().toString() + ".csv";

        // If file did not exist we must append column names at the start
        if (!Files.exists(Path.of(filePath))) {
            CSVManager.writeToFile(filePath, Collections.singletonList(getColumnNames()));
        }

        CSVManager.writeToFile(filePath, collectData(simulation));
    }

    private String[] getColumnNames() {
        return new String[] {
            "Day",
            "AnimalID",
            "Genes",
            "Current gene index",
            "X",
            "Y",
            "Direction",
            "Energy",
            "Age",
            "Children amount",
            "Descendants amount"
        };
    }

    private List<String[]> collectData(Simulation simulation) {
        List<String[]> data = new ArrayList<>();

        for (Animal animal : simulation.getWorldMap().getAnimals()) {
            data.add(new String[] {
                Integer.toString(simulation.getDaysPassed()),
                animal.getAnimalID().toString(),
                animal.getGenes().toString(),
                Integer.toString(animal.getCurrentGeneIndex()),
                Integer.toString(animal.getPosition().x()),
                Integer.toString(animal.getPosition().y()),
                animal.getDirection().toString(),
                Integer.toString(animal.getEnergy()),
                Integer.toString(animal.getAge()),
                Integer.toString(animal.getChildrenAmount()),
                Integer.toString(animal.getDescendantsAmount())
            });
        }

        return data;
    }
}
