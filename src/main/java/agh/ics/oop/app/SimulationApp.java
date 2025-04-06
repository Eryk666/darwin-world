package agh.ics.oop.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;


public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getClassLoader().getResource("mainapp.fxml")
        );

        SplitPane root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("RAT APP");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("rat.jpg"));
        primaryStage.show();
    }
}
