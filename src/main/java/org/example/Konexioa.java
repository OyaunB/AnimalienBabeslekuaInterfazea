package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class Konexioa {

        private static final String URL =
                "jdbc:mysql://127.0.0.1:3307/animalien_babeslekua"
                        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        private static final String USER = "root";
        private static final String PASSWORD = "2paag3";

        // Devuelve una conexión nueva a la base de datos.
        // IMPORTANTE: quien la llame debe cerrarla (try-with-resources).
        public static Connection konektatu() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }




