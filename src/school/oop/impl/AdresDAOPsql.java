package school.oop.impl;

import school.oop.model.Adres;
import school.oop.model.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private final Connection conn;
    private final ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn, ReizigerDAO rdao) {
        this.conn = conn;
        this.rdao = rdao;
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        String insertString = "INSERT INTO adres VALUES(?,?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(insertString);
        stmt.setInt(1, adres.getId());
        stmt.setString(2, adres.getPostcode());
        stmt.setString(3, adres.getHuisnummer());
        stmt.setString(4, adres.getStraat());
        stmt.setString(5, adres.getWoonplaats());
        stmt.setInt(6, adres.getReizigerId());
        stmt.executeUpdate();
        return true;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        String insertString = "UPDATE adres SET postcode = ?, huisnummer = ?, straat=?, woonplaats=?, reiziger_id=? WHERE adres_id =?";
        PreparedStatement stmt = conn.prepareStatement(insertString);
        stmt.setString(1, adres.getPostcode());
        stmt.setString(2, adres.getHuisnummer());
        stmt.setString(3, adres.getStraat());
        stmt.setString(4, adres.getWoonplaats());
        stmt.setInt(5, adres.getReizigerId());
        stmt.setInt(6, adres.getId());

        return stmt.executeUpdate() == 1;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM adres WHERE adres_id=?");
        stmt.setInt(1, adres.getId());
        int deleted = stmt.executeUpdate();
        return deleted == 1;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id=?");
        stmt.setInt(1, reiziger.getId());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            return new Adres(rs.getInt("adres_id"), rs.getString("postcode"), rs.getString("huisnummer"), rs.getString("straat"), rs.getString("woonplaats"), rs.getInt("reiziger_id"));
        }
        return null;

    }

    @Override
    public Adres findById(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM adres WHERE adres_id=?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return new Adres(rs.getInt("adres_id"), rs.getString("postcode"), rs.getString("huisnummer"), rs.getString("straat"), rs.getString("woonplaats"), rs.getInt("reiziger_id"), rdao.findById(rs.getInt("reiziger_id")));
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        ArrayList<Adres> returnList = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM adres");
        while (rs.next()) {
            returnList.add(
                    new Adres(rs.getInt("adres_id"), rs.getString("postcode"), rs.getString("huisnummer"), rs.getString("straat"), rs.getString("woonplaats"), rs.getInt("reiziger_id"), rdao.findById(rs.getInt("reiziger_id"))));
        }
        rs.close();
        return returnList;
    }
}
