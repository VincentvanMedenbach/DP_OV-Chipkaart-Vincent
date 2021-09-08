package school.oop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String user = "postgres";
    private static final String password = "root";
    private static Connection database;
    private static final String url = "jdbc:postgresql://127.0.0.1:5432/ovchip";

    public static Connection connect() throws SQLException {
        database = DriverManager.getConnection(url, user, password);
        return database;
    }
    public static void close() throws SQLException {
        database.close();
    }

}
