package school.oop.impl;

import school.oop.model.OVChipkaart;
import school.oop.model.Product;
import school.oop.model.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    Connection conn;
    AdresDAO adao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        ArrayList<OVChipkaart> returnList = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id= ?");
        stmt.setInt(1, reiziger.getId());

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart(rs.getInt("kaart_nummer"), rs.getDate("geldig_tot"), rs.getInt("klasse"), rs.getInt("saldo"), rs.getInt("reiziger_id"));
            PreparedStatement relationStmt = conn.prepareStatement("SELECT * FROM ov_chipkaart_product where kaart_nummer = ?");
            relationStmt.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet ovChipProducten = relationStmt.executeQuery();
            while (ovChipProducten.next()) {
                ovChipkaart.addProducten(
                        ovChipProducten.getInt("kaart_nummer"),
                        ovChipProducten.getString("status"),
                        ovChipProducten.getDate("last_update")
                );
            }
            returnList.add(ovChipkaart);

        }
        rs.close();
        return returnList;
    }

}
