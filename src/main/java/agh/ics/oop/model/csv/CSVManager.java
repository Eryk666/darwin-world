package agh.ics.oop.model.csv;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVManager {
    public static List<String[]> readFromFile(String fileName) throws FileNotFoundException {
        List<String[]> data = new ArrayList<>();

        try (
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader)
        ) {
            String line = bufferedReader.readLine();

            while (line != null) {
                data.add(line.split(";"));
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return data;
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
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String parseToCSV(String[] dataEntry) {
        return String.join(";", dataEntry);
    }
}
