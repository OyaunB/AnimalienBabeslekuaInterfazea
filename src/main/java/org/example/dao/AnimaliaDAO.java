package org.example.dao;

import org.example.model.Animalia;
import org.example.Konexioa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimaliaDAO {

    // ---------- READ ----------
    public List<Animalia> findAll() throws SQLException {
        List<Animalia> emaitza = new ArrayList<>();
        String sql = "SELECT id, izena, espezia, adina, pisua, txertatuta, adoptatuta, oharrak, argazkia FROM animaliak";

        //__________________________
        try (Connection conn = Konexioa.konektatu();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                emaitza.add(errenkadatik(rs));
            }
        }
        return emaitza;
    }

    // ---------- CREATE ----------
    public void insert(Animalia a) throws SQLException {
        String sql = "INSERT INTO animaliak (izena, espezia, adina, pisua, txertatuta, adoptatuta, oharrak, argazkia) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Konexioa.konektatu();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getIzena());
            ps.setString(2, a.getEspezia());
            ps.setInt(3, a.getAdina());
            ps.setDouble(4, a.getPisua());
            ps.setBoolean(5, a.isTxertatuta());
            ps.setBoolean(6, a.isAdoptatuta());
            ps.setString(7, a.getOharrak());
            ps.setString(8, a.getArgazkia());

            ps.executeUpdate();

            // Recuperar el id autogenerado y asignarlo al objeto
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
        }
    }

    // ---------- UPDATE ----------
    public void update(Animalia a) throws SQLException {
        String sql = "UPDATE animaliak SET izena=?, espezia=?, adina=?, pisua=?, "
                + "txertatuta=?, adoptatuta=?, oharrak=?, argazkia=? WHERE id=?";

        try (Connection conn = Konexioa.konektatu();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getIzena());
            ps.setString(2, a.getEspezia());
            ps.setInt(3, a.getAdina());
            ps.setDouble(4, a.getPisua());
            ps.setBoolean(5, a.isTxertatuta());
            ps.setBoolean(6, a.isAdoptatuta());
            ps.setString(7, a.getOharrak());
            ps.setString(8, a.getArgazkia());
            ps.setInt(9, a.getId());

            ps.executeUpdate();
        }
    }

    // ---------- DELETE ----------
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM animaliak WHERE id=?";

        try (Connection conn = Konexioa.konektatu();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ---------- Helper ----------
    private Animalia errenkadatik(ResultSet rs) throws SQLException {
        Animalia a = Animalia.sortu(
                rs.getString("espezia"),
                rs.getString("izena"),
                rs.getInt("adina"),
                rs.getDouble("pisua"),
                rs.getBoolean("txertatuta"),
                !rs.getBoolean("adoptatuta"),
                rs.getString("oharrak"),
                rs.getString("argazkia"));

        a.setId(rs.getInt("id"));
        return a;
    }
}