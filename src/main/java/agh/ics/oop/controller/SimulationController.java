package agh.ics.oop.controller;

import agh.ics.oop.model.*;
import agh.ics.oop.model.animal.*;
import agh.ics.oop.model.worldmap.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.*;

public class SimulationController implements SimulationChangeListener, Initializable {

    // Grid dimension
    private static final int CELL_WIDTH = 50;
    private static final int CELL_HEIGHT = 50;

    // UI Elements
    @FXML private Label animalGenome, animalCurrentGenome, animalEnergy, plantsEaten;
    @FXML private Label predecessors, animalLifespan, dayOfDeath;
    @FXML private Label animalsAmount, plantsAmount, emptySpaces, mostPopularGenome;
    @FXML private Label averageEnergy, animalsPredecessors, deadAnimalsLifespan;
    @FXML private AnchorPane statsPane;
    @FXML private GridPane mapGrid;

    private AbstractWorldMap worldMap;
    private Animal displayStatsAnimal;
    private Statistics statistics;
    private boolean showStatistics = false;

    public void setWorldMap(AbstractWorldMap map){
        this.worldMap = map;
        this.worldMap.setPaused(false);
    }

    @Override
    public void update(UUID simulationID, AbstractWorldMap worldMap) {
        Platform.runLater(() -> {
            this.statistics = new Statistics(worldMap);
            try {
                drawMap();
                updateStats();
                updateAnimalStats();

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void drawMap() throws InterruptedException {
        clearGrid();

        Map<Vector2d, Animal> strongestAnimals = worldMap.getStrongestAnimals();
        Map<Vector2d, Grass> grassMap = worldMap.getGrasses();
        Set<Vector2d> preferredGrassSpaces = worldMap.generatePreferredGrassSpaces();
        List<Integer> bestGenome = statistics.determineBestGenome();

        Boundary currBoundary = worldMap.getMapBoundary();
        int rows = currBoundary.upperRight().y() - currBoundary.bottomLeft().y() + 1;
        int cols = currBoundary.upperRight().x() - currBoundary.bottomLeft().x() + 1;

        // Draw cells
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Vector2d position = new Vector2d(
                    currBoundary.bottomLeft().x() + i,
                    currBoundary.upperRight().y() - j
                );

                // Draw cell background
                drawCellBackground(position, preferredGrassSpaces);

                // Draw animal or grass if present
                Animal animal = strongestAnimals.get(position);
                if (animal != null) {
                    drawAnimal(position, animal, bestGenome);
                } else if (grassMap.get(position) != null) {
                    drawGrass(position);
                }
            }
        }

        // Add grid constraints
        addGridConstraints(rows, cols);

        // Add row and column labels
        addGridLabels(currBoundary, rows, cols);
    }

    private void clearGrid() {
        this.mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        this.mapGrid.getColumnConstraints().clear();
        this.mapGrid.getRowConstraints().clear();
    }

    private void drawCellBackground(Vector2d position, Set<Vector2d> preferredGrassSpaces) {
        Canvas emptyCanvas = createCanvas("/empty.png", position.x(), position.y());;
        mapGrid.add(emptyCanvas, position.x(), position.y());

        if (preferredGrassSpaces.contains(position)) {
            Canvas prefferedCanvas = createCanvas("/preferedSpace.png", position.x(), position.y());
            mapGrid.add(prefferedCanvas, position.x(), position.y());
        }
    }

    private void drawAnimal(Vector2d position, Animal animal, List<Integer> bestGenome) {
        char rotation = getAnimalRotation(animal);
        char health = getAnimalHealthStatus(animal);

        // Draw animal image
        Canvas canvas = createCanvas("/rats/rat" + rotation + ".png", position.x(), position.y());
        mapGrid.add(canvas, position.x(), position.y());
        canvas = createCanvas("/hp/hp" + health + ".png", position.x(), position.y());
        mapGrid.add(canvas, position.x(), position.y());

        // Mark the best genome with a crown
        if (animal.getGenes().equals(bestGenome)) {
            canvas = createCanvas("/crown.png", position.x(), position.y());
            mapGrid.add(canvas, position.x(), position.y());
        }
    }

    private char getAnimalRotation(Animal animal) {
        switch (animal.getDirection()) {
            case NORTH: return '0';
            case NORTH_EAST: return '1';
            case EAST: return '2';
            case SOUTH_EAST: return '3';
            case SOUTH: return '4';
            case SOUTH_WEST: return '5';
            case WEST: return '6';
            case NORTH_WEST: return '7';
            default: return '0';
        }
    }

    private char getAnimalHealthStatus(Animal animal) {
        int energyLevel = animal.getEnergy() / (this.worldMap.getReproductionEnergyMinimum() / 3);
        switch (energyLevel) {
            case 0: return '1';
            case 1: return '2';
            case 2: return '3';
            default: return '4';
        }
    }

    private void drawGrass(Vector2d position) {
        Canvas canvas = createCanvas("/weed.png", position.x(), position.y());
        mapGrid.add(canvas, position.x(), position.y());
    }

    private void addGridConstraints(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
        for (int i = 0; i < cols; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
    }

    private void addGridLabels(Boundary currBoundary, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            Label label = new Label(Integer.toString(currBoundary.bottomLeft().x() + i));
            mapGrid.add(label, 0, i + 1);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        }
        for (int i = 1; i <= cols; i++) {
            Label label = new Label(Integer.toString(currBoundary.bottomLeft().y() + i - 1));
            mapGrid.add(label, i, 0);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        }
    }

    // Animal stats
    private void updateAnimalStats() {
        if (this.displayStatsAnimal != null) {
            this.animalGenome.setText(this.statistics.animalGenome(this.displayStatsAnimal));
            this.animalCurrentGenome.setText(this.statistics.animalCurrentGenome(this.displayStatsAnimal));
            this.animalEnergy.setText(this.statistics.animalEnergy(this.displayStatsAnimal));
            this.plantsEaten.setText(this.statistics.animalPlantsEaten(this.displayStatsAnimal));
            this.predecessors.setText(this.statistics.animalPredecessors(this.displayStatsAnimal));
            this.animalLifespan.setText(this.statistics.animalLifespan(this.displayStatsAnimal));
            this.dayOfDeath.setText(this.statistics.animalDayOfDeath(this.displayStatsAnimal));
        } else {
            this.animalGenome.setText("");
            this.animalCurrentGenome.setText("");
            this.animalEnergy.setText("");
            this.plantsEaten.setText("");
            this.predecessors.setText("");
            this.animalLifespan.setText("");
            this.dayOfDeath.setText("");
        }

    }

    // Main stats
    private void updateStats() {
        if (showStatistics) {
            this.animalsAmount.setText("Animals amount: " + this.worldMap.getAnimals().size());
            this.plantsAmount.setText("Plants amount: " + this.worldMap.getGrasses().size());
            this.emptySpaces.setText("Empty spaces: " + this.statistics.countEmptySpaces());
            this.mostPopularGenome.setText("Most popular genome: " + this.statistics.bestGenome());
            this.averageEnergy.setText("Average energy: " + this.statistics.averageEnergy());
            this.deadAnimalsLifespan.setText("Average dead animal lifespan: " + this.statistics.averageDeadAge());
            this.animalsPredecessors.setText("Average alive animal predecessors amount: " + this.statistics.averageAlivePredecessors());
        } else {
            this.animalsAmount.setText("");
            this.plantsAmount.setText("");
            this.emptySpaces.setText("");
            this.mostPopularGenome.setText("");
            this.averageEnergy.setText("");
            this.deadAnimalsLifespan.setText("");
            this.animalsPredecessors.setText("");
        }

    }



    private Canvas createCanvas(String path, int x, int y){
        Canvas canvas = new Canvas(50, 50);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        gc.drawImage(image, 0, 0, 50, 50);

        canvas.setOnMouseClicked(event -> displayStats(x, y));

        return canvas;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //buttons
    void displayStats(int x, int y) {
        System.out.println("Pressed button on pos: " +x+ ", "+y);
        //get animal on the pos
        Map<Vector2d, Animal> strongestAnimals = this.worldMap.getStrongestAnimals();
        this.displayStatsAnimal = strongestAnimals.getOrDefault(new Vector2d(x, y), null);
        Platform.runLater(this::updateAnimalStats);
    }

    @FXML
    private void toggleMapStats(){
        showStatistics = !showStatistics;
        Platform.runLater(this::updateStats);
    }
    @FXML
    public void stopSim(){
        worldMap.setPaused(true);
    }

    @FXML
    public void startSim(){
        worldMap.setPaused(false);
    }
}
