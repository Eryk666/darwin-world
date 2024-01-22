package agh.ics.oop.front.presenter;

import agh.ics.oop.model.*;
import agh.ics.oop.model.animal.*;
import agh.ics.oop.model.worldmap.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class SimulationPresenter implements MapChangeListener, Initializable {
    private static final int CELL_WIDTH = 50;
    private static final int CELL_HEIGHT = 50;
    @FXML
    public AnchorPane statsPane;
    private AbstractWorldMap worldMap;
    @FXML
    public GridPane mapGrid;

    public void setWorldMap(AbstractWorldMap map){
        this.worldMap = map;
        this.worldMap.setPaused(true);
    }

    @Override
    public void mapChanged(AbstractWorldMap map, String message) {
        Platform.runLater(() -> {
            try {
                drawMap();
                updateStats();
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {}));
            timeline.play();
        });
    }

    private void updateStats() {
        System.out.println("STATS:");
        System.out.println("");
    }

    private void drawMap() throws InterruptedException {
        clearGrid();

        Boundary currBoundary = this.worldMap.getMapBoundary();

        int rows = currBoundary.upperRight().y() - currBoundary.bottomLeft().y()+1;
        int cols = currBoundary.upperRight().x() - currBoundary.bottomLeft().x()+1;


        Map<Vector2d, Animal> strongestAnimals = this.worldMap.getStrongestAnimals();
        Map<Vector2d, Grass> grassMap = this.worldMap.getGrasses();

        System.out.println("Grass size:" + grassMap.size());

        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows; j++){
                Vector2d currPos = new Vector2d(currBoundary.bottomLeft().x()+i,currBoundary.bottomLeft().y()+j);
                Canvas canvas;
                canvas = createCanvas("/empty.png", currPos.x(), currPos.y());
                mapGrid.add(canvas, i+1, rows-j);
                //animal is the most important so it goes first
                if (strongestAnimals.get(currPos) != null) {
                    Animal rat = strongestAnimals.get(currPos);
                    char rotation = switch (rat.getDirection()) {
                        case NORTH -> '0';
                        case NORTH_EAST -> '1';
                        case EAST -> '2';
                        case SOUTH_EAST -> '3';
                        case SOUTH -> '4';
                        case SOUTH_WEST -> '5';
                        case WEST -> '6';
                        case NORTH_WEST -> '7';
                    };
                    char health = switch (rat.getEnergy()/(this.worldMap.getReproductionEnergyMinimum()/3)){
                        case 0 -> '1';
                        case 1 -> '2';
                        case 2 -> '3';
                        default -> '4';
                    };

                    //Label animal = new Label(rat.toString() + rat.getEnergy());
                    canvas = createCanvas("/rats/rat"+rotation+".png", currPos.x(), currPos.y());
                    mapGrid.add(canvas, i+1, rows-j);
                    canvas = createCanvas("/hp/hp"+health+".png",currPos.x(), currPos.y());
                    mapGrid.add(canvas, i+1, rows-j);
                    //mapGrid.add(animal, i+1, rows-j);
                } else if (grassMap.get(currPos) != null) {
                    //Label grass = new Label("*");
                    //mapGrid.add(grass, i+1, rows-j);
                    canvas = createCanvas("/weed.png", currPos.x(), currPos.y());
                    mapGrid.add(canvas, i+1, rows-j);

                }
                canvas = null;
                System.gc();
            }
        }


        //adding constraints
        for (int i = 0; i < rows; i++){
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
        for (int i = 0; i < cols; i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        //adding labels
        for (int i = 0; i < rows; i++){
            Label label = new Label(Integer.toString(currBoundary.bottomLeft().x()+i));
            mapGrid.add(label,0,rows-i);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        }
        for (int i = 1; i <= cols; i++){
            Label label = new Label(Integer.toString(currBoundary.bottomLeft().y()+i-1));
            mapGrid.add(label,i,0);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        }
    }
    private void clearGrid() {
        this.mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        this.mapGrid.getColumnConstraints().clear();
        this.mapGrid.getRowConstraints().clear();
    }

    private Canvas createCanvas(String path, int x, int y){
        Canvas canvas = new Canvas(50,50);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        gc.drawImage(image,0,0,50,50);

        canvas.setOnMouseClicked(event -> displayStats(x,y));

        return canvas;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void displayStats(int x, int y){
        System.out.println("Pressed button on pos: " +x+ ", "+y);
    }

    public void stopSim(){
        worldMap.setPaused(true);
    }
    public void startSim(){
        worldMap.setPaused(false);
    }

}
