package com.example.traveling_app.model.other;

public class Language {
    private int image;
    private String name;

    public Language(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public Language() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
