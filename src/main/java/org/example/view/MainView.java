package org.example.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.example.model.Animalia;

import java.io.File;

public class MainView extends BorderPane {

    // ---------- Datuak ----------
    private final ObservableList<Animalia> animaliak = FXCollections.observableArrayList();

    // ---------- Taula ----------
    private final TableView<Animalia> taula = new TableView<>(animaliak);

    // ---------- Formularioa ----------
    private final TextField txtIzena = new TextField();
    private final ComboBox<String> cmbEspezia = new ComboBox<>();
    private final TextField txtAdina = new TextField();
    private final TextField txtPisua = new TextField();
    private final CheckBox chkTxertatua = new CheckBox("Txertatuta dago");
    private final TextArea txtOharrak = new TextArea();

    // ---------- Botoiak ----------
    private final Button btnBerria = new Button("Adoptatu");
    private final Button btnTxertatu = new Button("Txertatu");
    private final Button btnEzabatu = new Button("Ezabatu");
    private final Button btnIkusi = new Button("Ikusi");
    private final Button btnIrten = new Button("Irten");
    private final Button btnGorde = new Button("Gorde");
    private final Button btnGarbitu = new Button("Garbitu");

    // ---------- Mezuak ----------
    private final Label mezua = new Label();
    private final Label soinuMezua = new Label();

    public MainView() {

        // ---------- GOIALDEA ----------
        Label titulua = new Label("Animalien Babeslekua");
        titulua.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox goikoa = new HBox(titulua);
        goikoa.setAlignment(Pos.CENTER);
        goikoa.setPadding(new Insets(15));
        goikoa.setStyle("-fx-background-color: #2e7d32;");

        // ---------- FORMULARIOA ----------
        txtIzena.setPromptText("Animaliaren izena");
        cmbEspezia.getItems().addAll("Txakurra", "Katua", "Untxia", "Beste bat");
        cmbEspezia.setPromptText("Aukeratu espeziea");
        txtAdina.setPromptText("Adina (urteak)");
        txtPisua.setPromptText("Pisua (kg)");
        txtOharrak.setPromptText("Oharrak...");
        txtOharrak.setPrefRowCount(3);

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
        GridPane.setHgrow(txtIzena, Priority.ALWAYS);
        GridPane.setHgrow(txtOharrak, Priority.ALWAYS);
        txtIzena.setMaxWidth(Double.MAX_VALUE);

        // ---------- Mezuak ----------
        soinuMezua.setStyle("-fx-text-fill: #2e7d32; -fx-font-style: italic;");

        // ---------- TAULA ----------
        taula.setPrefHeight(200);
        taula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        eraikiZutabeak();

        Separator separadorea = new Separator();
        VBox erdikoa = new VBox(15, taula, soinuMezua, separadorea, formularioa);
        erdikoa.setPadding(new Insets(10, 20, 10, 0));
        VBox.setVgrow(taula, Priority.ALWAYS);

        // ---------- MENUA ----------
        for (Button b : new Button[]{btnBerria, btnTxertatu, btnEzabatu, btnIkusi, btnIrten}) {
            b.setMaxWidth(Double.MAX_VALUE);
        }
        VBox menua = new VBox(10, btnBerria, btnEzabatu, btnIkusi, btnTxertatu, btnIrten);
        menua.setPadding(new Insets(15));
        menua.setPrefWidth(170);
        menua.setStyle("-fx-background-color: #e8f5e9;");

        // ---------- BEHEKOA ----------
        btnGorde.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");
        HBox behekoa = new HBox(15, btnGorde, btnGarbitu, mezua);
        behekoa.setAlignment(Pos.CENTER_LEFT);
        behekoa.setPadding(new Insets(12, 20, 12, 20));
        behekoa.setStyle("-fx-background-color: #f5f5f5;");

        // ---------- THIS (¡LA CLAVE DEL CAMBIO!) ----------
        this.setTop(goikoa);
        this.setLeft(menua);
        this.setCenter(erdikoa);
        this.setBottom(behekoa);

        // ---------- Listener de la selección ----------
        taula.getSelectionModel().selectedItemProperty().addListener((obs, old, neu) -> {
            if (neu == null) {
                btnBerria.setText("Adoptatu");
                btnTxertatu.setText("Txertatu");
            } else {
                btnBerria.setText(neu.isAdoptatuta() ? "Desadoptatu" : "Adoptatu");
                btnTxertatu.setText(neu.isTxertatuta() ? "Kendu" : "Txertatu");
            }
        });
    }

    private void eraikiZutabeak() {
        // Irudia
        TableColumn<Animalia, Animalia> colIrudia = new TableColumn<>("Irudia");
        colIrudia.setPrefWidth(70);
        colIrudia.setSortable(false);
        colIrudia.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue()));
        colIrudia.setCellFactory(col -> new TableCell<>() {
            private final ImageView iv = new ImageView();
            {
                iv.setFitWidth(48);
                iv.setFitHeight(48);
                iv.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(Animalia a, boolean empty) {
                super.updateItem(a, empty);
                if (empty || a == null || a.getArgazkia() == null || a.getArgazkia().isBlank()) {
                    setGraphic(null);
                    return;
                }
                File f = new File(a.getArgazkia());
                if (!f.exists()) { setGraphic(null); return; }
                iv.setImage(new Image(f.toURI().toString()));
                setGraphic(iv);
            }
        });

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

        TableColumn<Animalia, Void> colSoinua = new TableColumn<>("Soinua");
        colSoinua.setSortable(false);
        colSoinua.setMinWidth(130);
        colSoinua.setCellFactory(col -> new TableCell<>() {
            private final Button btnSoinua = new Button("Egin soinua");
            {
                btnSoinua.setOnAction(e -> {
                    Animalia a = getTableView().getItems().get(getIndex());
                    if (a != null) {
                        soinuMezua.setText(a.getIzena() + ": " + a.eginSoinua());
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnSoinua);
            }
        });

        taula.getColumns().addAll(colIrudia, colIzena, colEspezia, colAdina,
                colPisua, colTxertatua, colAdoptatuta, colSoinua);
    }

    // ---------- Metodo publikoak ----------
    public void erakutsiMezua(String testua, boolean errorea) {
        mezua.setText(testua);
        mezua.setStyle(errorea ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void garbituFormularioa() {
        txtIzena.clear();
        txtAdina.clear();
        txtPisua.clear();
        txtOharrak.clear();
        cmbEspezia.setValue(null);
        chkTxertatua.setSelected(false);
    }

    public void eguneratuBotoienTestua(Animalia a) {
        if (a == null) return;
        btnBerria.setText(a.isAdoptatuta() ? "Desadoptatu" : "Adoptatu");
        btnTxertatu.setText(a.isTxertatuta() ? "Kendu" : "Txertatu");
    }

    // ---------- Getters ----------
    public TableView<Animalia> getTaula() { return taula; }
    public ObservableList<Animalia> getAnimaliak() { return animaliak; }

    public TextField getTxtIzena() { return txtIzena; }
    public ComboBox<String> getCmbEspezia() { return cmbEspezia; }
    public TextField getTxtAdina() { return txtAdina; }
    public TextField getTxtPisua() { return txtPisua; }
    public CheckBox getChkTxertatua() { return chkTxertatua; }
    public TextArea getTxtOharrak() { return txtOharrak; }

    public Button getBtnBerria() { return btnBerria; }
    public Button getBtnTxertatu() { return btnTxertatu; }
    public Button getBtnEzabatu() { return btnEzabatu; }
    public Button getBtnIkusi() { return btnIkusi; }
    public Button getBtnIrten() { return btnIrten; }
    public Button getBtnGorde() { return btnGorde; }
    public Button getBtnGarbitu() { return btnGarbitu; }
}