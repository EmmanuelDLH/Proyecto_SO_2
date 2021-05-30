package com.operativos.teletica;

public class News{

    private String id;
    private String headline;
    private String text;
    private String news_url;

    public News() {
        super();
    }

    public News(String headline){
        super();
        this.headline = headline;
    }

    public News(String id, String headline){
        super();
        this.id = id;
        this.headline = headline;
    }

    public News(String id, String headline, String text){
        super();
        this.id = id;
        this.headline = headline;
        this.text = text;
    }

    public News(String id, String headline, String text, String news_url) {
        super();
        this.id = id;
        this.headline = headline;
        this.text = text;
        this.news_url = news_url;
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

}