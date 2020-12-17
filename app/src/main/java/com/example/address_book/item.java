package com.example.address_book;

public class item {
    private String name;
    private String imageUrl;
    public item(String name, String imageUrl){
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName(){
        return name;
    }

    public String getImageId(){
        return imageUrl;
    }
}
