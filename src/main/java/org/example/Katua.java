package org.example;

public class Katua extends Animalia {

    public Katua(String izena, int adina, double pisua,
            boolean txertatuta, boolean eskuragarri, String oharrak,String argazkia) {
        super(izena, adina, pisua, txertatuta, eskuragarri, oharrak, argazkia);
    }

    @Override
    public String getEspezia() {
        return "Katua";
    }
}
