package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.SceneManager;
import org.example.controller.LoginController;

public class LoginView extends VBox {

    public LoginView(SceneManager sceneManager) {

        // ---------- Osagaiak ----------
        Label titulua = new Label("Animalien Babeslekua");
        Label mezua = new Label("Sartu admin, pasahitza:1234");
        titulua.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        TextField txtErabiltzailea = new TextField();
        txtErabiltzailea.setPromptText("Erabiltzailea");
        txtErabiltzailea.setMaxWidth(220);

        PasswordField txtPasahitza = new PasswordField();
        txtPasahitza.setPromptText("Pasahitza");
        txtPasahitza.setMaxWidth(220);

        RadioButton rbIkaslea = new RadioButton("Ikaslea");
        RadioButton rbIrakaslea = new RadioButton("Irakaslea");
        RadioButton rbAdministraria = new RadioButton("Administraria");

        ToggleGroup rolak = new ToggleGroup();
        rbIkaslea.setToggleGroup(rolak);
        rbIrakaslea.setToggleGroup(rolak);
        rbAdministraria.setToggleGroup(rolak);
        rbIkaslea.setSelected(true);

        HBox rolBox = new HBox(15, rbIkaslea, rbIrakaslea, rbAdministraria);
        rolBox.setAlignment(Pos.CENTER);

        Button btnSartu = new Button("Sartu");
        btnSartu.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");

        Label errorea = new Label();
        errorea.setStyle("-fx-text-fill: red;");

        // ---------- Controller-a (5 arg: UI + SceneManager) ----------
        LoginController controller = new LoginController(
                txtErabiltzailea, txtPasahitza, rolak, errorea, sceneManager);

        btnSartu.setOnAction(e -> controller.sartu());
        txtPasahitza.setOnAction(e -> controller.sartu());

        // ---------- Diseinua ----------
        this.setSpacing(12);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(30));
        this.getChildren().addAll(titulua, mezua, txtErabiltzailea, txtPasahitza,
                rolBox, btnSartu, errorea);

        this.setStyle(
                "-fx-background-image: url('/img/fondo.jpg');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center center;" +
                        "-fx-background-repeat: no-repeat;"
        );
    }
}