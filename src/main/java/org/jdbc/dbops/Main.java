package org.jdbc.dbops;

import org.jdbc.util.ExceptionHandler;

import java.io.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
//        try {
//            ProductComponent product = new ProductComponent();
////            product.productListUsingJDBCStatement();
////            product.productListUsingJDBCStatementWithTryCatchFinallyBlock();
////            product.productListUsingJDBCStatementWithTryWithResource();
////            product.productListUsingJDBCPreparedStatementWithTryWithResource();
////            product.productListUsingJDBCPreparedStatementWithTryWithResource(90.0, 100.0);
//            product.productListUsingJDBCPreparedStatement();
//        }  catch (Exception exception) {
//            ExceptionHandler.handleException(exception);
//        }

//        productNavigation();
//        updateOrderDetailsTable(); 
//        reassignment();
//        newEmployee();
//        deleteEmployee();
//        storeBlob();
//        readBlob();
//        storeClob();
//        readClob();
//        simpleStoredProcedure();
        inOutStoredProcedure();
    }

    private static void inOutStoredProcedure() {
        try {
            int empNum = 1002;
            String newEmail = "diane@classicmodelcars.com";
            StoredProcedure sp = new StoredProcedure();


            String oldEmail = sp.spUpdateEmail(empNum, newEmail);

            if(oldEmail != null) {
                System.out.println("email changed from "+oldEmail+" to "+newEmail);
            }

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void simpleStoredProcedure() {
        try {
            StoredProcedure sp = new StoredProcedure();
            sp.spListProductsBy("Motorcycles");
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void storeClob() {
        try {
            BlobsAndClobs clob = new BlobsAndClobs();

            String prodLine = "Planes";
            String fileName = "src/JanesAllWorldAircraft1913_704482.txt";

            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);

            boolean success = clob.storeCLOB(prodLine, fileReader);
            fileReader.close();

            if(success) {
                System.out.println("Success: The text contents of " + fileName + " has been stored");
            }else {
                System.out.println("Fail: The text contents of " + fileName + " has NOT been stored");
            }

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void readClob() {
        try {
            BlobsAndClobs clob = new BlobsAndClobs();
            String prodLine = "Planes";

            Reader reader = clob.readCLOB(prodLine);

            int chr = 0;
            while ((chr = reader.read()) > 0) {
                System.out.write(chr);
            }

            reader.close();

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void storeBlob() {
        try {
            BlobsAndClobs blob = new BlobsAndClobs();

            String prodLine = "Planes";
            String fileName = "src/bi-plane.png";

            File file = new File(fileName);
            FileInputStream fileInputStream = new FileInputStream(file);

            boolean success = blob.storeBLOB(prodLine, fileInputStream);
            fileInputStream.close();

            if(success) {
                System.out.println("Success: The image " + fileName + " has been stored");
            }else {
                System.out.println("Fail: The image " + fileName + " has NOT been stored");
            }

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void readBlob() {
        try {
            BlobsAndClobs blob = new BlobsAndClobs();

            String fileName = "Planes_Image.png";
            String prodLine = "Planes";

            InputStream inStream = blob.readBLOB(prodLine);

            if(inStream == null) {
                System.out.println("Could not read image data from database");
                return;
            }
            File file = new File(fileName);
            FileOutputStream output = new FileOutputStream(file);

            System.out.println("Writing image to file " + fileName);

            byte[] buffer = new byte[1024];
            while (inStream.read(buffer) > 0) {
                output.write(buffer);
            }

            inStream.close();
            output.close();

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void deleteEmployee() {
        try {
//            String employeeNumber = "1703"; // do not exist
            String employeeNumber = "1";

            SqlDmlComponent dml = new SqlDmlComponent();

            boolean success = dml.deleteEmployee(employeeNumber);
            System.out.println(	" Employee "
                    + employeeNumber
                    + " has "
                    + (success ? "been deleted" : "not been deleted"));

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void newEmployee() {
        try {
            SqlDmlComponent dml = new SqlDmlComponent();

            int key = dml.addEmployee(
                    "Williams", "Roger", "x104",
                    "rwilliams@classicmodelcars.com", "3",
                    "Sales Manager (NA)");

            System.out.println("The auto-generated primary key = " + key);
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void reassignment() {
        try {
            SqlDmlComponent dml = new SqlDmlComponent();
            System.out.println(dml.replaceSalesManager(
                    "1143", "1621") + " employees has been reassigned!");
        }  catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void updateOrderDetailsTable() {
        try {
            int orderNumber = 10138;
            String productCode = "S24_2022";
            int newQuantity = 100;

            SqlDmlComponent order = new SqlDmlComponent();
            order.updateOrder(orderNumber, productCode, newQuantity);
            System.out.println("UPDATED ....");
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }

    }

    private static void productNavigation() {
        int rowId = 2;
        int relativePos = 3;

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement
//                     .executeQuery("SELECT * FROM products " + "where productLine = 'Motorcycles'")

             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products " +
                     "WHERE productLine = 'Motorcycles'");
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {

            ProductNavigationUtil comp = new ProductNavigationUtil(resultSet);

            System.out.println("All Products");
            comp.printForward();

            System.out.println("First row");
            comp.printFirst();

            System.out.println("Last row");
            comp.printLast();

            System.out.println("Row at position " + rowId);
            comp.printAt(rowId);

            System.out.println("Row " + (relativePos < 0 ? "Before" : "After") + " row " + relativePos);
            comp.printRelative(relativePos);

            System.out.println("All products in reverse order");
            comp.printReverse();

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }
}
