package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.MainController;
import org.example.service.AnimaliaService;
import org.example.view.LoginView;
import org.example.view.MainView;

public class Programa extends Application {

    @Override
    public void start(Stage stage) {
        LoginView.erakutsi(stage, () -> erakutsiPrintzipala(stage));
    }

    private void erakutsiPrintzipala(Stage stage) {
        MainView view = new MainView();
        new MainController(view, new AnimaliaService(), stage);

        stage.setTitle("Animalien Babeslekua");
        stage.setScene(new Scene(view.getRoot(), 850, 760));
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}