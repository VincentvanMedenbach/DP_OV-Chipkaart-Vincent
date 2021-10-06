package school.oop.impl;

import school.oop.model.OVChipkaart;
import school.oop.model.Product;
import school.oop.model.Reiziger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OVChipkaartDAO {
    //Todo find ovchipkaarten bij product
    //Todo crud functionality
    OVChipkaart findById(int id) throws SQLException;

    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;

    ArrayList<OVChipkaart> findByProduct(int productNummer) throws SQLException;

    void save(OVChipkaart ovChipkaart) throws SQLException;

    void update(OVChipkaart ovChipkaart) throws SQLException;

    void delete(OVChipkaart ovChipkaart) throws SQLException;

}
