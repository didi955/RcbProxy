package fr.rushcubeland.rcbproxy.bungee.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public class MySQL {

    public static void update(Connection connection, String qry) throws SQLException {
        PreparedStatement s = connection.prepareStatement(qry);
        s.executeUpdate();
        s.close();
        connection.close();
    }

    public static Object query(Connection connection, String qry, Function<ResultSet, Object> consumer) throws SQLException {

        PreparedStatement s = connection.prepareStatement(qry);
        ResultSet rs = s.executeQuery();
        s.close();
        connection.close();
        return consumer.apply(rs);

    }

    public static void query(Connection connection, String qry, Consumer<ResultSet> consumer) throws SQLException {
        PreparedStatement s = connection.prepareStatement(qry);
        ResultSet rs = s.executeQuery();
        consumer.accept(rs);
        s.close();
        connection.close();
    }

    public static void createTables() {

        try {
            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Proxyaccounts (" +
                    "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "uuid VARCHAR(255), " +
                    "grade VARCHAR(255), " +
                    "grade_end BIGINT)");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Proxyrank_permissions (" +
                    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "grade VARCHAR(255), " +
                    "permission VARCHAR(255))");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Proxyplayer_permissions (" +
                    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "uuid VARCHAR(255), " +
                    "permission VARCHAR(255))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
