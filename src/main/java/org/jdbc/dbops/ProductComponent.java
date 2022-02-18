package org.jdbc.dbops;

import org.jdbc.connection.ConnectComponent;
import org.jdbc.util.ExceptionHandler;

import java.sql.*;

public class ProductComponent {
    public void productListUsingJDBCStatement() throws Exception {
        /**
         * Without try-catch block
         */
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM products");
        while (resultSet.next()) {
            String name = resultSet.getString("productName");
            System.out.println(name);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }

    /**
     * With try-catch-finally block
     */
    public void productListUsingJDBCStatementWithTryCatchFinallyBlock() throws Exception{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM products;");
            while (resultSet.next()) {
                String name = resultSet.getString("productName");
                int quantity = resultSet.getInt("quantityInStock");
                double price = resultSet.getDouble("buyPrice");

                System.out.format("%-45s %5d %10.2f%n", name, quantity, price);
            }


        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        } finally {
            try {
                resultSet.close();
            } catch(Exception ex) {
                System.out.println("ResultSet.close(): " + ex.getMessage());
            }

            try {
                statement.close();
            } catch(Exception ex) {
                System.out.println("Statement.close(): " + ex.getMessage());
            }

            try {
                connection.close();
            } catch(Exception ex) {
                System.out.println("Connection.close(): " + ex.getMessage());
            }
        }
    }

    /**
     * Using try-with-resource
     */
    public void productListUsingJDBCStatementWithTryWithResource() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM products;");
             ) {
//            statement.executeQuery("SELECT * FROM products");
            while (resultSet.next()) {
                String name = resultSet.getString("productName");
                System.out.println(name);
            }

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    /**
     * Using try-with-resource with PreparedStatement
     */
    public void productListUsingJDBCPreparedStatementWithTryWithResource() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products");
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                String name = resultSet.getString("productName");
                System.out.println(name);
            }

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    public void productListUsingJDBCPreparedStatementWithTryWithResource(double lowPrice, double highPrice) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products " +
                     "WHERE buyPrice BETWEEN ?1 AND ?2");
        ) {

            preparedStatement.setDouble(1, lowPrice);
            preparedStatement.setDouble(2, highPrice);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("productName");
                    System.out.println(name);
                }
            }
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    public void productListUsingJDBCPreparedStatement() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT productName, quantityInStock, buyPrice " +
                             "FROM products");
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                String name = resultSet.getString("productName");
                int quantity = resultSet.getInt("quantityInStock");
                double price = resultSet.getDouble("buyPrice");

                System.out.format("%-45s %5d %10.2f%n", name, quantity, price);
            }
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }
}
