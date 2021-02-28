package com.nent.techtest.model;

public class Album {
    String title;
    String id;
    String Image;

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return Image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Album() {
    }

    public Album(String title, String id, String image) {
        this.title = title;
        this.id = id;
        Image = image;
    }

    @Override
    public String toString() {
        return "Album{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", Image='" + Image + '\'' +
                '}';
    }


}
