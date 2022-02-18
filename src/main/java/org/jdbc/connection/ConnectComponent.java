package org.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectComponent {
    public boolean driverConnection() throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC")) {

            return conn.isValid(2);
        }
    }

    public Connection dbConnection() throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC")) {
//            conn.isValid(2);
            return conn;
        }
    }
}
