package de.salzheld.login.model;

import com.mysql.jdbc.Connection;
import de.salzheld.login.helper.ConnectMySQL;

import java.sql.SQLException;

/**
 * Created by Joern on 24.01.2016.
 */
public class LoginModel {
    Connection connection;

    public LoginModel() {
        connection = ConnectMySQL.ConnectDatabase(
                "localhost",
                "3306",
                "Danis61128",
                "root",
                "tischTuch"
        );
        if(connection == null) {
            System.exit(1);
        }
    }

    public boolean isDbConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
