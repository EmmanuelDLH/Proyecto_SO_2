package com.operativos.teletica.sqliteConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectSqlite {
    
    public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:../Proyecto_SO_2/keyWords.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");

            return conn;
            
        } catch (SQLException e) {

            System.out.println(e.getMessage());
            
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return conn;
    }
    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
        connect();
    }*/
}
