package org.example;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Lehenik login pantaila erakusten da.
        // Datuak ongi badaude, interfaze nagusia irekitzen da.
        Login.erakutsi(stage, () -> erakutsiPrintzipala(stage));
    }

    // Interfaze nagusia (animalien kudeaketa)
    private void erakutsiPrintzipala(Stage stage) {

        // Fitxategian gordetako animaliak kargatu
        ObservableList<Animalia> animaliak =
                FXCollections.observableArrayList(AnimaliaFitxategia.kargatu());

        // ---------- GOIALDEA (top) ----------
        Label titulua = new Label("Animalien Babeslekua");
        titulua.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox goikoa = new HBox(titulua);
        goikoa.setAlignment(Pos.CENTER);
        goikoa.setPadding(new Insets(15));
        goikoa.setStyle("-fx-background-color: #2e7d32;");

        // ---------- FORMULARIOA ----------
        TextField txtIzena = new TextField();
        txtIzena.setPromptText("Animaliaren izena");

        ComboBox<String> cmbEspezia = new ComboBox<>();
        cmbEspezia.getItems().addAll("Txakurra", "Katua", "Untxia", "Beste bat");
        cmbEspezia.setPromptText("Aukeratu espeziea");

        TextField txtAdina = new TextField();
        txtAdina.setPromptText("Adina (urteak)");

        TextField txtPisua = new TextField();
        txtPisua.setPromptText("Pisua (kg)");

        CheckBox chkTxertatua = new CheckBox("Txertatuta dago");

        TextArea txtOharrak = new TextArea();
        txtOharrak.setPromptText("Oharrak...");
        txtOharrak.setPrefRowCount(3);

        PasswordField txtPasahitza = new PasswordField();
        txtPasahitza.setPromptText("Langilearen pasahitza");

        GridPane formularioa = new GridPane();
        formularioa.setHgap(10);
        formularioa.setVgap(10);
        formularioa.setPadding(new Insets(20));
        formularioa.addRow(0, new Label("Izena:"), txtIzena);
        formularioa.addRow(1, new Label("Espezia:"), cmbEspezia);
        formularioa.addRow(2, new Label("Adina:"), txtAdina);
        formularioa.addRow(3, new Label("Pisua:"), txtPisua);
        formularioa.addRow(4, new Label(""), chkTxertatua);
        formularioa.addRow(5, new Label("Oharrak:"), txtOharrak);
        formularioa.addRow(6, new Label("Pasahitza:"), txtPasahitza);
        GridPane.setHgrow(txtIzena, Priority.ALWAYS);
        GridPane.setHgrow(txtOharrak, Priority.ALWAYS);
        txtIzena.setMaxWidth(Double.MAX_VALUE);

        // ---------- TAULA ----------
        TableView<Animalia> taula = new TableView<>(animaliak);
        taula.setPrefHeight(200);
        taula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Animalia, String> colIzena = new TableColumn<>("Izena");
        colIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIzena()));
        TableColumn<Animalia, String> colEspezia = new TableColumn<>("Espezia");
        colEspezia.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEspezia()));
        TableColumn<Animalia, String> colAdina = new TableColumn<>("Adina");
        colAdina.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getAdina())));
        TableColumn<Animalia, String> colPisua = new TableColumn<>("Pisua (kg)");
        colPisua.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getPisua())));
        TableColumn<Animalia, String> colTxertatua = new TableColumn<>("Txertatuta");
        colTxertatua.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isTxertatuta() ? "Bai" : "Ez"));
        TableColumn<Animalia, String> colAdoptatuta = new TableColumn<>("Adoptatuta");
        colAdoptatuta.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isAdoptatuta() ? "Bai" : "Ez"));
        taula.getColumns().addAll(colIzena, colEspezia, colAdina, colPisua, colTxertatua, colAdoptatuta);

        VBox erdikoa = new VBox(formularioa, taula);
        erdikoa.setPadding(new Insets(0, 20, 10, 0));
        VBox.setVgrow(taula, Priority.ALWAYS);

        Label mezua = new Label();

        // ---------- MENUA (left) ----------
        Button btnZerrenda = new Button("Animalien zerrenda");
        Button btnBerria = new Button("Adoptatu");
        Button btnEzabatu = new Button("Ezabatu");
        Button btnIkusi = new Button("Ikusi");
        Button btnIrten = new Button("Irten");
        btnZerrenda.setMaxWidth(Double.MAX_VALUE);
        btnBerria.setMaxWidth(Double.MAX_VALUE);
        btnEzabatu.setMaxWidth(Double.MAX_VALUE);
        btnIkusi.setMaxWidth(Double.MAX_VALUE);
        btnIrten.setMaxWidth(Double.MAX_VALUE);
        btnZerrenda.setOnAction(e -> taula.requestFocus());
        btnIrten.setOnAction(e -> stage.close());

        // Adoptatu: aukeratutako animaliaren eskuragarri = false jartzen da
        btnBerria.setOnAction(e -> {
            Animalia hautatua = taula.getSelectionModel().getSelectedItem();
            if (hautatua == null) {
                erakutsi(mezua, "Aukeratu taulan adoptatu nahi duzun animalia.", true);
                return;
            }
            if (hautatua.isAdoptatuta()) {
                erakutsi(mezua, hautatua.getIzena() + " dagoeneko adoptatuta dago.", true);
                return;
            }
            try {
                hautatua.adoptatu();
                AnimaliaFitxategia.gordeGuztiak(animaliak);
                taula.refresh();
                erakutsi(mezua, hautatua.getIzena() + " adoptatu da.", false);
            } catch (IOException ex) {
                erakutsi(mezua, "Errorea fitxategia eguneratzean.", true);
            }
        });

        // Ezabatu: aukeratutako animalia zerrendatik eta fitxategitik kentzen da
        btnEzabatu.setOnAction(e -> {
            Animalia hautatua = taula.getSelectionModel().getSelectedItem();
            if (hautatua == null) {
                erakutsi(mezua, "Aukeratu taulan ezabatu nahi duzun animalia.", true);
                return;
            }
            Alert galdera = new Alert(Alert.AlertType.CONFIRMATION,
                    hautatua.getIzena() + " ezabatu nahi duzu?",
                    ButtonType.YES, ButtonType.NO);
            galdera.setHeaderText(null);
            if (galdera.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
                return;
            }
            try {
                animaliak.remove(hautatua);
                AnimaliaFitxategia.gordeGuztiak(animaliak);
                erakutsi(mezua, hautatua.getIzena() + " ezabatu da.", false);
            } catch (IOException ex) {
                erakutsi(mezua, "Errorea fitxategia eguneratzean.", true);
            }
        });
        //Ikusi botoia
        btnIkusi.setOnAction(e -> {
            Animalia hautatua = taula.getSelectionModel().getSelectedItem();
            if (hautatua == null) {
                erakutsi(mezua, "Aukeratu taulan ikusi nahi duzun animalia.", true);
                return;
            }
            AnimaliaXehetasuna.erakutsi(hautatua, animaliak, taula::refresh);
        });

        VBox menua = new VBox(10, btnZerrenda, btnBerria, btnEzabatu, btnIkusi, btnIrten);
        menua.setPadding(new Insets(15));
        menua.setPrefWidth(170);
        menua.setStyle("-fx-background-color: #e8f5e9;");

        // ---------- BEHEKOA (bottom) ----------
        Button btnGorde = new Button("Gorde");
        Button btnGarbitu = new Button("Garbitu");

        btnGorde.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");
        btnGorde.setOnAction(e -> {
            // 1. Balidazioa
            if (txtIzena.getText().isBlank() || cmbEspezia.getValue() == null) {
                erakutsi(mezua, "Izena eta espeziea beharrezkoak dira.", true);
                return;
            }
            int adina;
            double pisua;
            try {
                adina = Integer.parseInt(txtAdina.getText().trim());
                pisua = Double.parseDouble(txtPisua.getText().trim().replace(',', '.'));
                if (adina < 0 || pisua <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                erakutsi(mezua, "Adinak eta pisuak zenbaki positiboak eta osoak izan behar dute.", true);
                return;
            }

            // 2. Animalia sortu (eskuragarri = true, oraindik ez dago adoptatuta)
            Animalia animalia = Animalia.sortu(
                    cmbEspezia.getValue(),
                    txtIzena.getText().trim(),
                    adina,
                    pisua,
                    chkTxertatua.isSelected(),
                    true,
                    txtOharrak.getText(),
                    "");

            // 3. Fitxategian gorde eta taulan gehitu
            try {
                AnimaliaFitxategia.gehitu(animalia);
                animaliak.add(animalia);
                erakutsi(mezua, animalia.getIzena() + " gorde da.", false);
                garbitu(txtIzena, txtAdina, txtPisua, txtOharrak, txtPasahitza,
                        cmbEspezia, chkTxertatua);
            } catch (IOException ex) {
                erakutsi(mezua, "Errorea fitxategian gordetzean.", true);
            }
        });

        btnGarbitu.setOnAction(e -> {
            garbitu(txtIzena, txtAdina, txtPisua, txtOharrak, txtPasahitza,
                    cmbEspezia, chkTxertatua);
            mezua.setText("");
        });

        HBox behekoa = new HBox(15, btnGorde, btnGarbitu, mezua);
        behekoa.setAlignment(Pos.CENTER_LEFT);
        behekoa.setPadding(new Insets(12, 20, 12, 20));
        behekoa.setStyle("-fx-background-color: #f5f5f5;");

        // ---------- ERRELA NAGUSIA ----------
        BorderPane root = new BorderPane();
        root.setTop(goikoa);
        root.setLeft(menua);
        root.setCenter(erdikoa);
        root.setBottom(behekoa);

        stage.setTitle("Animalien Babeslekua");
        stage.setScene(new Scene(root, 850, 760));
        stage.sizeToScene();       // Leihoa pantaila berriaren tamainara egokitu
        stage.centerOnScreen();
        stage.show();
    }

    // Formularioko eremuak husten ditu
    private void garbitu(TextField izena, TextField adina, TextField pisua,
                         TextArea oharrak, PasswordField pasahitza,
                         ComboBox<String> espezia, CheckBox txertatua) {
        izena.clear();
        adina.clear();
        pisua.clear();
        oharrak.clear();
        pasahitza.clear();
        espezia.setValue(null);
        txertatua.setSelected(false);
    }

    // Mezu bat erakusten du (gorriz errorea bada, berdez bestela)
    private void erakutsi(Label etiketa, String testua, boolean errorea) {
        etiketa.setText(testua);
        etiketa.setStyle(errorea ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
