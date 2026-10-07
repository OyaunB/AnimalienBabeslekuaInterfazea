package org.example;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
        import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
        import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javafx.scene.layout.Priority;

public class AnimaliaXehetasuna {

    // Irudiak gordetzen diren karpeta (proiektuaren erroan)
    private static final Path IRUDI_KARPETA = Path.of("img");

    // Animalia baten xehetasunak erakusten dituen leihoa irekitzen du.
    // zerrenda: taulako zerrenda (ezabatzeko eta gordetzeko)
    // eguneratu: taula freskatzeko exekutatu beharreko kodea
    public static void erakutsi(Animalia animalia,
                                ObservableList<Animalia> zerrenda,
                                Runnable eguneratu) {

        Stage stage = new Stage();
        stage.setTitle(animalia.getIzena() + " - " + animalia.getEspezia());

        // ---------- Irudia ----------
        ImageView irudiView = new ImageView();
        irudiView.setFitWidth(240);
        irudiView.setFitHeight(240);
        irudiView.setPreserveRatio(true);

        Label placeholder = new Label("Irudiarik ez");
        placeholder.setPrefSize(240, 240);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setStyle("-fx-text-fill: #888; -fx-border-color: #ccc; -fx-border-width: 1;");

        StackPane irudiEremua = new StackPane(placeholder, irudiView);
        kargatuIrudia(irudiView, placeholder, animalia.getArgazkia());

        Button btnIrudia = new Button("Irudia aldatu");

        VBox irudiPanel = new VBox(10, irudiEremua, btnIrudia);
        irudiPanel.setAlignment(Pos.TOP_CENTER);
        irudiPanel.setPadding(new Insets(10));

        // ---------- Datuak ----------
        TextField txtAdina = new TextField(String.valueOf(animalia.getAdina()));
        TextField txtPisua = new TextField(String.valueOf(animalia.getPisua()));
        CheckBox chkTxertatua = new CheckBox("Txertatuta dago");
        chkTxertatua.setSelected(animalia.isTxertatuta());
        CheckBox chkAdoptatuta = new CheckBox("Adoptatuta dago");
        chkAdoptatuta.setSelected(animalia.isAdoptatuta());
        TextArea txtOharrak = new TextArea(animalia.getOharrak());
        txtOharrak.setPrefRowCount(4);

        GridPane datuak = new GridPane();
        datuak.setHgap(10);
        datuak.setVgap(10);
        datuak.setPadding(new Insets(10));
        datuak.addRow(0, new Label("Izena:"), new Label(animalia.getIzena()));
        datuak.addRow(1, new Label("Espezia:"), new Label(animalia.getEspezia()));
        datuak.addRow(2, new Label("Adina:"), txtAdina);
        datuak.addRow(3, new Label("Pisua:"), txtPisua);
        datuak.addRow(4, new Label(""), chkTxertatua);
        datuak.addRow(5, new Label(""), chkAdoptatuta);
        datuak.addRow(6, new Label("Oharrak:"), txtOharrak);
        GridPane.setHgrow(txtOharrak, Priority.ALWAYS);

        // ---------- Mezua ----------
        Label mezua = new Label();

        // ---------- Botoiak ----------
        Button btnGorde = new Button("Gorde");
        Button btnEzabatu = new Button("Ezabatu");
        Button btnItxi = new Button("Itxi");
        btnGorde.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");

        HBox botoiak = new HBox(10, btnGorde, btnEzabatu, btnItxi);
        botoiak.setAlignment(Pos.CENTER_RIGHT);
        botoiak.setPadding(new Insets(10));

        // ---------- Gorde ----------
        btnGorde.setOnAction(e -> {
            try {
                int adina = Integer.parseInt(txtAdina.getText().trim());
                double pisua = Double.parseDouble(txtPisua.getText().trim().replace(',', '.'));
                if (adina < 0 || pisua <= 0) throw new NumberFormatException();

                animalia.setAdina(adina);
                animalia.setPisua(pisua);
                animalia.setTxertatuta(chkTxertatua.isSelected());
                animalia.setOharrak(txtOharrak.getText());
                // ALDAKETA BERRIA: dopzio-egoera aldatzeko
                if (animalia.isAdoptatuta() != chkAdoptatuta.isSelected()) {
                    animalia.aldatuAdopzioa();
                }

                AnimaliaFitxategia.gordeGuztiak(zerrenda);
                eguneratu.run();
                mezua.setText("Gordeta.");
                mezua.setStyle("-fx-text-fill: green;");
            } catch (NumberFormatException ex) {
                mezua.setText("Adinak eta pisuak zenbaki positiboak izan behar dute.");
                mezua.setStyle("-fx-text-fill: red;");
            } catch (IOException ex) {
                mezua.setText("Errorea fitxategia gordetzean.");
                mezua.setStyle("-fx-text-fill: red;");
            }
        });

        // ---------- Ezabatu ----------
        btnEzabatu.setOnAction(e -> {
            Alert galdera = new Alert(Alert.AlertType.CONFIRMATION,
                    animalia.getIzena() + " ezabatu nahi duzu?",
                    ButtonType.YES, ButtonType.NO);
            galdera.setHeaderText(null);
            if (galdera.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;
            try {
                zerrenda.remove(animalia);
                AnimaliaFitxategia.gordeGuztiak(zerrenda);
                eguneratu.run();
                stage.close();
            } catch (IOException ex) {
                mezua.setText("Errorea fitxategia eguneratzean.");
                mezua.setStyle("-fx-text-fill: red;");
            }
        });

        // ---------- Itxi ----------
        btnItxi.setOnAction(e -> stage.close());

        // ---------- Irudia aldatu ----------
        btnIrudia.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Aukeratu irudia");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Irudiak", "*.png", "*.jpg", "*.jpeg"));

            File hautatua = fc.showOpenDialog(stage);
            if (hautatua == null) return;

            try {
                if (!Files.exists(IRUDI_KARPETA)) Files.createDirectories(IRUDI_KARPETA);

                String izenaFitx = hautatua.getName();
                String luzapena = izenaFitx.substring(izenaFitx.lastIndexOf('.'));
                String oinarria = animalia.getIzena().replaceAll("[^a-zA-Z0-9]", "_");
                String izenBerria = oinarria + "_" + System.currentTimeMillis() + luzapena;
                Path helburua = IRUDI_KARPETA.resolve(izenBerria);

                Files.copy(hautatua.toPath(), helburua, StandardCopyOption.REPLACE_EXISTING);

                animalia.setArgazkia(helburua.toString().replace("\\", "/"));
                AnimaliaFitxategia.gordeGuztiak(zerrenda);
                kargatuIrudia(irudiView, placeholder, animalia.getArgazkia());
                eguneratu.run();
                mezua.setText("Irudia aldatuta.");
                mezua.setStyle("-fx-text-fill: green;");
            } catch (IOException ex) {
                mezua.setText("Errorea irudia kopiatzean.");
                mezua.setStyle("-fx-text-fill: red;");
            }
        });

        // ---------- Diseinua ----------
        HBox goikoa = new HBox(20, irudiPanel, datuak);
        goikoa.setPadding(new Insets(15));

        //Datuak ongi ikusi daitezen: izena: ____ , pisua: _____
        // Restricciones de columna: la 0 con ancho mínimo, la 1 que crezca
        ColumnConstraints colEtiketak = new ColumnConstraints();
        colEtiketak.setMinWidth(90);         // suficiente para "Oharrak:"

        ColumnConstraints colBalioak = new ColumnConstraints();
        colBalioak.setHgrow(Priority.ALWAYS);

        datuak.getColumnConstraints().addAll(colEtiketak, colBalioak);

        VBox root = new VBox(10, goikoa, mezua, botoiak);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 620, 420));
        stage.initModality(Modality.APPLICATION_MODAL);   // modal: blokeatu nagusia
        stage.show();
    }

    // Irudia ImageView-era kargatzen du. Bidea hutsik badago edo fitxategia
    // existitzen ez bada, placeholder-a erakusten du.
    private static void kargatuIrudia(ImageView iv, Label ph, String bidea) {
        if (bidea != null && !bidea.isBlank()) {
            File f = new File(bidea);
            if (f.exists()) {
                iv.setImage(new Image(f.toURI().toString()));
                iv.setVisible(true);
                ph.setVisible(false);
                return;
            }
        }
        iv.setImage(null);
        iv.setVisible(false);
        ph.setVisible(true);
    }
}