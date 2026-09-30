package org.example;

public class Untxia extends Animalia {

    public Untxia(String izena, int adina, double pisua,
            boolean txertatuta, boolean eskuragarri, String oharrak) {
        super(izena, adina, pisua, txertatuta, eskuragarri, oharrak);
    }

    @Override
    public String getEspezia() {
        return "Untxia";
    }
}
