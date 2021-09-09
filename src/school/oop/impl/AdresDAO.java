package school.oop.impl;

import school.oop.model.Adres;
import school.oop.model.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    boolean save(Adres adres) throws SQLException;

    boolean update(Adres adres) throws SQLException;

    boolean delete(Adres adres) throws SQLException;

    Adres findByReiziger(Reiziger reiziger) throws SQLException;

    List<Adres> findAll() throws SQLException;

    Adres findById(int id) throws SQLException;
}
