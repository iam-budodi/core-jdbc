package org.jdbc.dbops;

import java.sql.*;

public class SqlDmlComponent {
    public void updateOrder(int orderNumber, String productCode, int newQuantity) throws Exception {
        String query = "UPDATE orderdetails " +
                "SET quantityOrdered = ? " +
                "WHERE orderNumber = ? " +
                "AND productCode = ?";
        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");

            PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setInt(2, orderNumber);
            preparedStatement.setString(3, productCode);
            preparedStatement.executeUpdate();
        }

    }

    public int replaceSalesManager(String managerBeingReplaced, String replacementManager) throws Exception {
        String query = "UPDATE employees " +
                "SET reportsTo = ? " +
                "WHERE reportsTo = ?";
        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");

            PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, replacementManager);
            preparedStatement.setString(2, managerBeingReplaced);
            return preparedStatement.executeUpdate();
        }
    }

    public int addEmployee(String lastName, String firstName,
                           String extension, String email,
                           String officeCode,
                           String jobTitle) throws Exception {

        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");

            PreparedStatement preparedStatement =
                    connection.prepareStatement(
                            "INSERT INTO employees "
                                    + "(lastName, firstName, extension, email, officeCode, jobTitle) "
                                    + "VALUES (?, ?, ?, ?, ?, ?)",

                            Statement.RETURN_GENERATED_KEYS);
            ){
            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, extension);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, officeCode);
            preparedStatement.setString(6, jobTitle);

            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys();){
                int autoGenKey = 0;
                if(resultSet.next()) {
                    autoGenKey = resultSet.getInt(1);
                }

                return autoGenKey;
            }}
    }

    public boolean deleteEmployee(String employeeNumber) throws Exception {
        try(Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");

            PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM employees WHERE employeeNumber = ?");
            ){
            preparedStatement.setString(1, employeeNumber);
            int count = preparedStatement.executeUpdate();
            return count > 0 ? true : false;

        }


    }
}
