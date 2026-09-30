package org.example;

public abstract class Animalia {

    private final String izena;
    private int adina;
    private double pisua;
    private boolean txertatuta;
    private boolean eskuragarri;   // true = adoptatu gabe, false = adoptatuta
    private String oharrak;

    protected Animalia(String izena, int adina, double pisua,
                       boolean txertatuta, boolean eskuragarri, String oharrak) {
        this.izena = izena;
        this.adina = adina;
        this.pisua = pisua;
        this.txertatuta = txertatuta;
        this.eskuragarri = eskuragarri;
        this.oharrak = oharrak;
    }

    // Azpiklaseek beren espezia itzultzen dute
    public abstract String getEspezia();

    // ---------- Getterrak ----------
    public String getIzena() { return izena; }
    public int getAdina() { return adina; }
    public double getPisua() { return pisua; }
    public boolean isTxertatuta() { return txertatuta; }
    public boolean isEskuragarri() { return eskuragarri; }
    public boolean isAdoptatuta() { return !eskuragarri; }
    public String getOharrak() { return oharrak; }

    // ---------- Setterrak ----------
    public void setAdina(int adina) { this.adina = adina; }
    public void setPisua(double pisua) { this.pisua = pisua; }
    public void setTxertatuta(boolean txertatuta) { this.txertatuta = txertatuta; }
    public void setOharrak(String oharrak) { this.oharrak = oharrak; }

    // Animalia adoptatzen da: eskuragarri = false
    public void adoptatu() {
        this.eskuragarri = false;
    }

    // ---------- Espeziearen arabera objektu egokia sortzen du ----------
    public static Animalia sortu(String espezia, String izena, int adina, double pisua,
                                 boolean txertatuta, boolean eskuragarri, String oharrak) {
        return switch (espezia) {
            case "Txakurra" -> new Txakurra(izena, adina, pisua, txertatuta, eskuragarri, oharrak);
            case "Katua" -> new Katua(izena, adina, pisua, txertatuta, eskuragarri, oharrak);
            case "Untxia" -> new Untxia(izena, adina, pisua, txertatuta, eskuragarri, oharrak);
            default -> new BesteBat(izena, adina, pisua, txertatuta, eskuragarri, oharrak);
        };
    }

    // ---------- CSV ----------
    // Formatua: espezia;izena;adina;pisua;txertatuta;eskuragarri;oharrak
    public String csvLerroa() {
        return String.join(";",
                getEspezia(),
                garbitu(izena),
                String.valueOf(adina),
                String.valueOf(pisua),
                String.valueOf(txertatuta),
                String.valueOf(eskuragarri),
                garbitu(oharrak));
    }

    public static Animalia csvLerrotik(String lerroa) {
        String[] p = lerroa.split(";", -1);
        return sortu(p[0], p[1], Integer.parseInt(p[2]), Double.parseDouble(p[3]),
                Boolean.parseBoolean(p[4]), Boolean.parseBoolean(p[5]),
                p[6].replace("\\n", "\n"));
    }

    // ; eta lerro-jauziak kentzen ditu, CSV egitura ez apurtzeko
    private static String garbitu(String testua) {
        return testua.replace(";", ",").replace("\r", "").replace("\n", "\\n");
    }
}
