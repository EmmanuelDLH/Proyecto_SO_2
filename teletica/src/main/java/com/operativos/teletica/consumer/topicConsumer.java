package com.operativos.teletica.consumer;

import org.jsoup.select.Elements;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.operativos.teletica.News;
import com.operativos.teletica.NewsUtil;
import com.operativos.teletica.puller;
import com.operativos.teletica.sqliteConnect.connectSqlite;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Hashtable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class topicConsumer {

    public int eventsCountFinal = 0;
    public int healthCountFinal = 0;
    public int politicsCountFinal = 0;
    
    private final Dictionary<String, Integer> keyList = new Hashtable<String, Integer>();
    
    private KafkaTemplate<String, String> template;

    public topicConsumer (KafkaTemplate<String, String> template){
        this.template = template;
    }

    @KafkaListener(topics = "myTopic", groupId = "teletica")
    public void listen(String message) {

        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        String URL = jsonObject.get("news_url").getAsString();
        String ID = jsonObject.get("id").getAsString();
        //String HEADLINE = jsonObject.get("headline").getAsString();

        if (NewsUtil.getHtmlDocument(URL).getElementById("content") == null){ //para cuando la pagina es incorrecta
            keyList.put("Events", eventsCountFinal);
            keyList.put("Health", healthCountFinal);
            keyList.put("Politics", politicsCountFinal);
            template.send("myTopic", new Gson().toJson(new News(ID, keyList)));//There is no info o String vacio
        }
        else{
            if(NewsUtil.getHtmlDocument(URL).getElementById("content").getElementsByClass("text-editor").isEmpty()){ //para cuando no encunetra la noticia
                keyList.put("Events", eventsCountFinal);
                keyList.put("Health", healthCountFinal);
                keyList.put("Politics", politicsCountFinal);
                template.send("myTopic", new Gson().toJson(new News(ID, keyList)));//There is no info o String vacio
            }
            else{
                //con esto podemos ver cuantos parrafos podemos usar
                Elements paragraphs = NewsUtil.getHtmlDocument(URL).getElementById("content").getElementsByClass("text-editor").get(0).getElementsByTag("p");
                int tamano = paragraphs.size();
                System.out.println(tamano);

                for (int i = 0; i < tamano; i++) {
                    Connection c = null;
                    
                    try {
                        String textExtract = paragraphs.get(i).ownText();
                        //System.out.println(textExtract);
                        c = connectSqlite.connect();
                        //Threads para ejecutar el parrafo con los tres metodos de cada tabla
                        
                        //Thread eventsThread
                        
                            eventsCountFinal += puller.puller_methodEvents(textExtract, c);

                        //Thread healthThread
                        
                            healthCountFinal += puller.puller_methodHealth(textExtract, c);

                        //Thread politicsThread
                        
                            politicsCountFinal += puller.puller_methodPolitics(textExtract, c);


                    } catch (Exception e) {
                        //TODO: handle exception
                        System.err.println(e.getMessage());
                    }
                    finally {
                        try {
                            if (c != null) {
                                c.close();
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }

                try {

                    keyList.put("Events", eventsCountFinal);
                    keyList.put("Health", healthCountFinal);
                    keyList.put("Politics", politicsCountFinal);

                    template.send("myTopic", new Gson().toJson(new News(ID, keyList)));//article_text

                } catch (Exception e) {
                    //TODO: handle exception
                    System.err.println("excepcion a la hora de cargar el json final");
                }
            }
        }
    }
}
