package com.operativos.repretel.getconsume;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetRequest {

    public static DictionaryDB db;

    public static void request() {

        // Create a neat value object to hold the URL
        URL url;

        try {
            url = new URL("https://9daf7b8ebe33.ngrok.io/getDictionaryTokens");

            // Open a connection(?) on the URL(?) and cast the response(??)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // This line makes the request
            InputStream responseStream = connection.getInputStream();

            // Manually converting the response body InputStream to DictionaryDB using Jackson
            ObjectMapper mapper = new ObjectMapper();
            db = mapper.readValue(responseStream, DictionaryDB.class);

            // Finally we have the response
            //System.out.println(responseStream);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*public static void main(String[] args){
        request();
        System.out.println(db.toString());
    }*/

}