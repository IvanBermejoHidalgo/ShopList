package com.example.socialpuig.Domain;


public class CategoryDomain {
    private String title;
    private int id;
    private String picUrl;
    private String url; // Nueva variable para almacenar la URL

    public CategoryDomain(String title, int id, String picUrl, String url) {
        this.title = title;
        this.id = id;
        this.picUrl = picUrl;
        this.url = url;
    }

    public CategoryDomain() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
