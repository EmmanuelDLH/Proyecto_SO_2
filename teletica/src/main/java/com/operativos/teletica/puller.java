package com.operativos.teletica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.operativos.teletica.sqliteConnect.connectSqlite;

//esta clase recibe el texto que se obtine mediante web scrapping y extrae las palabras clave que se asemejan a las de la DB
public class puller{

    public static int nroVecesR(String frase,String palabra){
        int pos = 0;
        pos = frase.indexOf(palabra);
        
        if(pos>=0){
            return 1 + nroVecesR(frase.substring(pos+palabra.length()+1),palabra);
        }
        else
            return 0;
    }

    public static ArrayList<String> puller_method(String newsText) {

        //todas las keyWords que aparescan dentro del texto que sean las de la DB retornelas para llamar al metodo en el consumer
        ArrayList<String> similarKeys = new ArrayList<String>();

        Connection c = null;
        Statement stmt = null;
        
        try {

            c = connectSqlite.connect();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Events;" );
            
            while ( rs.next() ) {
                new Thread("" + rs.getFetchSize()){
                    public void run(){
                        int id = rs.getInt("ID");
                        String  word = rs.getString("Word");
                        //System.out.println("Thread: " + getName() + " running");

                        

                        //newsText = sTexto.substring(sTexto.indexOf(sTextoBuscado)+sTextoBuscado.length(),sTexto.length());
                    }
                  }.start();
                
                /*System.out.println( "ID = " + id );
                System.out.println( "WORD = " + word );*/


            }
            rs.close();
            stmt.close();
            c.close();
            
        } catch (Exception e) {
            //TODO: handle exception
        }
        

        return similarKeys;
    }
}
