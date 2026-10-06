package org.example;

public class Untxia extends Animalia {

    public Untxia(String izena, int adina, double pisua,
            boolean txertatuta, boolean eskuragarri, String oharrak,String argazkia) {
        super(izena, adina, pisua, txertatuta, eskuragarri, oharrak,argazkia);
    }

    @Override
    public String getEspezia() {
        return "Untxia";
    }
}
