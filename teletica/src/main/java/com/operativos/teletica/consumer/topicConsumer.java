package com.operativos.teletica.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.w3c.dom.html.HTMLCollection;

import com.operativos.teletica.News;
import com.operativos.teletica.NewsUtil;
import com.operativos.teletica.puller;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.operativos.teletica.puller;

@Component
public class topicConsumer {
    
    //private final List<String> messages = new ArrayList<>();
    private KafkaTemplate<String, String> template;

    public topicConsumer (KafkaTemplate<String, String> template){
        this.template = template;
    }

    @KafkaListener(topics = "myTopic", groupId = "teletica")
    public void listen(String message) {
        /*synchronized (messages) {
            messages.add(message);
        }*/

        //*[@id="content"]/div/article/div/div[2]/article

        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        String URL = jsonObject.get("news_url").getAsString();
        String ID = jsonObject.get("id").getAsString();
        //String HEADLINE = jsonObject.get("headline").getAsString();

        if (NewsUtil.getHtmlDocument(URL).getElementById("content") == null){ //para cuando la pagina es incorrecta
            template.send("myTopic", new Gson().toJson(new News(ID,"There is no info")));//There is no info o String vacio
        }
        else{
            if(NewsUtil.getHtmlDocument(URL).getElementById("content").getElementsByClass("text-editor").isEmpty()){ //para cuando no encunetra la noticia
                template.send("myTopic", new Gson().toJson(new News(ID,"There is no info")));//There is no info o String vacio
            }
            else{
                //con esto podemos ver cuantos parrafos podemos usar
                HTMLCollection paragraphs = (HTMLCollection) NewsUtil.getHtmlDocument(URL).getElementById("content").getElementsByClass("text-editor").get(0).getElementsByTag("p");
                int tamano = paragraphs.getLength();

                for (int i = 0; i < tamano; i++) {
                    try {
                        String textExtract = paragraphs.item(i).getTextContent();
                        
                        //llamar metodo puller
                        ArrayList<String> keyList = puller.puller_method(textExtract);

                        String jsonVar = new Gson().toJson(new News(ID, textExtract, keyList));
                        template.send("myTopic", jsonVar);//article_text

                    } catch (Exception e) {
                        //TODO: handle exception

                    }
                }
            }
        }
    }

    /*public List<String> getMessages() {
        return messages;
    }*/
}
