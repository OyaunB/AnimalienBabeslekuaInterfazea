package org.example;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.MainController;
import org.example.service.AnimaliaService;
import org.example.view.LoginView;
import org.example.view.MainView;

public class SceneManager {

    private final Scene scene;
    private final Stage stage;

    public SceneManager(Scene scene, Stage stage) {
        this.scene = scene;
        this.stage = stage;
        showLoginView();
    }

    /** Erakusten du login pantaila. */
    public void showLoginView() {
        // LoginView-ak berak sortzen ditu bai UIa bai LoginController-a
        LoginView loginView = new LoginView(this);
        scene.setRoot(loginView);
    }

    /** Erakusten du aplikazioaren pantaila nagusia. */
    public void showMainView() {
        MainView mainView = new MainView();
        new MainController(mainView, new AnimaliaService(), this, stage);
        scene.setRoot(mainView);
    }
}