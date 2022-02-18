package org.jdbc.connectionpool;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class OrdersComponent {
    public void tryConnection() throws Exception {
        try(Connection con = ConnectionPool.getConnection();) {
            PreparedStatement preparedStatement = con.prepareStatement("select * from orderdetails");
            preparedStatement.execute();
            String msg = Thread.currentThread().getName() + " --> " + this.getConnectionId(con);
            System.out.println(msg);
        }
    }

    public String getConnectionId(Connection con) {
        String conId = con.toString();
        int lastPos = conId.length() - 2;
        conId = conId.substring(lastPos);
        return conId;
    }

    /**
     * Manual Transaction Commit
     */
    public int createOrder(int customerNumber,LineItem lineItem) throws  Exception {
        try(Connection connection =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels?"
                            + "user=root&password=pluralsight&serverTimezone=UTC");

            PreparedStatement orderStmnt =
                    connection.prepareStatement(
                            "INSERT INTO orders " +
                                    "(orderDate, requiredDate, status, customerNumber ) " +
                                    "VALUES (now(),now(),'In Process',?)",

                            Statement.RETURN_GENERATED_KEYS );){

            connection.setAutoCommit(false);
            orderStmnt.setInt(1,customerNumber);
            orderStmnt.executeUpdate();

            try (ResultSet result = orderStmnt.getGeneratedKeys(); ){
                if(!result.next()){
                    connection.rollback();
                    return 0;
                }

                int orderNumber = result.getInt(1);
                String sqlStr =
                        "INSERT INTO orderdetails "
                                + "(orderNumber, productCode, quantityOrdered, "
                                +  "priceEach, orderLineNumber) "
                                + "VALUES (?,?,?,?,?)";

                try( PreparedStatement detailsPS =
                             connection.prepareStatement(sqlStr);){

                    detailsPS.setInt(1,orderNumber);
                    detailsPS.setString(2,lineItem.productCode);
                    detailsPS.setInt(3,lineItem.quantityOrdered);
                    detailsPS.setDouble(4,lineItem.priceEach);
                    detailsPS.setDouble(5,1);

                    int count = detailsPS.executeUpdate();

                    if(count == 1){
                        connection.commit();
                        return orderNumber;
                    }else{
                        connection.rollback();
                        return 0;
                    }
                }
            }catch(Exception e) {
                connection.rollback();
                throw e;
            }
        }
    }

    /**
     * Automatic Transaction Commit
     */
    public int createNewOrder(int customerNumber,LineItem lineItem) throws  Exception {
        try(Connection connection =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels?"
                            + "user=root&password=pluralsight&serverTimezone=UTC");

            PreparedStatement orderStmnt =
                    connection.prepareStatement(
                            "INSERT INTO orders " +
                                    "(orderDate, requiredDate, status, customerNumber ) " +
                                    "VALUES (now(),now(),'In Process',?)",

                            Statement.RETURN_GENERATED_KEYS );){

            orderStmnt.setInt(1,customerNumber);
            orderStmnt.executeUpdate();
            try ( ResultSet result = orderStmnt.getGeneratedKeys(); ){
                if(!result.next()){ return 0;}
                int orderNumber = result.getInt(1);
                String sqlStr =
                        "INSERT INTO orderdetails "
                                + "(orderNumber, productCode, quantityOrdered, "
                                +  "priceEach, orderLineNumber) "
                                + "VALUES (?,?,?,?,?)";

                try( PreparedStatement detailsPS = connection.prepareStatement(sqlStr);){
                    detailsPS.setInt(1,orderNumber);
                    detailsPS.setString(2,lineItem.productCode);
                    detailsPS.setInt(3,lineItem.quantityOrdered);
                    detailsPS.setDouble(4,lineItem.priceEach);
                    detailsPS.setDouble(5,1);

                    int count = detailsPS.executeUpdate();
                    if(count == 1){
                        return orderNumber;
                    }else{
                        return 0;
                    }
                }
            }
        }
    }

    public CachedRowSet ordersByStatus(String status) throws Exception {
        String queryString = "SELECT * FROM orders WHERE status = ?";

        RowSetFactory rowSetProvider = RowSetProvider.newFactory();
        CachedRowSet rowSet = rowSetProvider.createCachedRowSet();
        rowSet.setUrl("jdbc:mysql://localhost:3306/classicmodels?user=root&password=pluralsight&serverTimezone=UTC");
        rowSet.setCommand(queryString);
        rowSet.setString(1, status);
        rowSet.execute();

        return rowSet;
    }
}
