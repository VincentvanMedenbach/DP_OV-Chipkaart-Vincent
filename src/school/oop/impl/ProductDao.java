package school.oop.impl;

import school.oop.model.OVChipkaart;
import school.oop.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductDao {
    Product findById(int id) throws SQLException;

    ArrayList<Product> findAll() throws SQLException;

    void save(Product product) throws SQLException;

    void update(Product product) throws SQLException;

    void delete(Product product) throws SQLException;

    ArrayList<Product> findByOvChipkaart(OVChipkaart ovChipkaart) throws SQLException;
}
