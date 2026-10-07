package org.example;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.PauseTransition; // Ongi etorri animaziorako
import javafx.util.Duration;   // Ongi etorri animaziorako

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

            //_____________________RadioButtona-ak______________________
            RadioButton rbIkaslea = new RadioButton("Ikaslea");
            RadioButton rbIrakaslea = new RadioButton("Irakaslea");
            RadioButton rbAdministraria = new RadioButton("Administraria");

        //PORQUE ToggleGroup: Sin él, los tres RadioButton son independientes y podrías marcar
        // los tres a la vez. Con un ToggleGroup común, solo uno puede estar seleccionado
            ToggleGroup rolak = new ToggleGroup();
            rbIkaslea.setToggleGroup(rolak);
            rbIrakaslea.setToggleGroup(rolak);
            rbAdministraria.setToggleGroup(rolak);
            // Uno seleccionado por defecto (opcional pero recomendado)
            rbIkaslea.setSelected(true);
            //hbOX-A SORTU ELEMENTU HAUEKIN ETA ONDOREN VBox-ean jarri
            HBox rolBox = new HBox(15, rbIkaslea, rbIrakaslea, rbAdministraria);
            rolBox.setAlignment(Pos.CENTER);

            //GARRANTZITSUA: Funtzionatu ahal izateko, [[import javafx.scene.layout.HBox;]] behar du

            //___________________________________________________________

            Button btnSartu = new Button("Sartu");
            btnSartu.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");

            Label errorea = new Label();
            errorea.setStyle("-fx-text-fill: red;");

            // ---------- Datuak egiaztatzen dituen kodea ----------
            Runnable egiaztatu = () -> {
                if (txtErabiltzailea.getText().equals(ERABILTZAILEA)
                        && txtPasahitza.getText().equals(PASAHITZA)) {
                    //sarreraZuzena.run();          // Datuak ongi: interfaze nagusira joan
                    // 1. Averiguar qué rol se ha elegido
                    RadioButton elegido = (RadioButton) rolak.getSelectedToggle();
                    String rola = (elegido != null) ? elegido.getText() : "";

                    // 2. Crear la ventana de bienvenida
                    Stage agurra = new Stage();
                    agurra.setTitle("Ongi etorri");
                    Label ongiEtorri = new Label("Ongi etorri, " + rola + " \nbezala sartu zara!!!");
                    ongiEtorri.setWrapText(true);  //Ilara hau behar da, hurrengo filara "\n"-k funtzionatu dezan
                    ongiEtorri.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");  //Kolore berdea
                    VBox agurRoot = new VBox(ongiEtorri);
                    agurRoot.setAlignment(Pos.CENTER);
                    agurRoot.setPadding(new Insets(30));
                    agurRoot.setStyle("-fx-background-color: #e8f5e9;"); //Kolore berdea
                    agurra.setScene(new Scene(agurRoot, 320, 140));

                    // 3. Temporizador: cerrar la ventana a los 3 segundos
                    PauseTransition pausa = new PauseTransition(Duration.seconds(3));
                    pausa.setOnFinished(ev -> {
                        agurra.close();          // cierra el saludo
                        sarreraZuzena.run();     // y ahora sí, abre el Main
                    });

                    // 4. Mostrar la ventana y arrancar el temporizador
                    agurra.show();
                    pausa.play();
                } else {
                    errorea.setText("Erabiltzaile edo pasahitz okerra.");
                    txtPasahitza.clear();
                }
            };

            // Botoia sakatzean edo pasahitzean Enter sakatzean egiaztatu
            btnSartu.setOnAction(e -> egiaztatu.run());
            txtPasahitza.setOnAction(e -> egiaztatu.run());

            // ---------- Diseinua (VBox erdian) ----------
            VBox root = new VBox(12, titulua, mezua, txtErabiltzailea, txtPasahitza, rolBox, btnSartu, errorea);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(30));

            // Fondoa: irudia leiho osoan zehar
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


