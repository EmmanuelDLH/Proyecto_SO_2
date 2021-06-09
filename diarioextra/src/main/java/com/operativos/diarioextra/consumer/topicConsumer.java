package com.operativos.diarioextra.consumer;

import org.jsoup.select.Elements;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.operativos.diarioextra.News;
import com.operativos.diarioextra.NewsUtil;
import com.operativos.diarioextra.puller2;
import com.operativos.diarioextra.getconsume.GetRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class topicConsumer {
    
    private KafkaTemplate<String, String> template;

    public topicConsumer (KafkaTemplate<String, String> template){
        this.template = template;
    }

    @KafkaListener(topics = "newsdiarioextra", groupId = "diarioextra")
    public void listen(String message) {

        int eventsCountFinal = 0;
        int healthCountFinal = 0;
        int politicsCountFinal = 0;
        int sportsCountFinal = 0;
        int economyCountFinal = 0;
        int entertainmentCountFinal = 0;

        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();

        String URL = jsonObject.get("news_url").getAsString();
        String title = "";

        if (NewsUtil.getHtmlDocument(URL).getElementById("content") == null){ //para cuando la pagina es incorrecta
            String stringValueofKeyList = "{Politics: " + politicsCountFinal + ", "
                        + "Events: " + eventsCountFinal + ", "
                        + "Entertainment: " + entertainmentCountFinal + ", "
                        + "Sports: " + sportsCountFinal + ", "
                        + "Health: " + healthCountFinal + ", "
                        + "Economy: " + economyCountFinal + "}";
            template.send("article_text", new Gson().toJson(new News(title, stringValueofKeyList)));//There is no info o String vacio
        }
        else{
            if(NewsUtil.getHtmlDocument(URL).getElementById("content").getElementsByClass("text-editor").isEmpty()){ //para cuando no encunetra la noticia
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
                Elements paragraphs = NewsUtil.getHtmlDocument(URL).getElementById("container").getElementsByTag("article").get(0).getElementsByTag("p");
                title = NewsUtil.getHtmlDocument(URL).getElementById("container").getElementsByTag("figcaption").get(0).getElementsByTag("h1").first().text();
                int tamano = paragraphs.size();

                //hacemos la llamada al request para obtener las palabras
                GetRequest.request();
                
                for (int i = 0; i < tamano - 1; i++) {
                    
                    String textExtract = paragraphs.get(i).ownText();
                    
                    eventsCountFinal += puller2.puller_methodEvents(textExtract);
                    healthCountFinal += puller2.puller_methodHealth(textExtract);
                    politicsCountFinal += puller2.puller_methodPolitics(textExtract);
                    sportsCountFinal += puller2.puller_methodSports(textExtract);
                    economyCountFinal += puller2.puller_methodEconomy(textExtract);
                    entertainmentCountFinal += puller2.puller_methodEntertainment(textExtract);
                }

                String stringValueofKeyList = "{Politics: " + politicsCountFinal + ", "
                    + "Events: " + eventsCountFinal + ", "
                    + "Entertainment: " + entertainmentCountFinal + ", "
                    + "Sports: " + sportsCountFinal + ", "
                    + "Health: " + healthCountFinal + ", "
                    + "Economy: " + economyCountFinal + "}";

                template.send("article_text", new Gson().toJson(new News(title, stringValueofKeyList)));
            }
        }
    }

}
