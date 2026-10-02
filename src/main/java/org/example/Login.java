package org.example;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

    public class Login {

        // Login-aren datuak (ariketa sinple bat denez, kodean idatzita daude)
        private static final String ERABILTZAILEA = "admin";
        private static final String PASAHITZA = "1234";

        // Login pantaila erakusten du.
        // Datuak zuzenak badira, "sarreraZuzena" kodea exekutatzen da (interfaze nagusia irekitzen du)
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

            Button btnSartu = new Button("Sartu");
            btnSartu.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");

            Label errorea = new Label();
            errorea.setStyle("-fx-text-fill: red;");

            // ---------- Datuak egiaztatzen dituen kodea ----------
            Runnable egiaztatu = () -> {
                if (txtErabiltzailea.getText().equals(ERABILTZAILEA)
                        && txtPasahitza.getText().equals(PASAHITZA)) {
                    sarreraZuzena.run();          // Datuak ongi: interfaze nagusira joan
                } else {
                    errorea.setText("Erabiltzaile edo pasahitz okerra.");
                    txtPasahitza.clear();
                }
            };

            // Botoia sakatzean edo pasahitzean Enter sakatzean egiaztatu
            btnSartu.setOnAction(e -> egiaztatu.run());
            txtPasahitza.setOnAction(e -> egiaztatu.run());

            // ---------- Diseinua (VBox erdian) ----------
            VBox root = new VBox(12, titulua, mezua, txtErabiltzailea, txtPasahitza, btnSartu, errorea);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(30));

            stage.setTitle("Login");
            stage.setScene(new Scene(root, 380, 320));
            stage.show();
        }
    }


