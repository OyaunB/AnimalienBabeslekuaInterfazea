package org.example.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.model.Animalia;
import org.example.service.AnimaliaService;
import org.example.view.AnimaliaXehetasuna;
import org.example.view.MainView;

import java.sql.SQLException;

public class MainController {

    private final MainView view;
    private final AnimaliaService service;
    private final Stage stage;

    public MainController(MainView view, AnimaliaService service, Stage stage) {
        this.view = view;
        this.service = service;
        this.stage = stage;

        kargatuAnimaliak();
        konektatuEkintzak();
    }

    // ---------- Hasierako karga ----------
    private void kargatuAnimaliak() {
        try {
            view.getAnimaliak().addAll(service.lortuGuztiak());
        } catch (SQLException ex) {
            view.erakutsiMezua("Errorea animaliak kargatzean: " + ex.getMessage(), true);
        }
    }

    // ---------- Botoien ekintzak ----------
    private void konektatuEkintzak() {
        view.getBtnIrten().setOnAction(e -> stage.close());
        view.getBtnZerrenda().setOnAction(e -> view.getTaula().requestFocus());

        view.getBtnBerria().setOnAction(e -> adoptatu());
        view.getBtnTxertatu().setOnAction(e -> txertatu());
        view.getBtnEzabatu().setOnAction(e -> ezabatu());
        view.getBtnIkusi().setOnAction(e -> ikusi());
        view.getBtnGorde().setOnAction(e -> gorde());
        view.getBtnGarbitu().setOnAction(e -> {
            view.garbituFormularioa();
            view.erakutsiMezua("", false);
        });
    }

    private void adoptatu() {
        Animalia a = view.getTaula().getSelectionModel().getSelectedItem();
        if (a == null) {
            view.erakutsiMezua("Aukeratu taulan animalia bat.", true);
            return;
        }
        try {
            boolean zegoen = a.isAdoptatuta();
            service.aldatuAdopzioa(a);
            view.getTaula().refresh();
            view.eguneratuBotoienTestua(a);
            view.erakutsiMezua(a.getIzena() + (zegoen ? " desadoptatu da." : " adoptatu da."), false);
        } catch (SQLException ex) {
            view.erakutsiMezua("Errorea datu-basean eguneratzean.", true);
        }
    }

    private void txertatu() {
        Animalia a = view.getTaula().getSelectionModel().getSelectedItem();
        if (a == null) {
            view.erakutsiMezua("Aukeratu taulan txertatu nahi duzun animalia.", true);
            return;
        }
        try {
            service.aldatuTxertaketa(a);
            view.getTaula().refresh();
            view.eguneratuBotoienTestua(a);
            String egoera = a.isTxertatuta() ? "txertatuta" : "txertatu gabe";
            view.erakutsiMezua(a.getIzena() + " orain " + egoera + " dago.", false);
        } catch (SQLException ex) {
            view.erakutsiMezua("Errorea datu-basean eguneratzean.", true);
        }
    }

    private void ezabatu() {
        Animalia a = view.getTaula().getSelectionModel().getSelectedItem();
        if (a == null) {
            view.erakutsiMezua("Aukeratu taulan ezabatu nahi duzun animalia.", true);
            return;
        }
        Alert galdera = new Alert(Alert.AlertType.CONFIRMATION,
                a.getIzena() + " ezabatu nahi duzu?",
                ButtonType.YES, ButtonType.NO);
        galdera.setHeaderText(null);
        if (galdera.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;

        try {
            service.ezabatuAnimalia(a.getId());
            view.getAnimaliak().remove(a);
            view.erakutsiMezua(a.getIzena() + " ezabatu da.", false);
        } catch (SQLException ex) {
            view.erakutsiMezua("Errorea datu-basean ezabatzean.", true);
        }
    }

    private void ikusi() {
        Animalia a = view.getTaula().getSelectionModel().getSelectedItem();
        if (a == null) {
            view.erakutsiMezua("Aukeratu taulan ikusi nahi duzun animalia.", true);
            return;
        }
        AnimaliaXehetasuna.erakutsi(a, view.getAnimaliak(), view.getTaula()::refresh);
    }

    private void gorde() {
        // 1. Balidazioa
        if (view.getTxtIzena().getText().isBlank() || view.getCmbEspezia().getValue() == null) {
            view.erakutsiMezua("Izena eta espeziea beharrezkoak dira.", true);
            return;
        }
        int adina;
        double pisua;
        try {
            adina = Integer.parseInt(view.getTxtAdina().getText().trim());
            pisua = Double.parseDouble(view.getTxtPisua().getText().trim().replace(',', '.'));
            if (adina < 0 || pisua <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            view.erakutsiMezua("Adinak eta pisuak zenbaki positiboak eta osoak izan behar dute.", true);
            return;
        }

        // 2. Animalia sortu
        Animalia animalia = Animalia.sortu(
                view.getCmbEspezia().getValue(),
                view.getTxtIzena().getText().trim(),
                adina, pisua,
                view.getChkTxertatua().isSelected(),
                true,
                view.getTxtOharrak().getText(),
                "");

        // 3. Gorde
        try {
            service.gehituAnimalia(animalia);
            view.getAnimaliak().add(animalia);
            view.erakutsiMezua(animalia.getIzena() + " gorde da.", false);
            view.garbituFormularioa();
        } catch (SQLException ex) {
            view.erakutsiMezua("Errorea datu-basean gordetzean.", true);
        }
    }
}