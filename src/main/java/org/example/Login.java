package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login {

    public static void erakutsi(Stage stage, Runnable sarreraZuzena) {

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

        // ---------- Rolak (RadioButton) ----------
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

        // ---------- Botoia eta errorea ----------
        Button btnSartu = new Button("Sartu");
        btnSartu.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");

        Label errorea = new Label();
        errorea.setStyle("-fx-text-fill: red;");

        // ---------- Controllerra ----------
        LoginController controller = new LoginController(
                txtErabiltzailea, txtPasahitza, rolak, errorea, sarreraZuzena);

        btnSartu.setOnAction(e -> controller.sartu());
        txtPasahitza.setOnAction(e -> controller.sartu());

        // ---------- Diseinua ----------
        VBox root = new VBox(12, titulua, mezua, txtErabiltzailea, txtPasahitza,
                rolBox, btnSartu, errorea);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.setStyle(
                "-fx-background-image: url('/img/fondo.jpg');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center center;" +
                        "-fx-background-repeat: no-repeat;"
        );

        stage.setTitle("Login");
        stage.setScene(new Scene(root, 380, 320));
        stage.show();
    }
}