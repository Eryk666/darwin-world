package agh.ics.oop.front.presenter;


import agh.ics.oop.front.InputParser;
import agh.ics.oop.front.WrongInputException;
import agh.ics.oop.model.*;
import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.FunkyAnimal;
import agh.ics.oop.model.csv.CSVManager;
import agh.ics.oop.model.worldmap.AbstractWorldMap;
import agh.ics.oop.model.worldmap.EarthMap;
import agh.ics.oop.model.worldmap.FunkyMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class MainPresenter implements Initializable {
    @FXML
    public ChoiceBox<String> chosenConfig;
    @FXML
    public Button loadConfigButton;
    @FXML
    public TextField mapHeight;
    @FXML
    public TextField mapWidth;
    @FXML
    public ChoiceBox<String> mapVariant;
    @FXML
    public TextField startGrassAmount;
    @FXML
    public TextField energyPerGrass;
    @FXML
    public TextField dailyGrassGrowth;
    @FXML
    public ChoiceBox<String> grassVariant;
    @FXML
    public TextField initialAnimalAmount;
    @FXML
    public TextField initialAnimalEnergy;
    @FXML
    public TextField energyForHorny;
    @FXML
    public TextField reproductionEnergyCost;
    @FXML
    public TextField minMutationAmount;
    @FXML
    public TextField maxMutationAmount;
    @FXML
    public ChoiceBox<String> mutationVariant;
    @FXML
    public TextField genomeLength;
    @FXML
    public ChoiceBox<String> animalBehaviourVariant;
    @FXML
    public CheckBox saveToCSVCheckBox;
    @FXML
    public Button saveConfigButton;
    @FXML
    public TextField configName;
    @FXML
    public Button startButton;


    @FXML
    private void simulationStartButton() throws WrongInputException {
        Simulation simulation = generateSimulation();

        if (this.saveToCSVCheckBox.isSelected()) {
            SimulationChangeListener simulationChangeListener = new SimulationDataRecorder();
            simulation.subscribe(simulationChangeListener);
        }

        // Create new scene with simulation

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        Stage stage = new Stage();
        stage.setTitle("RatApp simulation");

        Image icon = new Image("rat.jpg");
        stage.getIcons().add(icon);

        AnchorPane root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        SimulationPresenter presenter = loader.getController();

        simulation.subscribe(presenter);

        presenter.setWorldMap(simulation.getWorldMap());
        stage.setMaximized(true);
        stage.show();

        Thread thread = new Thread(simulation);
        thread.start();
    }

    private Simulation generateSimulation() throws WrongInputException {
        return new Simulation(
            generateWorldMap(),
            generateInitialAnimals(),
            InputParser.parse(this.energyForHorny.getText()),
            InputParser.parse(this.reproductionEnergyCost.getText()),
            InputParser.parse(this.startGrassAmount.getText()),
            InputParser.parse(this.dailyGrassGrowth.getText()),
            InputParser.parse(this.energyPerGrass.getText())
        );
    }

    private AbstractWorldMap generateWorldMap() throws WrongInputException {
        String grassVariant = this.grassVariant.getValue();
        Boundary mapBoundary = new Boundary(
            new Vector2d(1, 1),
            new Vector2d(
                InputParser.parse(this.mapWidth.getText()),
                InputParser.parse(this.mapHeight.getText())
            )
        );

        switch (grassVariant) {
            case "ForestedEquators" -> {
                return new EarthMap(mapBoundary, InputParser.parse(energyForHorny.getText()));
            }
            case "CreepingJungle" -> {
                return new FunkyMap(mapBoundary, InputParser.parse(energyForHorny.getText()));
            }
            default -> throw new RuntimeException("Map variant " + grassVariant + " does not exist!");
        }
    }

    private List<Animal> generateInitialAnimals() throws WrongInputException {
        ArrayList<Animal> animals = new ArrayList<>();
        Random random = new Random();
        String selectedBehaviour = this.animalBehaviourVariant.getValue();
        int animalAmount = InputParser.parse(this.initialAnimalAmount.getText());
        int initialAnimalEnergy = InputParser.parse(this.initialAnimalEnergy.getText());
        int minMutation = InputParser.parse(this.minMutationAmount.getText());
        int maxMutation = InputParser.parse(this.maxMutationAmount.getText());
        int genomeLength = InputParser.parse(this.genomeLength.getText());
        Boundary positionBoundary = new Boundary(
            new Vector2d(1, 1),
            new Vector2d(
                InputParser.parse(this.mapWidth.getText()),
                InputParser.parse(this.mapHeight.getText())
            )
        );

        if (genomeLength < maxMutation) {
            throw new WrongInputException(
                "Genome length " + genomeLength + " is smaller than maxMutation " + maxMutation
            );
        }

        switch (selectedBehaviour) {
            case "CompletePredestination" -> {
                for (int i = 0; i < animalAmount; i++) {
                    animals.add(new Animal(
                        new Vector2d(
                            random.nextInt(positionBoundary.upperRight().x()),
                            random.nextInt(positionBoundary.upperRight().y())),
                        initialAnimalEnergy,
                        createRandomGenes(genomeLength),
                        minMutation,
                        maxMutation
                    ));
                }
            }
            case "BackAndForth" -> {
                for (int i = 0; i < animalAmount; i++) {
                    animals.add(new FunkyAnimal(
                        new Vector2d(
                            random.nextInt(positionBoundary.upperRight().x()),
                            random.nextInt(positionBoundary.upperRight().y())),
                        initialAnimalEnergy,
                        createRandomGenes(genomeLength),
                        minMutation,
                        maxMutation
                    ));
                }
            }
            default -> throw new RuntimeException(
                "Behaviour variant " + selectedBehaviour + " does not exist!"
            );
        }

        return animals;
    }

    public List<Integer> createRandomGenes(int genomeLength) {
        ArrayList<Integer> genes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < genomeLength; i++) {
            genes.add(r.nextInt(8));
        }
        return genes;
    }

    public void loadConfig() {
        String chosenConfigName = this.chosenConfig.getValue();
        String[] chosenConfig;

        if (chosenConfigName == null) {
            return;
        }

        try {
            chosenConfig = CSVManager.readFromFile("src/main/resources/configs.csv").stream()
                .filter(config -> config[0].equals(chosenConfigName))
                .findFirst()
                .orElse(null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        if (chosenConfig == null) {
            return;
        }

        this.mapHeight.setText(chosenConfig[1]);
        this.mapWidth.setText(chosenConfig[2]);
        this.mapVariant.setValue(chosenConfig[3]);
        this.startGrassAmount.setText(chosenConfig[4]);
        this.energyPerGrass.setText(chosenConfig[5]);
        this.dailyGrassGrowth.setText(chosenConfig[6]);
        this.grassVariant.setValue(chosenConfig[7]);
        this.initialAnimalAmount.setText(chosenConfig[8]);
        this.initialAnimalEnergy.setText(chosenConfig[9]);
        this.reproductionEnergyCost.setText(chosenConfig[10]);
        this.energyForHorny.setText(chosenConfig[11]);
        this.minMutationAmount.setText(chosenConfig[12]);
        this.maxMutationAmount.setText(chosenConfig[13]);
        this.mutationVariant.setValue(chosenConfig[14]);
        this.genomeLength.setText(chosenConfig[15]);
        this.animalBehaviourVariant.setValue(chosenConfig[16]);
        this.saveToCSVCheckBox.setSelected(false);
    }

    public void saveConfig() {
        String configFile = "src/main/resources/configs.csv";
        String[] config = new String[] {
            configName.getText(),
            mapHeight.getText(),
            mapWidth.getText(),
            mapVariant.getValue(),
            startGrassAmount.getText(),
            energyPerGrass.getText(),
            dailyGrassGrowth.getText(),
            grassVariant.getValue(),
            initialAnimalAmount.getText(),
            initialAnimalEnergy.getText(),
            reproductionEnergyCost.getText(),
            energyForHorny.getText(),
            minMutationAmount.getText(),
            maxMutationAmount.getText(),
            mutationVariant.getValue(),
            genomeLength.getText(),
            animalBehaviourVariant.getValue()
        };

        CSVManager.writeToFile(configFile, Collections.singletonList(config));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Saved configurations
        try {
            String[] savedConfigs = CSVManager.readFromFile("src/main/resources/configs.csv").stream()
                .map(config -> config[0])
                .toArray(String[]::new);
            this.chosenConfig.getItems().addAll(savedConfigs);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Map variant
        String[] maps = {"EarthMap"};
        this.mapVariant.getItems().addAll(maps);
        this.mapVariant.setValue("EarthMap");

        // Grass variant
        String[] grass = {"ForestedEquators", "CreepingJungle"};
        this.grassVariant.getItems().addAll(grass);
        this.grassVariant.setValue("ForestedEquators");

        // Mutation variant
        String[] mutations = {"FullRandomness"};
        this.mutationVariant.getItems().addAll(mutations);
        this.mutationVariant.setValue("FullRandomness");

        // Behaviour variant
        String[] behaviours = {"CompletePredestination","BackAndForth"};
        this.animalBehaviourVariant.getItems().addAll(behaviours);
        this.animalBehaviourVariant.setValue("CompletePredestination");
    }
}
