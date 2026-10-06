package org.example;

public abstract class Animalia {

    private final String izena;
    private int adina;
    private double pisua;
    private boolean txertatuta;
    private boolean eskuragarri;   // true = adoptatu gabe, false = adoptatuta
    private String oharrak;
    private String argazkia;

    protected Animalia(String izena, int adina, double pisua,
                       boolean txertatuta, boolean eskuragarri, String oharrak, String argazkia) {
        this.izena = izena;
        this.adina = adina;
        this.pisua = pisua;
        this.txertatuta = txertatuta;
        this.eskuragarri = eskuragarri;
        this.oharrak = oharrak;
        this.argazkia = argazkia;

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
    public String getArgazkia() { return argazkia; }

    // ---------- Setterrak ----------
    public void setAdina(int adina) { this.adina = adina; }
    public void setPisua(double pisua) { this.pisua = pisua; }
    public void setTxertatuta(boolean txertatuta) { this.txertatuta = txertatuta; }
    public void setOharrak(String oharrak) { this.oharrak = oharrak; }
    public void setArgazkia(String argazkia) { this.argazkia = argazkia; }

    // Animalia adoptatzen da: eskuragarri = false
    public void adoptatu() {
        this.eskuragarri = false;
    }

    // ---------- Espeziearen arabera objektu egokia sortzen du ----------
    public static Animalia sortu(String espezia, String izena, int adina, double pisua,
                                 boolean txertatuta, boolean eskuragarri, String oharrak, String argazkia) {
        return switch (espezia) {
            case "Txakurra" -> new Txakurra(izena, adina, pisua, txertatuta, eskuragarri, oharrak, argazkia);
            case "Katua" -> new Katua(izena, adina, pisua, txertatuta, eskuragarri, oharrak,argazkia);
            case "Untxia" -> new Untxia(izena, adina, pisua, txertatuta, eskuragarri, oharrak, argazkia);
            default -> new BesteBat(izena, adina, pisua, txertatuta, eskuragarri, oharrak, argazkia);
        };
    }

    // ---------- CSV ----------
    // Formatua: espezia;izena;adina;pisua;txertatuta;eskuragarri;oharrak;argazkia
    public String csvLerroa() {
        return String.join(";",
                getEspezia(),
                garbitu(izena),
                String.valueOf(adina),
                String.valueOf(pisua),
                String.valueOf(txertatuta),
                String.valueOf(eskuragarri),
                garbitu(oharrak),
                garbitu(argazkia));
    }

    public static Animalia csvLerrotik(String lerroa) {
        String[] p = lerroa.split(";", -1);
        return sortu(p[0], p[1], Integer.parseInt(p[2]), Double.parseDouble(p[3]),
                Boolean.parseBoolean(p[4]), Boolean.parseBoolean(p[5]),
                p[6].replace("\\n", "\n"),
                p[7].replace("\\n", "\n"));
    }

    // ; eta lerro-jauziak kentzen ditu, CSV egitura ez apurtzeko
    private static String garbitu(String testua) {
        return testua.replace(";", ",").replace("\r", "").replace("\n", "\\n");
    }
}
