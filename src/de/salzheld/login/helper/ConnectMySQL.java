package de.salzheld.login.helper;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;

/**
 *
 * @author Joern
 */
public final class ConnectMySQL {

    public static Connection ConnectDatabase(String dbHost, String dbPort, String dbName, String dbUser, String dbPassword) {
        String dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        try {
            Connection con = ( Connection ) DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            return con;
        }
        catch (Exception e) {
            return null;
        }
    }
}