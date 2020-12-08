package com.example.javaradio.models;

public class Radio {

    public String name, tagline, color, desc, url, icon, image, category, id;

    public Radio(String name, String tagline, String color, String desc, String url, String icon, String image, String category, String id) {
        this.name = name;
        this.tagline = tagline;
        this.color = color;
        this.desc = desc;
        this.url = url;
        this.icon = icon;
        this.image = image;
        this.category = category;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Radio{" +
                "name='" + name + '\'' +
                ", tagline='" + tagline + '\'' +
                ", color='" + color + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", image='" + image + '\'' +
                ", category='" + category + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
