package school.oop.impl;

import school.oop.model.OVChipkaart;
import school.oop.model.Product;
import school.oop.model.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;

}
