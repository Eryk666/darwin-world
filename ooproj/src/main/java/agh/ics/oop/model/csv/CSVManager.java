package agh.ics.oop.model.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CSVManager {
    // TODO possibly add readFromFile method

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

    private static String parseToCSV(String[] dataEntry) {
        return String.join(";", dataEntry);
    }
}
