package school.oop;

import com.sun.source.doctree.SystemPropertyTree;
import school.oop.impl.*;
import school.oop.model.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, ParseException {
        Connection conn = Database.connect();
        OVChipkaartDAO ovChipkaartDao = new OVChipkaartDAOPsql(conn);
        ReizigerDAO reizigerDao = new ReizigerDAOPsql(conn, ovChipkaartDao);
        AdresDAO adresDao = new AdresDAOPsql(conn, reizigerDao);
        ProductDao productDao = new ProductDaoPsql(conn);

//        System.out.println("Alle reizigers:");
//        for(Reiziger reiziger : reizigerDao.findAll()){
//            System.out.println(reiziger);
//            System.out.println(reiziger.getOvchipkaarten());
//        }
//        testReizigerDAO(reizigerDao);
//        testAdresDAO(adresDao, reizigerDao);
        testOvChip(ovChipkaartDao, new Reiziger(2, "S", "", "Boers", new Date(1 - 8 - 2000)));
//        testProduct(productDao, new Product(10, "testNaam", "testBeschrijving", 8));

    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     * <p>
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
        System.out.println(java.sql.Date.valueOf(gbdatum));
        System.out.println("test zoeken met geboortedatum:" + rdao.findByGbdatum(gbdatum));
        rdao.delete(sietske);
        System.out.println("Dit zou nu 1 moeten missen");
        for (Reiziger reiziger : rdao.findAll()) {
            System.out.println(reiziger);
        }


        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        List<Adres> initialList = adao.findAll();
        for (Adres adres : initialList) {
            System.out.println(adres);
        }
        System.out.println("before create" + initialList.size());
        Adres adres = new Adres(6, "1234ab", "12", "testSTraat", "testPlaats", 6);
        adao.delete(adres);

        adao.save(adres);
        System.out.println("Before delete, after create " + adao.findAll().size());

        System.out.println("find by reiziger 6 returns, before update: " + adao.findByReiziger(new Reiziger(6, "S", "", "Boers", java.sql.Date.valueOf("1981-03-14"))));
        adao.update(new Adres(6, "123", "12ed", "testSTraated", "testPlaatsED", 6));
        System.out.println("After update:" + adao.findByReiziger(new Reiziger(6, "S", "", "Boers", java.sql.Date.valueOf("1981-03-14"))));
        adao.delete(adres);
        System.out.println("after delete" + adao.findAll().size());
    }

    private static void testOvChip(OVChipkaartDAO ovChipkaartDAO, Reiziger reiziger) throws SQLException {
        Product product1 = new Product(6, "Studentenreisproduct", "beschrijving", 50);
        Product product2 = new Product(7, "Studentenreisproduct", "beschrijving", 50);

        try {
            List<OVChipkaart> ovChipkaarten = ovChipkaartDAO.findByReiziger(reiziger);
            System.out.println(ovChipkaarten);
        } catch (Exception e) {
            System.out.println("Error finding by Reiziger");
        }
        OVChipkaart ovChipkaart = new OVChipkaart(1231, new java.util.Date(), 2, 4, reiziger);
        ovChipkaart.addProducten(product1.getProduct_nummer(), "test", new Date(System.currentTimeMillis()));
        try {
            ovChipkaartDAO.save(ovChipkaart);
        } catch (Exception e) {
            System.out.println("Saving failed");
        }
        System.out.println("voor update na save: \n" + ovChipkaartDAO.findById(ovChipkaart.getKaart_nummer()));
        System.out.println("find by products: \n" + ovChipkaartDAO.findByProduct(product1.getProduct_nummer()));
        try {
            ovChipkaart.addProducten(product2.getProduct_nummer(), "test2", new Date(System.currentTimeMillis()));
            ovChipkaartDAO.update(ovChipkaart);
            System.out.println("na save: \n" + ovChipkaartDAO.findById(ovChipkaart.getKaart_nummer()));

        } catch (Exception e) {
            System.out.println("update failed!");
        }

        try {
            ovChipkaartDAO.delete(ovChipkaart);
        } catch (Exception e) {
            System.out.println("Delete failed");
        }
        try {
            System.out.println("Deleted?: ");

            ovChipkaartDAO.findById(ovChipkaart.getKaart_nummer());
            System.out.println("Nee");

        }catch(Exception e){
            System.out.println("Ja");

        }
    }

    // Ids weghalen, eigenaar hoort voor relatie te zorgen
    private static void testProduct(ProductDao productDao, Product product) throws SQLException {
        product.addOvChipkaarten(35283, "actief", new Date(System.currentTimeMillis()));
        product.addOvChipkaarten(46392, "actief", new Date(System.currentTimeMillis()));

        System.out.println("Origineel object: \n" + product);
        try {
            productDao.save(product);
            System.out.println("Opgeslagen object: \n" + productDao.findById(product.getProduct_nummer()));
        } catch (Exception e) {
            System.out.println("saven mislukt met error:");
            System.out.println(e);
        }
        try {
            product.setBeschrijving("geupdate beschrijving");
            product.setPrijs(2);
            product.addOvChipkaarten(46392, "No", new Date(System.currentTimeMillis()));
            product.addOvChipkaarten(90537, "actief", new Date(System.currentTimeMillis()));
            productDao.update(product);
        } catch (Exception e) {
            System.out.println("updaten mislukt met error:");

            System.out.println(e);
        }
        try {
            System.out.println("Gevonden producten bij ov: \n" + productDao.findByOvChipkaart(new OVChipkaart(46392)));
        } catch (Exception e) {
            System.out.println("vinden bij ovchipkaart mislukt met error:");
            System.out.println(e);

        }
        try {
            System.out.println("Alle producten: \n" + productDao.findAll());
        } catch (Exception e) {
            System.out.println("vinden van alle ovchipkaarten mislukt met error:");
            System.out.println(e);
        }
        try {
            System.out.println("Geupdate object: \n" + productDao.findById(product.getProduct_nummer()));
            productDao.delete(product);

        } catch (Exception e) {
            System.out.println("deleten mislukt met error:");

            System.out.println(e);
        }
        try {
            System.out.println("leeg object: \n" + productDao.findById(product.getProduct_nummer()));
        } catch (Exception e) {
            System.out.println("niks gevonden...");
        }


    }
}
