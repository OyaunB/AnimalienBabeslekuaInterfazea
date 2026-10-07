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
//Argazkia columnan agertu dadin, beheko funtzionalitate jauek behar dira
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

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

        //LEHEN KOLUMNA; argazkia
        // ---- Columna de imagen (miniatura) ----
        TableColumn<Animalia, Animalia> colIrudia = new TableColumn<>("Irudia");
        colIrudia.setPrefWidth(70);
        colIrudia.setSortable(false);

                    // 1) Qué dato saca de cada fila: el objeto Animalia entero
        colIrudia.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue()));

                    // 2) Cómo dibuja la celda: un ImageView con la miniatura
        colIrudia.setCellFactory(col -> new TableCell<Animalia, Animalia>() {
            private final ImageView iv = new ImageView();

            {
                iv.setFitWidth(48);
                iv.setFitHeight(48);
                iv.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Animalia animalia, boolean empty) {
                super.updateItem(animalia, empty);

                // Sin contenido: no dibujamos nada
                if (empty || animalia == null
                        || animalia.getArgazkia() == null
                        || animalia.getArgazkia().isBlank()) {
                    setGraphic(null);
                    return;
                }

                File f = new File(animalia.getArgazkia());
                if (!f.exists()) {
                    setGraphic(null);
                    return;
                }

                iv.setImage(new Image(f.toURI().toString()));
                setGraphic(iv);
            }
        });
        //__________________________
        //__________________________
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
        taula.getColumns().addAll(colIrudia, colIzena, colEspezia, colAdina, colPisua, colTxertatua, colAdoptatuta);

        VBox erdikoa = new VBox(formularioa, taula);
        erdikoa.setPadding(new Insets(0, 20, 10, 0));
        VBox.setVgrow(taula, Priority.ALWAYS);

        Label mezua = new Label();

        // ---------- MENUA (left) ----------
        Button btnZerrenda = new Button("AUKERATU");
        Button btnBerria = new Button("Adoptatu");
        Button btnTxertatu = new Button("Txertatu");
        Button btnEzabatu = new Button("Ezabatu");
        Button btnIkusi = new Button("Ikusi");
        Button btnIrten = new Button("Irten");
        btnZerrenda.setMaxWidth(Double.MAX_VALUE);
        btnBerria.setMaxWidth(Double.MAX_VALUE);
        btnTxertatu.setMaxWidth(Double.MAX_VALUE);
        btnEzabatu.setMaxWidth(Double.MAX_VALUE);
        btnIkusi.setMaxWidth(Double.MAX_VALUE);
        btnIrten.setMaxWidth(Double.MAX_VALUE);
        btnZerrenda.setOnAction(e -> taula.requestFocus());
        btnIrten.setOnAction(e -> stage.close());

        // Adoptatu: aukeratutako animaliaren eskuragarri = false jartzen da
        btnBerria.setOnAction(e -> {
            Animalia hautatua = taula.getSelectionModel().getSelectedItem();
            if (hautatua == null) {
                erakutsi(mezua, "Aukeratu taulan animalia bat.", true);
                return;
            }
            try {
                boolean adoptatutaZegoen = hautatua.isAdoptatuta();
                hautatua.aldatuAdopzioa();
                AnimaliaFitxategia.gordeGuztiak(animaliak);
                taula.refresh();
                // Forzar al botón a recalcular su texto
                btnBerria.setText(hautatua.isAdoptatuta() ? "Desadoptatu" : "Adoptatu");
                if (adoptatutaZegoen) {
                    erakutsi(mezua, hautatua.getIzena() + " desadoptatu da.", false);
                } else {
                    erakutsi(mezua, hautatua.getIzena() + " adoptatu da.", false);
                }
            } catch (IOException ex) {
                erakutsi(mezua, "Errorea fitxategia eguneratzean.", true);
            }
        });

        // >>> HAMEN DOA LISTENER -a <<< EGOERAREN ARABERA aldatuko da hasierako menuan
        taula.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo == null) {
                btnBerria.setText("Adoptatu");
                btnTxertatu.setText("Txertatu");
            } else {
                btnBerria.setText(nuevo.isAdoptatuta() ? "Desadoptatu" : "Adoptatu");
                btnTxertatu.setText(nuevo.isTxertatuta() ? "Kendu" : "Txertatu");
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

        //Txertatu botoia
        btnTxertatu.setOnAction(e -> {
            Animalia hautatua = taula.getSelectionModel().getSelectedItem();
            if (hautatua == null) {
                erakutsi(mezua, "Aukeratu taulan txertatu nahi duzun animalia.", true);
                return;
            }
            try {
                hautatua.aldatuTxertatuta();
                AnimaliaFitxategia.gordeGuztiak(animaliak);
                taula.refresh();
                btnTxertatu.setText(hautatua.isTxertatuta() ? "Kendu" : "Txertatu");
                String egoera = hautatua.isTxertatuta() ? "txertatuta" : "txertatu gabe";
                erakutsi(mezua, hautatua.getIzena() + " orain " + egoera + " dago.", false);
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

        VBox menua = new VBox(10, btnZerrenda, btnBerria, btnEzabatu, btnIkusi,btnTxertatu, btnIrten);
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
