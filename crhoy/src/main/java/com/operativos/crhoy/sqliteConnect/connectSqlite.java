package com.operativos.teletica.sqliteConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectSqlite {
    
    public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:keyWords.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");

            return conn;
            
        } catch (SQLException e) {

            System.out.println(e.getMessage());
            
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
