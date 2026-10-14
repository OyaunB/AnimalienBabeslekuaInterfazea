package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public class KonexioaTest {
    public static void main(String[] args) {
        try (Connection conn = Konexioa.konektatu()) {
            System.out.println("✅ Konexioa ondo!");
            System.out.println("Produktua: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("Bertsioa: " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("Katalogoa: " + conn.getCatalog());
        } catch (SQLException e) {
            System.out.println("❌ Errorea konexioan:");
            e.printStackTrace();
        }
    }
}