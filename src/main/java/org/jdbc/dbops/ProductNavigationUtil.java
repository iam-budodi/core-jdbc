package org.jdbc.dbops;

import java.sql.ResultSet;

public class ProductNavigationUtil {
    private ResultSet resultSet = null;

    public ProductNavigationUtil(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public void printForward() throws Exception {
        while(resultSet.next()) {
            String name = resultSet.getString("productName");
            System.out.println("   " + name);
        }
    }

    public void printFirst() throws Exception{
        if(resultSet.first()) {
            String name = resultSet.getString("productName");
            System.out.println("   " + name);
        }
    }

    public void printLast() throws Exception{
        if(resultSet.last()) {
            String name = resultSet.getString("productName");
            System.out.println("   " + name);
        }
    }

    public void printAt(int position) throws Exception{
        if(resultSet.absolute(position)) {
            String name = resultSet.getString("productName");
            System.out.println("   " + name);
        }
    }

    public void printRelative(int position) throws Exception{
        if(resultSet.relative(position)) {
            String name = resultSet.getString("productName");
            System.out.println("   " + name);
        }
    }

    public void printReverse() throws Exception {
        resultSet.afterLast();
        while (resultSet.previous()) {
            String name = resultSet.getString("productName");
            System.out.println("   " + name);
        }
    }
}
