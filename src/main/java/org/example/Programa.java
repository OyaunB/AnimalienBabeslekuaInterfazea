package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Programa extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Crear una escena vacía que el SceneManager irá rellenando.
        Scene scene = new Scene(new Pane(), 850, 760);

        // 2. Inicializar el SceneManager.
        new SceneManager(scene, stage);

        // 3. Configurar y mostrar la ventana.
        stage.setTitle("Animalien Babeslekua");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}