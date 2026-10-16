package org.example.model;

public class Txakurra extends Animalia {

    public Txakurra(String izena, int adina, double pisua,
            boolean txertatuta, boolean eskuragarri, String oharrak, String argazkia) {
        super(izena, adina, pisua, txertatuta, eskuragarri, oharrak, argazkia);
    }

    @Override
    public String getEspezia() {
        return "Txakurra";
    }

    @Override
    public String eginSoinua() {
        return getEspezia() +"-k Uau- uau egiten du";
    }
}
