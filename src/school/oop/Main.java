package school.oop;

import school.oop.impl.ReizigerDao;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        ReizigerDao reizigerDao = new ReizigerDao();
        System.out.println("Alle reizigers:");

        for(String reiziger : reizigerDao.getReizigers()){
            System.out.println(reiziger);
        }
    }
}
