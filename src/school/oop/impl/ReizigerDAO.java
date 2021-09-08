package school.oop.impl;

import school.oop.model.Reiziger;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface ReizigerDAO {
    boolean save(Reiziger reiziger) throws SQLException;
    boolean update(Reiziger reiziger) throws SQLException;
    boolean delete(Reiziger reiziger) throws SQLException;
    Reiziger findById(int id) throws SQLException;
    List<Reiziger> findByGbdatum(String datum) throws SQLException, ParseException;
    List<Reiziger> findAll() throws SQLException;

}
