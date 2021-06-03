package com.operativos.crhoy.consumer;

import org.jsoup.select.Elements;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.operativos.crhoy.News;
import com.operativos.crhoy.NewsUtil;
import com.operativos.crhoy.puller;
import com.operativos.crhoy.sqliteConnect.connectSqlite;

import java.sql.Connection;
import java.util.Dictionary;
import java.util.Hashtable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class topicConsumer {
    
    private KafkaTemplate<String, String> template;

    public topicConsumer (KafkaTemplate<String, String> template){
        this.template = template;
    }

    @KafkaListener(topics = "myTopic", groupId = "crhoy")
    public void listen(String message) {

        int eventsCountFinal = 0;
        int healthCountFinal = 0;
        int politicsCountFinal = 0;
        
        Dictionary<String, Integer> keyList = new Hashtable<String, Integer>();

        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
    
        String ID = jsonObject.get("id").getAsString();
        String URL = jsonObject.get("news_url").getAsString();

        //*[@id="contenido"]/div[1]

        if (NewsUtil.getHtmlDocument(URL).getElementsByClass("errorContainer container") != null){ //para cuando la pagina es incorrecta
            keyList.put("Events", eventsCountFinal);
            keyList.put("Health", healthCountFinal);
            keyList.put("Politics", politicsCountFinal);
            template.send("article_text", new Gson().toJson(new News(ID, keyList)));//There is no info o String vacio
        }
        else{
            if(NewsUtil.getHtmlDocument(URL).getElementById("contenido").getElementsByTag("div").isEmpty()){ //para cuando no encunetra la noticia
                keyList.put("Events", eventsCountFinal);
                keyList.put("Health", healthCountFinal);
                keyList.put("Politics", politicsCountFinal);
                template.send("article_text", new Gson().toJson(new News(ID, keyList)));//There is no info o String vacio
            }
            else{
                //con esto podemos ver cuantos parrafos podemos usar
                Elements paragraphs = NewsUtil.getHtmlDocument(URL).getElementById("contenido").getElementsByTag("div").get(0).getElementsByTag("p");
                int tamano = paragraphs.size();
                System.out.println(tamano);

                Connection c = null;

                try {
                    c = connectSqlite.connect();

                    for (int i = 0; i < tamano - 1; i++) {
                        try {

                            String textExtract = paragraphs.get(i).ownText();
                            eventsCountFinal += puller.puller_methodEvents(textExtract, c);
                            healthCountFinal += puller.puller_methodHealth(textExtract, c);
                            politicsCountFinal += puller.puller_methodPolitics(textExtract, c);

                        } catch (Exception e) {
                            //TODO: handle exception
                            System.err.println(e.getMessage());
                        }
                    }

                    try {

                        keyList.put("Events", eventsCountFinal);
                        keyList.put("Health", healthCountFinal);
                        keyList.put("Politics", politicsCountFinal);

                        template.send("article_text", new Gson().toJson(new News(ID, keyList)));//article_text

                    } catch (Exception e) {
                        //TODO: handle exception
                        System.err.println("excepcion a la hora de cargar el json final");
                    }
                    
                } catch (Exception e) {
                    //TODO: handle exception

                }finally {
                    try {
                        if (c != null) {
                            c.close();
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }
    }
}
