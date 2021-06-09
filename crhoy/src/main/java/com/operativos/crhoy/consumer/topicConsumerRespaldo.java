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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class topicConsumerRespaldo {
    
    private KafkaTemplate<String, String> template;

    public topicConsumerRespaldo (KafkaTemplate<String, String> template){
        this.template = template;
    }

    @KafkaListener(topics = "newscrhoy", groupId = "crhoy")
    public void listen(String message) {

        int eventsCountFinal = 0;
        int healthCountFinal = 0;
        int politicsCountFinal = 0;
        int sportsCountFinal = 0;
        int economyCountFinal = 0;
        int entertainmentCountFinal = 0;

        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
    
        String title = "";
        String URL = jsonObject.get("news_url").getAsString();

        //*[@id="contenido"]/div[1]

        if (NewsUtil.getHtmlDocument(URL).getElementsByClass("errorContainer container") != null){ //para cuando la pagina es incorrecta
            String stringValueofKeyList = "{Politics: " + politicsCountFinal + ", "
                        + "Events: " + eventsCountFinal + ", "
                        + "Entertainment: " + entertainmentCountFinal + ", "
                        + "Sports: " + sportsCountFinal + ", "
                        + "Health: " + healthCountFinal + ", "
                        + "Economy: " + economyCountFinal + "}";
            template.send("article_text", new Gson().toJson(new News(title, stringValueofKeyList)));//There is no info o String vacio
        }
        else{
            if(NewsUtil.getHtmlDocument(URL).getElementById("contenido").getElementsByTag("div").isEmpty()){ //para cuando no encunetra la noticia
                String stringValueofKeyList = "{Politics: " + politicsCountFinal + ", "
                            + "Events: " + eventsCountFinal + ", "
                            + "Entertainment: " + entertainmentCountFinal + ", "
                            + "Sports: " + sportsCountFinal + ", "
                            + "Health: " + healthCountFinal + ", "
                            + "Economy: " + economyCountFinal + "}";
                template.send("article_text", new Gson().toJson(new News(title, stringValueofKeyList)));//There is no info o String vacio
            }
            else{
                //con esto podemos ver cuantos parrafos podemos usar
                Elements paragraphs = NewsUtil.getHtmlDocument(URL).getElementById("contenido").getElementsByTag("div").get(0).getElementsByTag("p");
                title = NewsUtil.getHtmlDocument(URL).getElementById("contenido").getElementsByClass("text-left titulo").first().text();
                int tamano = paragraphs.size();

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

                        String stringValueofKeyList = "{Politics: " + politicsCountFinal + ", "
                                    + "Events: " + eventsCountFinal + ", "
                                    + "Entertainment: " + entertainmentCountFinal + ", "
                                    + "Sports: " + sportsCountFinal + ", "
                                    + "Health: " + healthCountFinal + ", "
                                    + "Economy: " + economyCountFinal + "}";
                        template.send("article_text", new Gson().toJson(new News(title, stringValueofKeyList)));//article_text

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
