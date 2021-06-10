package com.operativos.diarioextra;

import java.util.ArrayList;

import com.operativos.diarioextra.getconsume.GetRequest;

//esta clase recibe el texto que se obtine mediante web scrapping y extrae las palabras clave que se asemejan a las de la clase DictionaryDB
public class puller2{

    public static int nroVecesR(String frase,String palabra){ //devulve la repeticion de una sola palabra 
        int pos = 0;
        pos = frase.indexOf(palabra);
        
        if(pos>=0){
            return 1 + nroVecesR(frase.substring(pos+palabra.length()+1),palabra);
        }
        else
            return 0;
    }

    public static int puller_methodEvents(String newsText) {
        int totalCount = 0;
        ArrayList<String> eventos = GetRequest.db.getevents();

        for (int i = 0; i < eventos.size(); i++) {
            totalCount += nroVecesR(newsText, eventos.get(i));
        }
        
        return totalCount;
    }

    public static int puller_methodHealth(String newsText) {
        int totalCount = 0;
        ArrayList<String> salud = GetRequest.db.gethealth();

        for (int i = 0; i < salud.size(); i++) {
            totalCount += nroVecesR(newsText, salud.get(i));
        }
        
        return totalCount;
    }

    public static int puller_methodPolitics(String newsText) {
        int totalCount = 0;
        ArrayList<String> politica = GetRequest.db.getpolitics();

        for (int i = 0; i < politica.size(); i++) {
            totalCount += nroVecesR(newsText, politica.get(i));
        }
        
        return totalCount;
    }

    public static int puller_methodSports(String newsText) {
        int totalCount = 0;
        ArrayList<String> deportes = GetRequest.db.getsports();

        for (int i = 0; i < deportes.size(); i++) {
            totalCount += nroVecesR(newsText, deportes.get(i));
        }
        
        return totalCount;
    }

    public static int puller_methodEconomy(String newsText) {
        int totalCount = 0;
        ArrayList<String> economia = GetRequest.db.geteconomy();

        for (int i = 0; i < economia.size(); i++) {
            totalCount += nroVecesR(newsText, economia.get(i));
        }
        
        return totalCount;
    }

    public static int puller_methodEntertainment(String newsText) {
        int totalCount = 0;
        ArrayList<String> entretenimiento = GetRequest.db.getentertainment();

        for (int i = 0; i < entretenimiento.size(); i++) {
            totalCount += nroVecesR(newsText, entretenimiento.get(i));
        }
        
        return totalCount;
    }
}
