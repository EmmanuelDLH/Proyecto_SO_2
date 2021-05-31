package com.operativos.teletica;

import java.util.ArrayList;
import java.util.List;

public class News{

    private String id;
    private String headline;//no lo voy a necesitar
    private String text;
    private String news_url;
    private List<String> keyWordsList;

    public News() {
        super(); 
    }

    public News(String headline){
        super();
        this.headline = headline;
    }

    public News(String id, String text){
        super();
        this.id = id;
        this.text = text;
    }

    public News(String id, String text, ArrayList<String> keyWordsList){
        super();
        this.id = id;
        this.text = text;
        this.keyWordsList = keyWordsList;
    }

    public News(String id, String text, String headline, ArrayList<String> keyWordsList){
        super();
        this.id = id;
        this.headline = headline;
        this.text = text;
        this.keyWordsList = keyWordsList;
    }

    public News(String id, String headline, String text, String news_url, ArrayList<String> keyWordsList) {
        super();
        this.id = id;
        this.headline = headline;
        this.text = text;
        this.news_url = news_url;
        this.keyWordsList = keyWordsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitular() {
        return headline;
    }

    public void setTitular(String headline) {
        this.headline = headline;
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

    public List<String> getKeyWordsList() {
        return keyWordsList;
    }

    public void setKeyWordsList(List<String> keyWordsList) {
        this.keyWordsList = keyWordsList;
    }

}