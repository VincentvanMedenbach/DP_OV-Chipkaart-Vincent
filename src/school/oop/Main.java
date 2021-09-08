package school.oop;

import school.oop.impl.ReizigerDAO;
import school.oop.impl.ReizigerDAOPsql;
import school.oop.model.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, ParseException {
        Connection conn = Database.connect();
        ReizigerDAOPsql reizigerDao = new ReizigerDAOPsql(conn);
        System.out.println("Alle reizigers:");
        testReizigerDAO(reizigerDao);

        for(Reiziger reiziger : reizigerDao.findAll()){
            System.out.println(reiziger);
        }

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
        System.out.println("test zoeken met id:" + rdao.findById(sietske.getId()));
        System.out.println( java.sql.Date.valueOf(gbdatum));
        System.out.println("test zoeken met geboortedatum:" + rdao.findByGbdatum(gbdatum));
        rdao.delete(sietske);
        System.out.println( "Dit zou nu 1 moeten missen");
        for(Reiziger reiziger : rdao.findAll()){
            System.out.println(reiziger);
        }



        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }
}
