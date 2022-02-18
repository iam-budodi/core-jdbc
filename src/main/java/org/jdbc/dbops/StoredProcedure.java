package org.jdbc.dbops;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class StoredProcedure {
    public void spListProductsBy(String productLine) throws Exception {

        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");

            CallableStatement callableStatement = connection.prepareCall("{call listProductsFor(?)}");
            ){
            callableStatement.setString(1, productLine);
            boolean success = callableStatement.execute();
            if (success) {
                try(ResultSet resultSet = callableStatement.getResultSet()){
                    while (resultSet.next()) {
                        String name = resultSet.getString("productName");
                        System.out.println(name);
                    }
                }
            }
        }
    }


    public String spUpdateEmail(int employeeNumber, String newEmail) throws Exception {

        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");

            CallableStatement callableStatement = connection.prepareCall("{call updateEmail(?,?)}");
            ){
            callableStatement.setInt(1, employeeNumber);
            callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
            callableStatement.setString(2, newEmail);
            callableStatement.execute();

            String oldEmail = callableStatement.getString(2);
            return oldEmail;

        }
    }
}
