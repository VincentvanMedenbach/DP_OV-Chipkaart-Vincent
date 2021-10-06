package school.oop.impl;

import school.oop.model.Reiziger;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    Connection conn;
    AdresDAO adao;
    OVChipkaartDAO ovdao;
    public ReizigerDAOPsql(Connection conn, OVChipkaartDAO ovdao) {
        this.conn = conn;
        this.adao = new AdresDAOPsql(conn, this);
        this.ovdao = ovdao;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {

        Statement stmt = conn.createStatement();
        String insertString = "INSERT INTO reiziger VALUES('"
                + reiziger.getId() + "','"
                + reiziger.getVoorletters() + "','"
                + reiziger.getTussenvoegsels() + "','"
                + reiziger.getAchternaam() + "','"
                + reiziger.getGeboortedatum().toString()
                + "')";


        stmt.executeUpdate(insertString);
        return true;

    }


    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String insertString = "UPDATE reiziger SET voorletters = ?, tussenvoegsels = ?, achternaam=?, geboortedatum=?";
        PreparedStatement stmt = conn.prepareStatement(insertString);

        stmt.setString(1, reiziger.getVoorletters());
        stmt.setString(2, reiziger.getTussenvoegsels());
        stmt.setString(3, reiziger.getAchternaam());
        stmt.setDate(4, reiziger.getGeboortedatum());
        return stmt.executeUpdate() == 1;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        PreparedStatement stmtRelation = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE reiziger_id=?");
        stmtRelation.setInt(1, reiziger.getId());
         stmtRelation.executeUpdate();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id=?");
        stmt.setInt(1, reiziger.getId());
        int deleted = stmt.executeUpdate();
        return deleted == 1;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id=?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return new Reiziger(
                rs.getInt("reiziger_id"), rs.getString("voorletters"), rs.getString("tussenvoegsel"), rs.getString("achternaam"), java.sql.Date.valueOf(rs.getString("geboortedatum"))
                , adao.findByReiziger(
                new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"), rs.getString("tussenvoegsel"), rs.getString("achternaam"), java.sql.Date.valueOf(rs.getString("geboortedatum")))
        ));


    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException, ParseException {
        ArrayList<Reiziger> returnList = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum= ?");
        stmt.setDate(1, java.sql.Date.valueOf(datum));

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            returnList.add(new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"), rs.getString("tussenvoegsel"), rs.getString("achternaam"), java.sql.Date.valueOf(rs.getString("geboortedatum"))));
        }
        rs.close();
        return returnList;
    }

    @Override
    public ArrayList<Reiziger> findAll() throws SQLException {
        ArrayList<Reiziger> returnList = new ArrayList<>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM reiziger");
        while (rs.next()) {
            Reiziger reiziger = new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"), rs.getString("tussenvoegsel"), rs.getString("achternaam"), java.sql.Date.valueOf(rs.getString("geboortedatum")));
            reiziger.setAdres(adao.findByReiziger(reiziger));
            reiziger.setOvchipkaarten(ovdao.findByReiziger(reiziger));
            returnList.add(reiziger);
        }
        rs.close();
        return returnList;
    }
}
