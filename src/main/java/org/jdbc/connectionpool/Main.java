package org.jdbc.connectionpool;

import org.jdbc.util.ExceptionHandler;

import javax.sql.rowset.CachedRowSet;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Commented are connection pool code
 */
//public class Main implements Runnable {
//    public void run() {
//        for(int i = 0; i < 3; i++) {
//            try {
//                OrdersComponent comp = new OrdersComponent();
//                comp.tryConnection();
//            }catch (Exception exception) {
//                ;
//            }
//        }
//    }
//
//    public static void main(String [] args) {
//        new ConnectionPool();
//
//        System.out.println("Thread   Conn");
//        System.out.println("------   ----");
//
//        for(int i = 0; i < 5; i++) {
//            Thread t = new Thread(new Main(), "Main_" + i);
//            t.start();
//        }
//    }
//
//    public static long sleepInterval() {
//        Double randomDouble = Main.randomize(0,500);
//        return Math.round(randomDouble);
//
//    }
//    public static double randomize(double min, double max){
//        double x = (int)(Math.random()*((max-min)+1))+min;
//        return x;
//    }

public class Main {
    public static void main(String [] args) {
//        autoCommit();
//        manualCommit();
        jdbcRawSet();
    }

    private static void jdbcRawSet() {
        String status = "In Process";
        OrdersComponent order = new OrdersComponent();
        try(CachedRowSet rowSet1 = order.ordersByStatus(status);
            FileOutputStream fOut = new FileOutputStream("row_set_serialized.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fOut);){

            oos.writeObject(rowSet1);
            fOut.close();
            oos.close();

            // Read CachedRowSet from file
            try(FileInputStream fin = new FileInputStream("row_set_serialized.ser");
                ObjectInputStream ois = new ObjectInputStream(fin);
                CachedRowSet rowSet2 = (CachedRowSet)ois.readObject();
                ){

                // Print out CachedRowSet
                while (rowSet2.next()) {
                    int customerNumber = rowSet2.getInt("customerNumber");
                    int orderNumber = rowSet2.getInt("orderNumber");
                    System.out.println(customerNumber + " " + orderNumber + " " + status);
                }

            }
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void manualCommit() {
        int customerNumber = 112;
        LineItem lineItem = new LineItem("S10_1949", 10, 100.00);

        try {
            OrdersComponent order = new OrdersComponent();
            int orderNumber = order.createOrder(customerNumber, lineItem);
            System.out.println("New Order Number = "+ orderNumber);

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }

    private static void autoCommit() {
        int customerNumber = 103;
        LineItem lineItem = new LineItem("S10_1678", 20, 50.00);

        try {
            OrdersComponent order = new OrdersComponent();
            int orderNumber = order.createNewOrder(customerNumber, lineItem);
            System.out.println("New Order Number = "+ orderNumber);

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }
}
