package com.example.identity_info_control.Adapter;

public class item {
    private String id;
    private String name;
    private String imageUrl;
    public item(String id, String name, String imageUrl){
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName(){
        return name;
    }

    public String getImageId(){
        return imageUrl;
    }

    public String getId(){
        return id;
    }
}
