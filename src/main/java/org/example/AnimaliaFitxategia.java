package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class AnimaliaFitxategia {

    // Fitxategia proiektuaren karpetan sortuko da
    private static final Path FITXATEGIA = Path.of("animaliak.csv");

    // Fitxategiko animalia guztiak irakurtzen ditu (lerro okerrak saltatzen ditu)
    public static List<Animalia> kargatu() {
        List<Animalia> zerrenda = new ArrayList<>();
        if (!Files.exists(FITXATEGIA)) {
            return zerrenda;
        }
        try {
            for (String lerroa : Files.readAllLines(FITXATEGIA, StandardCharsets.UTF_8)) {
                if (lerroa.isBlank()) continue;
                try {
                    zerrenda.add(Animalia.csvLerrotik(lerroa));
                } catch (RuntimeException e) {
                    System.err.println("Lerro okerra saltatu da: " + lerroa);
                }
            }
        } catch (IOException e) {
            System.err.println("Errorea fitxategia irakurtzean: " + e.getMessage());
        }
        return zerrenda;
    }

    // Fitxategia osorik berridazten du zerrendako animaliekin
    public static void gordeGuztiak(List<Animalia> zerrenda) throws IOException {
        List<String> lerroak = new ArrayList<>();
        for (Animalia a : zerrenda) {
            lerroak.add(a.csvLerroa());
        }
        Files.write(FITXATEGIA, lerroak, StandardCharsets.UTF_8);
    }

    // Animalia bat fitxategiaren amaieran gehitzen du
    public static void gehitu(Animalia animalia) throws IOException {
        Files.writeString(FITXATEGIA,
                animalia.csvLerroa() + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
