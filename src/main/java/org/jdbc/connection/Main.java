package org.jdbc.connection;

import org.jdbc.util.ExceptionHandler;

public class Main {
    public static void main(String[] args) {
        try {
            ConnectComponent connect = new ConnectComponent();
            if (connect.driverConnection()) System.out.println("Connected Successfully!!!");
            else System.out.println("Failed to Connect!!!");
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }
    }
}
