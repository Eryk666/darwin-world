package agh.ics.oop.model.csv;

import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.animal.Animal;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SimulationChangeListener implements CSVEventListener {
    @Override
    public void update(Simulation simulation) {
        String filePath = "logs/simulation_" + simulation.getSimulationID().toString() + ".csv";

        // If file did not exist we must append column names at the start
        if (!Files.exists(Path.of(filePath))) {
            writeToFile(filePath, Collections.singletonList(getColumnNames()));
        }

        writeToFile(filePath, collectData(simulation));
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

    private static String parseToCSV(String[] dataEntry) {
        return String.join(";", dataEntry);
    }

    public static void writeToFile(String fileName, List<String[]> data) {
        try (
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter)
        ) {
            for (String[] entry : data) {
                printWriter.println(parseToCSV(entry));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
