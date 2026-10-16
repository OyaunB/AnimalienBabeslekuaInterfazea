package org.example.service;

import org.example.dao.AnimaliaDAO;
import org.example.model.Animalia;

import java.sql.SQLException;
import java.util.List;

public class AnimaliaService {

    private final AnimaliaDAO dao;

    public AnimaliaService() {
        this.dao = new AnimaliaDAO();
    }

    /** Animaliak zerrenda bat itzultzen du. */
    public List<Animalia> lortuGuztiak() throws SQLException {
        return dao.findAll();
    }

    /** Animalia berria gehitzen du. */
    public void gehituAnimalia(Animalia a) throws SQLException {
        dao.insert(a);
    }

    /** Animalia baten datuak eguneratzen ditu. */
    public void eguneratuAnimalia(Animalia a) throws SQLException {
        dao.update(a);
    }

    /** Animalia bat ezabatzen du bere id-a erabilita. */
    public void ezabatuAnimalia(int id) throws SQLException {
        dao.delete(id);
    }

    /** Adopzio-egoera aldatzen du eta datu-basean gordetzen du. */
    public void aldatuAdopzioa(Animalia a) throws SQLException {
        a.aldatuAdopzioa();
        dao.update(a);
    }

    /** Txertaketa-egoera aldatzen du eta datu-basean gordetzen du. */
    public void aldatuTxertaketa(Animalia a) throws SQLException {
        a.aldatuTxertatuta();
        dao.update(a);
    }

    /** Animalia guztien soinuak biltzen ditu. */
    public String lortuSoinuGuztiak(List<Animalia> animaliak) {
        StringBuilder sb = new StringBuilder();
        for (Animalia a : animaliak) {
            sb.append(a.getIzena()).append(": ").append(a.eginSoinua()).append("\n");
        }
        return sb.toString();
    }
}