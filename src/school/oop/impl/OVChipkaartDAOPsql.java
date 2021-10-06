package school.oop.impl;

import school.oop.model.OVChipkaart;
import school.oop.model.OVChipkaartProduct;
import school.oop.model.Product;
import school.oop.model.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    Connection conn;
    AdresDAO adao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    //Todo find ovchipkaarten bij product
    //Todo crud functionality
    @Override
    public OVChipkaart findById(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE kaart_nummer= ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        rs.next();

        OVChipkaart ovChipkaart = new OVChipkaart(rs.getInt("kaart_nummer"), rs.getDate("geldig_tot"), rs.getInt("klasse"), rs.getInt("saldo"), rs.getInt("reiziger_id"));
        PreparedStatement relationStmt = conn.prepareStatement("SELECT * FROM ov_chipkaart_product where kaart_nummer = ?");
        relationStmt.setInt(1, ovChipkaart.getKaart_nummer());
        ResultSet ovChipProducten = relationStmt.executeQuery();
        while (ovChipProducten.next()) {
            ovChipkaart.addProducten(
                    ovChipProducten.getInt("kaart_nummer"),
                    ovChipProducten.getString("status"),
                    ovChipProducten.getDate("last_update")
            );
        }


        rs.close();
        return ovChipkaart;
    }


    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        ArrayList<OVChipkaart> returnList = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id= ?");
        stmt.setInt(1, reiziger.getId());

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart(rs.getInt("kaart_nummer"), rs.getDate("geldig_tot"), rs.getInt("klasse"), rs.getInt("saldo"), rs.getInt("reiziger_id"));
            PreparedStatement relationStmt = conn.prepareStatement("SELECT * FROM ov_chipkaart_product where kaart_nummer = ?");
            relationStmt.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet ovChipProducten = relationStmt.executeQuery();
            while (ovChipProducten.next()) {
                ovChipkaart.addProducten(
                        ovChipProducten.getInt("kaart_nummer"),
                        ovChipProducten.getString("status"),
                        ovChipProducten.getDate("last_update")
                );
            }
            returnList.add(ovChipkaart);

        }
        rs.close();
        return returnList;
    }

    @Override
    public ArrayList<OVChipkaart> findByProduct(int productNummer) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ov_chipkaart_product INNER JOIN ov_chipkaart ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer WHERE product_nummer=?  ");
        stmt.setInt(1, productNummer);
        ResultSet rs = stmt.executeQuery();
        ArrayList<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>();
        while (rs.next()) {
            ovChipkaarten.add(new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getInt("saldo"),
                    rs.getInt("reiziger_id")
            ));
        }
        return ovChipkaarten;
    }

    @Override
    public void save(OVChipkaart ovChipkaart) throws SQLException {
        String insertString = "INSERT INTO ov_chipkaart (kaart_nummer,geldig_tot,klasse,saldo,reiziger_id) VALUES(?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(insertString);
        stmt.setInt(1, ovChipkaart.getKaart_nummer());
        stmt.setDate(2, new java.sql.Date(ovChipkaart.getGeldig_tot().getTime()));
        stmt.setInt(3, ovChipkaart.getKlasse());
        stmt.setInt(4, ovChipkaart.getSaldo());
        stmt.setInt(5, ovChipkaart.getReiziger().getId());
        stmt.executeUpdate();
        for (OVChipkaartProduct ovChipkaartProduct : ovChipkaart.getProducten()) {//This should probably be done by ovchipcard or seperatly but this is what the assignment demands
            String insertRelationString = "INSERT INTO ov_chipkaart_product (product_nummer,kaart_nummer,status,last_update) VALUES(?,?,?,?)";
            PreparedStatement relationstmt = conn.prepareStatement(insertRelationString);
            relationstmt.setInt(1, ovChipkaartProduct.getProduct_nummer());
            relationstmt.setInt(2, ovChipkaartProduct.getKaart_nummer());
            relationstmt.setString(3, ovChipkaartProduct.getStatus());
            relationstmt.setDate(4, (Date) ovChipkaartProduct.getLast_update());
            relationstmt.executeUpdate();
        }

    }

    @Override
    public void update(OVChipkaart ovChipkaart) throws SQLException {
        String insertString = "UPDATE ov_chipkaart SET geldig_tot=?,klasse=?,saldo=?,reiziger_id=? WHERE kaart_nummer =?";
        PreparedStatement stmt = conn.prepareStatement(insertString);
        stmt.setDate(1, new java.sql.Date(ovChipkaart.getGeldig_tot().getTime()));
        stmt.setInt(2, ovChipkaart.getKlasse());
        stmt.setInt(3, ovChipkaart.getSaldo());
        stmt.setInt(4, ovChipkaart.getReiziger().getId());
        stmt.setInt(5, ovChipkaart.getKaart_nummer());
        stmt.executeUpdate();
    }

    @Override
    public void delete(OVChipkaart ovChipkaart) throws SQLException {
        String relationString = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer=?"; //Wouldn't this ussually be done by enabling cascade on the database?
        PreparedStatement relationStatement = conn.prepareStatement(relationString);
        relationStatement.setInt(1, ovChipkaart.getKaart_nummer());
        relationStatement.executeUpdate();
        String insertString = "DELETE FROM ov_chipkaart WHERE kaart_nummer=?";
        PreparedStatement stmt = conn.prepareStatement(insertString);
        stmt.setInt(1, ovChipkaart.getKaart_nummer());
        stmt.executeUpdate();
    }

}
