package school.oop;

import school.oop.impl.AdresDAO;
import school.oop.impl.AdresDAOPsql;
import school.oop.impl.ReizigerDAO;
import school.oop.impl.ReizigerDAOPsql;
import school.oop.model.Adres;
import school.oop.model.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, ParseException {
        Connection conn = Database.connect();
        ReizigerDAOPsql reizigerDao = new ReizigerDAOPsql(conn);
        AdresDAOPsql adresDao = new AdresDAOPsql(conn,reizigerDao);

        System.out.println("Alle reizigers:");
        for(Reiziger reiziger : reizigerDao.findAll()){
            System.out.println(reiziger);
        }
        testReizigerDAO(reizigerDao);
        testAdresDAO(adresDao, reizigerDao);



    }
    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException, ParseException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
        System.out.println("test zoeken met id:" + rdao.findById(1));
        System.out.println( java.sql.Date.valueOf(gbdatum));
        System.out.println("test zoeken met geboortedatum:" + rdao.findByGbdatum(gbdatum));
        rdao.delete(sietske);
        System.out.println( "Dit zou nu 1 moeten missen");
        for(Reiziger reiziger : rdao.findAll()){
            System.out.println(reiziger);
        }



        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }
    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        List<Adres> initialList = adao.findAll();
        for(Adres adres : initialList ){
            System.out.println(adres);
        }
        System.out.println("before create" + initialList.size());
Adres adres = new Adres(6, "1234ab", "12", "testSTraat", "testPlaats", 6);
        adao.delete(adres);

        adao.save(adres);
        System.out.println("Before delete, after create " + adao.findAll().size() );

        System.out.println("find by reiziger 6 returns, before update: " + adao.findByReiziger(new Reiziger(6, "S", "", "Boers", java.sql.Date.valueOf("1981-03-14"))));
        adao.update(new Adres(6, "123", "12ed", "testSTraated", "testPlaatsED", 6));
        System.out.println("After update:"+ adao.findByReiziger(new Reiziger(6, "S", "", "Boers", java.sql.Date.valueOf("1981-03-14"))));
        adao.delete(adres);
        System.out.println("after delete" + adao.findAll().size());


    }
}
