package agh.ics.oop.front.presenter;


import agh.ics.oop.front.InputParser;
import agh.ics.oop.front.WrongInputException;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.FunkyAnimal;
import agh.ics.oop.model.SimulationChangeListener;
import agh.ics.oop.model.SimulationDataRecorder;
import agh.ics.oop.model.worldmap.AbstractWorldMap;
import agh.ics.oop.model.worldmap.EarthMap;
import agh.ics.oop.model.worldmap.FunkyMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


public class MainPresenter implements Initializable {
    @FXML
    public TextField mapHeight;
    @FXML
    public TextField mapWidth;
    @FXML
    public ChoiceBox<String> mapVariant;
    @FXML
    public Button startButton;
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
    private void simulationStartButton() throws IOException, InterruptedException, WrongInputException {
        // Parse inputs
        Boundary mapBounds = new Boundary(new Vector2d(1,1),
                new Vector2d(InputParser.parse(this.mapWidth.getText()),InputParser.parse(this.mapHeight.getText())));
        int initialGrassNumber = InputParser.parse(this.startGrassAmount.getText());
        int energyPerGrass = InputParser.parse(this.energyPerGrass.getText());
        int grassGrownPerDay = InputParser.parse(this.dailyGrassGrowth.getText());
        int reproductionEnergyMinimum = InputParser.parse(this.energyForHorny.getText());
        int reproductionEnergyCost = InputParser.parse(this.reproductionEnergyCost.getText());


        // Select map
        String selectedMap = this.mapVariant.getValue();
        if("EarthMap".equals(selectedMap)){
            System.out.println("Earth Default config");
        }else {
            throw new IOException("how?");
        }

        // Select grass (bc we don't do anything in map we select the map here)
        String selectedGrass = this.grassVariant.getValue();
        AbstractWorldMap map;
        if("ForestedEquators".equals(selectedGrass)){
            map = new EarthMap(mapBounds,reproductionEnergyMinimum);
            System.out.println("Selected ForestedEquators");
        } else if ("CreepingJungle".equals(selectedGrass)) {
            map = new FunkyMap(mapBounds, reproductionEnergyMinimum);
            System.out.println("Selected CreepingJungle");
        }else{
            throw new IOException("how?");
        }

        // Animals
        int animalAmount = InputParser.parse(this.initialAnimalAmount.getText());
        int minMutation = InputParser.parse(this.minMutationAmount.getText());
        int maxMutation = InputParser.parse(this.maxMutationAmount.getText());
        int genomeLength = InputParser.parse(this.genomeLength.getText());
        if (genomeLength < maxMutation){
            throw new WrongInputException("Genome length " + genomeLength + " is smaller than maxMutation " + maxMutation);
        }
        int initialAnimalEnergy = InputParser.parse(this.initialAnimalEnergy.getText());

        // Select mutations
        String selectedMutations = this.mutationVariant.getValue();
        if("FullRandomness".equals(selectedMutations)){
            System.out.println("Mutations Default config");
        } else {
            throw new IOException("how?");
        }

        // Select behaviour
        String selectedBehaviour = this.animalBehaviourVariant.getValue();
        // Normal animal is creating other normal animals, so we have to just put into
        // simulation correct animal type
        ArrayList<Animal> animals = new ArrayList<>();
        Random r = new Random();

        if("CompletePredestination".equals(selectedBehaviour)){
            for (int i = 0; i < animalAmount; i++) {
                animals.add(new Animal(new Vector2d(r.nextInt(mapBounds.upperRight().x()),r.nextInt(mapBounds.upperRight().y())),
                        initialAnimalEnergy,createRandomGenes(genomeLength),minMutation,maxMutation));
            }
            System.out.println("Default Animal");
        } else if ("BackAndForth".equals(selectedBehaviour)) {
            for (int i = 0; i < animalAmount; i++) {
                animals.add(new FunkyAnimal(new Vector2d(r.nextInt(mapBounds.upperRight().x()),r.nextInt(mapBounds.upperRight().y())),
                        initialAnimalEnergy,createRandomGenes(genomeLength),minMutation,maxMutation));

            }
            System.out.println("Funky Animal");
        } else throw new IOException("how?");

        // Create new scene with simulation

        System.out.println("entered thread");
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
        // Works up to here
        Simulation simulation = new Simulation(map,animals,reproductionEnergyMinimum,reproductionEnergyCost,initialGrassNumber,
                grassGrownPerDay,energyPerGrass);

        simulation.subscribe(presenter);
        // TODO let users choose if simulation should be saved to csv file
        boolean saveToFile = true;
        if (saveToFile) {
            SimulationChangeListener simulationChangeListener = new SimulationDataRecorder();
            simulation.subscribe(simulationChangeListener);
        }


        presenter.setWorldMap(map);
        stage.setMaximized(true);
        stage.show();

        Thread thread = new Thread(simulation);
        thread.start();
    }

    public List<Integer> createRandomGenes(int genomeLength){
        ArrayList<Integer> genes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < genomeLength; i++) {
            genes.add(r.nextInt(8));
        }
        return genes;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
