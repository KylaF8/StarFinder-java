package com.example.ca1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GalaxyScannerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Create an FXMLLoader instance to load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(GalaxyScannerApplication.class.getResource("hello-view.fxml"));
        // Create a Scene with the loaded FXML file as its root and set its dimensions
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        // Set the title of the stage
        stage.setTitle("Galaxy Programme");
        // Set the scene on the stage
        stage.setScene(scene);
        stage.show();
    }

    // The main method that launches JavaFX application
    public static void main(String[] args) {
        launch();
    }
}