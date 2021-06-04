package com.operativos.diarioextra.consumer;

import org.jsoup.select.Elements;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.operativos.diarioextra.News;
import com.operativos.diarioextra.NewsUtil;
import com.operativos.diarioextra.puller;
import com.operativos.diarioextra.sqliteConnect.connectSqlite;

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

    @KafkaListener(topics = "myTopic", groupId = "diarioextra")
    public void listen(String message) {

        //*[@id="container"]/main/div[1]/article

        int eventsCountFinal = 0;
        int healthCountFinal = 0;
        int politicsCountFinal = 0;
        int sportsCountFinal = 0;
        int economyCountFinal = 0;
        int entertainmentCountFinal = 0;
        
        Dictionary<String, Integer> keyList = new Hashtable<String, Integer>();

        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
    
        String ID = jsonObject.get("id").getAsString();
        String URL = jsonObject.get("news_url").getAsString();

        if (NewsUtil.getHtmlDocument(URL).getElementById("container") == null){ //para cuando la pagina es incorrecta
            keyList.put("Events", eventsCountFinal);
            keyList.put("Health", healthCountFinal);
            keyList.put("Politics", politicsCountFinal);
            keyList.put("Sports", sportsCountFinal);
            keyList.put("Economy", economyCountFinal);
            keyList.put("Entertainment", entertainmentCountFinal);
            template.send("article_text", new Gson().toJson(new News(ID, keyList)));//There is no info o String vacio
        }
        else{
            if(NewsUtil.getHtmlDocument(URL).getElementById("container").getElementsByTag("article").isEmpty()){ //para cuando no encunetra la noticia
                keyList.put("Events", eventsCountFinal);
                keyList.put("Health", healthCountFinal);
                keyList.put("Politics", politicsCountFinal);
                keyList.put("Sports", sportsCountFinal);
                keyList.put("Economy", economyCountFinal);
                keyList.put("Entertainment", entertainmentCountFinal);
                template.send("article_text", new Gson().toJson(new News(ID, keyList)));//There is no info o String vacio
            }
            else{
                //con esto podemos ver cuantos parrafos podemos usar
                Elements paragraphs = NewsUtil.getHtmlDocument(URL).getElementById("container").getElementsByTag("article").get(0).getElementsByTag("p");
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
                            sportsCountFinal += puller.puller_methodSports(textExtract, c);
                            economyCountFinal += puller.puller_methodEconomy(textExtract, c);
                            entertainmentCountFinal += puller.puller_methodEntertainment(textExtract, c);

                        } catch (Exception e) {
                            //TODO: handle exception
                            System.err.println(e.getMessage());
                        }
                    }

                    try {

                        keyList.put("Events", eventsCountFinal);
                        keyList.put("Health", healthCountFinal);
                        keyList.put("Politics", politicsCountFinal);
                        keyList.put("Sports", sportsCountFinal);
                        keyList.put("Economy", economyCountFinal);
                        keyList.put("Entertainment", entertainmentCountFinal);

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
