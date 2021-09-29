package school.oop.impl;

import school.oop.model.OVChipkaart;
import school.oop.model.OVChipkaartProduct;
import school.oop.model.Product;

import java.sql.*;
import java.util.ArrayList;

public class ProductDaoPsql implements ProductDao {
    Connection conn;

    public ProductDaoPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Product findById(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM product WHERE product_nummer=?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return new Product(
                rs.getInt("product_nummer"),
                rs.getString("naam"),
                rs.getString("beschrijving"),
                rs.getInt("prijs")
        );
    }

    @Override
    public ArrayList<Product> findAll() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM product");
        ResultSet rs = stmt.executeQuery();
        ArrayList<Product> producten = new ArrayList<>();
        while (rs.next()) {
            int productNummer = rs.getInt("product_nummer");
            Product product = new Product(
                    productNummer,
                    rs.getString("naam"),
                    rs.getString("beschrijving"),
                    rs.getInt("prijs")
            );
            PreparedStatement relationStmt = conn.prepareStatement("SELECT * FROM ov_chipkaart_product where product_nummer = ?");
            relationStmt.setInt(1, productNummer);
            ResultSet ovChipProducten = relationStmt.executeQuery();
            while (ovChipProducten.next()) {
                product.addOvChipkaarten(
                        ovChipProducten.getInt("kaart_nummer"),
                        ovChipProducten.getString("status"),
                        ovChipProducten.getDate("last_update")
                );
            }
            producten.add(product);
        }
        return producten;
    }

    @Override
    public void save(Product product) throws SQLException {
//        String insertString = "INSERT INTO product (product_nummer,naam,beschrijving,prijs) VALUES(DEFAULT,?,?,?)";
        String insertString = "INSERT INTO product (product_nummer,naam,beschrijving,prijs) VALUES(?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(insertString);
        stmt.setInt(1, product.getProduct_nummer());
        stmt.setString(2, product.getNaam());
        stmt.setString(3, product.getBeschrijving());
        stmt.setInt(4, product.getPrijs());
        stmt.executeUpdate();
        for (OVChipkaartProduct ovChipkaartProduct : product.getOvChipkaarten()) {//This should probably be done by ovchipcard or seperatly but this is what the assignment demands
            String insertRelationString = "INSERT INTO ov_chipkaart_product (product_nummer,kaart_nummer,status,last_update) VALUES(?,?,?,?)";
            PreparedStatement relationstmt = conn.prepareStatement(insertRelationString);
            relationstmt.setInt(1, product.getProduct_nummer());
            relationstmt.setInt(2, ovChipkaartProduct.getKaart_nummer());
            relationstmt.setString(3, ovChipkaartProduct.getStatus());
            relationstmt.setDate(4, (Date) ovChipkaartProduct.getLast_update());
            relationstmt.executeUpdate();
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        String insertString = "UPDATE product SET naam = ?, beschrijving = ?, prijs=? WHERE product_nummer=?";
        PreparedStatement stmt = conn.prepareStatement(insertString);

        stmt.setString(1, product.getNaam());
        stmt.setString(2, product.getBeschrijving());
        stmt.setInt(3, product.getPrijs());
        stmt.setInt(4, product.getProduct_nummer());
        stmt.executeUpdate();
        for (OVChipkaartProduct ovChipkaartProduct : product.getOvChipkaarten()) {//This should probably be done by ovchipcard or seperatly but this is what the assignment demands
            String insertRelationString = "INSERT INTO ov_chipkaart_product (product_nummer,kaart_nummer,status,last_update) VALUES(?,?,?,?) ON CONFLICT (product_nummer, kaart_nummer) DO UPDATE SET product_nummer = excluded.product_nummer, kaart_nummer = excluded.kaart_nummer;";
            PreparedStatement relationstmt = conn.prepareStatement(insertRelationString);
            relationstmt.setInt(1, product.getProduct_nummer());
            relationstmt.setInt(2, ovChipkaartProduct.getKaart_nummer());
            relationstmt.setString(3, ovChipkaartProduct.getStatus());
            relationstmt.setDate(4, (Date) ovChipkaartProduct.getLast_update());
            relationstmt.executeUpdate();
        }

    }

    @Override
    public void delete(Product product) throws SQLException {
        String relationString = "DELETE FROM ov_chipkaart_product WHERE product_nummer=?"; //Wouldn't this ussually be done by enabling cascade on the database?
        PreparedStatement relationStatement = conn.prepareStatement(relationString);
        relationStatement.setInt(1, product.getProduct_nummer());
        relationStatement.executeUpdate();
        String insertString = "DELETE FROM product WHERE product_nummer=?";
        PreparedStatement stmt = conn.prepareStatement(insertString);

        stmt.setInt(1, product.getProduct_nummer());
        stmt.executeUpdate();
        return;
    }

    @Override
    public ArrayList<Product> findByOvChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ov_chipkaart_product INNER JOIN product ON product.product_nummer = ov_chipkaart_product.product_nummer WHERE kaart_nummer=?  ");
        stmt.setInt(1, ovChipkaart.getKaart_nummer());
        ResultSet rs = stmt.executeQuery();
        ArrayList<Product> producten = new ArrayList<Product>();
        while (rs.next()) {
            producten.add(new Product(
                    rs.getInt("product_nummer"),
                    rs.getString("naam"),
                    rs.getString("beschrijving"),
                    rs.getInt("prijs")
            ));
        }
        return producten;
    }
}
