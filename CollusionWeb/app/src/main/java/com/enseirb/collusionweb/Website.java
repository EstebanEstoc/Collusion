package com.enseirb.collusionweb;

public class Website {
    private String url;
    private float rating;

    public Website(String url, float rating) {
        this.url = url;
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
