package com.operativos.crhoy;

import java.util.Dictionary;

public class News{

    private String id;
    private String text;
    private String news_url;
    private Dictionary<String, Integer> keyWordsList;

    public News() {
        super(); 
    }

    public News(String id, String text){
        super();
        this.id = id;
        this.text = text;
    }

    public News(String id, String text, Dictionary<String, Integer> keyWordsList){
        super();
        this.id = id;
        this.text = text;
        this.keyWordsList = keyWordsList;
    }

    public News(String id, String text, String news_url, Dictionary<String, Integer> keyWordsList) {
        super();
        this.id = id;
        this.text = text;
        this.news_url = news_url;
        this.keyWordsList = keyWordsList;
    }

    public News(String id, Dictionary<String, Integer> keyWordsList) {
        super();
        this.id = id;
        this.keyWordsList = keyWordsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public Dictionary<String, Integer> getKeyWordsList() {
        return keyWordsList;
    }

    public void setKeyWordsList(Dictionary<String, Integer> keyWordsList) {
        this.keyWordsList = keyWordsList;
    }
    
}