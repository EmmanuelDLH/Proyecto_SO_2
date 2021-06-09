package com.operativos.repretel;

public class News{

    private String title;
    private String news_url;
    private String keyWordsList;

    public News() {
        super(); 
    }

    public News(String title, String news_url, String keyWordsList) {
        super();
        this.title = title;
        this.news_url = news_url;
        this.keyWordsList = keyWordsList;
    }

    public News(String title, String keyWordsList) {
        super();
        this.title = title;
        this.keyWordsList = keyWordsList;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getKeyWordsList() {
        return keyWordsList;
    }

    public void setKeyWordsList(String keyWordsList) {
        this.keyWordsList = keyWordsList;
    }
    
}