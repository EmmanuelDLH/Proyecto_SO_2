package com.operativos.teletica.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.w3c.dom.html.HTMLCollection;

import com.operativos.teletica.News;
import com.operativos.teletica.NewsUtil;
import com.operativos.teletica.puller;

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

                        //Threads para ejecutar el parrafo con los tres metodos de cada tabla
                        
                        //Thread eventsThread
                        new Thread(){
                            public void run(){
                              //System.out.println("Thread Running");
                                eventsCountFinal += puller.puller_methodEvents(textExtract);
                            }
                        }.start();
                
                        //Thread healthThread
                        new Thread(){
                            public void run(){
                              //System.out.println("Thread Running");
                              healthCountFinal += puller.puller_methodHealth(textExtract);
                            }
                        }.start();
                
                        //Thread politicsThread
                        new Thread(){
                            public void run(){
                              //System.out.println("Thread Running");
                              politicsCountFinal += puller.puller_methodPolitics(textExtract);
                            }
                        }.start();

                    } catch (Exception e) {
                        //TODO: handle exception

                    }
                }

                keyList.put("Events", eventsCountFinal);
                keyList.put("Health", healthCountFinal);
                keyList.put("Politics", politicsCountFinal);

                String jsonVar = new Gson().toJson(new News(ID, keyList));
                template.send("myTopic", jsonVar);//article_text
            }
        }
    }
}
