package de.salzheld.login.helper;

import com.mysql.jdbc.Connection;
import com.sun.corba.se.impl.util.Version;
import de.salzheld.login.model.Student;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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