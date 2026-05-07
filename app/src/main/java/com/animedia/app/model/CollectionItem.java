package com.animedia.app.model;

import java.io.Serializable;

public class CollectionItem implements Serializable {
    private int id;
    private String title;
    private String imageUrl;

    public CollectionItem() {}

    public CollectionItem(int id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
