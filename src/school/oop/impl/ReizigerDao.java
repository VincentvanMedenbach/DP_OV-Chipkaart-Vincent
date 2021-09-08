package school.oop.impl;

import school.oop.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReizigerDao {
    public ArrayList<String> getReizigers() throws SQLException {
        ArrayList<String> returnList = new ArrayList<>();
        Connection connection = Database.connect();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM reiziger");
        while (rs.next()) {
            returnList.add("    #" + rs.getInt("reiziger_id") + ": " + rs.getString("voorletters") + " " + rs.getString("tussenvoegsel") + " " + rs.getString("achternaam") + " " + rs.getString("geboortedatum")); // A. Donk (1968-07-19)
        }
        rs.close();
        return returnList;
    }
}
