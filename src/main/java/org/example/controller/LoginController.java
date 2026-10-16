package org.example.controller;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.SceneManager;

public class LoginController {

    // ---------- Datuak ----------
    private static final String ERABILTZAILEA = "admin";
    private static final String PASAHITZA = "1234";

    // ---------- UI erreferentziak ----------
    private final TextField txtErabiltzailea;
    private final PasswordField txtPasahitza;
    private final ToggleGroup rolak;
    private final Label errorea;

    // ---------- SceneManager ----------
    private final SceneManager sceneManager;

    // ---------- Eraikitzailea ----------
    public LoginController(TextField txtErabiltzailea,
                           PasswordField txtPasahitza,
                           ToggleGroup rolak,
                           Label errorea,
                           SceneManager sceneManager) {
        this.txtErabiltzailea = txtErabiltzailea;
        this.txtPasahitza = txtPasahitza;
        this.rolak = rolak;
        this.errorea = errorea;
        this.sceneManager = sceneManager;
    }

    // ---------- Ekintza nagusia ----------
    public void sartu() {
        if (txtErabiltzailea.getText().equals(ERABILTZAILEA)
                && txtPasahitza.getText().equals(PASAHITZA)) {
            String rola = lortuRola();
            erakutsiAgurra(rola);
        } else {
            errorea.setText("Erabiltzaile edo pasahitz okerra.");
            txtPasahitza.clear();
        }
    }

    // ---------- Laguntzaileak ----------
    private String lortuRola() {
        RadioButton elegido = (RadioButton) rolak.getSelectedToggle();
        return (elegido != null) ? elegido.getText() : "";
    }

    private void erakutsiAgurra(String rola) {
        Stage agurra = new Stage();
        agurra.setTitle("Ongi etorri");

        Label ongiEtorri = new Label("Ongi etorri, " + rola + " \nbezala sartu zara!!!");
        ongiEtorri.setWrapText(true);
        ongiEtorri.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        VBox agurRoot = new VBox(ongiEtorri);
        agurRoot.setAlignment(Pos.CENTER);
        agurRoot.setPadding(new Insets(30));
        agurRoot.setStyle("-fx-background-color: #e8f5e9;");

        agurra.setScene(new Scene(agurRoot, 320, 140));

        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(ev -> {
            agurra.close();
            // ALDAKETA: en lugar de sarreraZuzena.run(), usamos el SceneManager
            sceneManager.showMainView();
        });

        agurra.show();
        pausa.play();
    }
}