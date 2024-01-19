package agh.ics.oop.front.presenter;

import agh.ics.oop.model.*;
import agh.ics.oop.model.animal.*;
import agh.ics.oop.model.worldmap.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SimulationPresenter implements MapChangeListener, Initializable {
    private static final int CELL_WIDTH = 50;
    private static final int CELL_HEIGHT = 50;

    private AbstractWorldMap worldMap;
    @FXML
    public GridPane mapGrid;

    public void setWorldMap(AbstractWorldMap map){
        this.worldMap = map;
    }

    @Override
    public void mapChanged(AbstractWorldMap map, String message) {
        Platform.runLater(() -> {
            try {
                drawMap();
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        });
    }

    private void drawMap() throws InterruptedException {
        clearGrid();

        Boundary currBoundary = this.worldMap.getMapBoundary();
        int rows = currBoundary.upperRight().y() - currBoundary.bottomLeft().y()+1;
        int cols = currBoundary.upperRight().x() - currBoundary.bottomLeft().x()+1;



        Map<Vector2d, Animal> strongestAnimals = this.worldMap.getStrongestAnimals();
        Map<Vector2d, Grass> grassMap = this.worldMap.getGrasses();

            for (int i = 0; i <= cols; i++){
                for (int j = 0; j <= rows; j++){
                    Vector2d currPos = new Vector2d(currBoundary.bottomLeft().x()+i,currBoundary.bottomLeft().y()+j);

                    //animal is the most important so it goes first
                    if (strongestAnimals.get(currPos) != null) {
                        Animal rat = strongestAnimals.get(currPos);
                        int rotation = switch (rat.getDirection()) {
                            case NORTH -> 0;
                            case NORTH_EAST -> 1;
                            case EAST -> 2;
                            case SOUTH_EAST -> 3;
                            case SOUTH -> 4;
                            case SOUTH_WEST -> 5;
                            case WEST -> 6;
                            case NORTH_WEST -> 7;
                        };
                        int yOffset = 0;
                        if (rat.getEnergy() > this.worldMap.getReproductionEnergyMinimum()) {
                            yOffset = 50;
                        }
                        int xOffset = rotation * 50; //sprite is 50x50 each
                        Image image = new Image("ratsprite1.png");
                        final ImageView view = new ImageView(image);
                        Rectangle mask = new Rectangle(xOffset, yOffset, 50, 50);
                        view.setClip(mask);
                        GridPane.setHalignment(view, HPos.CENTER);
                        GridPane.setValignment(view, VPos.CENTER);
                        mapGrid.add(view, i+1, rows-j);
                    } else if (grassMap.get(currPos) != null) {
                        Image image = new Image("weed.png");
                        final ImageView view = new ImageView(image);
                        GridPane.setHalignment(view, HPos.CENTER);
                        GridPane.setValignment(view, VPos.CENTER);
                        mapGrid.add(view, i+1, rows-j);

                    } else {
                        Image image = new Image("empty.png");
                        final ImageView view = new ImageView(image);
                        GridPane.setHalignment(view, HPos.CENTER);
                        GridPane.setValignment(view, VPos.CENTER);
                        mapGrid.add(view, i+1, rows-j);

                    }
                }
            }


            //adding constraints
            for (int i = 0; i <= rows; i++){
                mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
            }
            for (int i = 0; i <= cols; i++){
                mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
            }
            //adding labels
            for (int i = 0; i <= rows; i++){
                Label label = new Label(Integer.toString(currBoundary.bottomLeft().x()+i));
                mapGrid.add(label,0,rows-i);
            }
            for (int i = 1; i <= cols; i++){
                Label label = new Label(Integer.toString(currBoundary.bottomLeft().y()+i-1));
                mapGrid.add(label,i,0);
                mapGrid.setAlignment(Pos.CENTER);
            }

        Thread.sleep(1000);
    }
    private void clearGrid() {
        this.mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        this.mapGrid.getColumnConstraints().clear();
        this.mapGrid.getRowConstraints().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
