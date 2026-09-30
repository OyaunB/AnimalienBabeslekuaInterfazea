package org.example;

public class BesteBat extends Animalia {

    public BesteBat(String izena, int adina, double pisua,
            boolean txertatuta, boolean eskuragarri, String oharrak) {
        super(izena, adina, pisua, txertatuta, eskuragarri, oharrak);
    }

    @Override
    public String getEspezia() {
        return "Beste bat";
    }
}
