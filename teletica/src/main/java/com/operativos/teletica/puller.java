package com.operativos.teletica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Hashtable;

import com.operativos.teletica.sqliteConnect.connectSqlite;

//esta clase recibe el texto que se obtine mediante web scrapping y extrae las palabras clave que se asemejan a las de la DB
public class puller{

    //todas las keyWords que aparescan dentro del texto que sean las de la DB retornelas para llamar al metodo en el consumer
    public Dictionary<String, Integer> similarKeys = new Hashtable<String, Integer>();
    //["Events" : 10, "Health" : 5, "Politics" : 9]

    public static int nroVecesR(String frase,String palabra){ //devulve la repeticion de una sola palabra 
        int pos = 0;
        pos = frase.indexOf(palabra);
        
        if(pos>=0){
            return 1 + nroVecesR(frase.substring(pos+palabra.length()+1),palabra);
        }
        else
            return 0;
    }

    public static int puller_methodEvents(String newsText, Connection c) {
        int totalCount = 0;
        Statement stmt = null;

        try {

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Events;" );
            
            while ( rs.next() ) {
                String  word = rs.getString("Word");
                totalCount += nroVecesR(newsText, word);
                
            }
            rs.close();
            stmt.close();

            return totalCount;
            
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("Error del puller_methodEvents: " + e);
            return totalCount = 0;
        }
    }

    public static int puller_methodHealth(String newsText, Connection c) {
        int totalCount = 0;
        Statement stmt = null;
        
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Health;" );
            
            while ( rs.next() ) {
                //int id = rs.getInt("ID");
                String  word = rs.getString("Word");

                totalCount += nroVecesR(newsText, word);
                
            }
            rs.close();
            stmt.close();

            return totalCount;
            
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("Error del puller_methodHealth: " + e);
            return totalCount = 0;
        }
    }

    public static int puller_methodPolitics(String newsText, Connection c) {
        int totalCount = 0;
        Statement stmt = null;
        
        try {

            c = connectSqlite.connect();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Politics;" );
            
            while ( rs.next() ) {
                //int id = rs.getInt("ID");
                String  word = rs.getString("Word");

                totalCount += nroVecesR(newsText, word);
                
            }
            rs.close();
            stmt.close();

            return totalCount;
            
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("Error del puller_methodPolitics: " + e);
            return totalCount = 0;
        }
    }
}
