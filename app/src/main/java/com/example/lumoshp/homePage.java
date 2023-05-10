package com.example.lumoshp;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.URL;

public class homePage {

    private int id;
    private String name;
    private String type;
    private int quantity;
    private String seller;
    private String seller_email;
    private int seller_phone;

    //n
//    private char images;

    @SerializedName("body")
    private String text;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSeller() {
        return seller;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public int getSeller_phone() {
        return seller_phone;
    }

//    public char getImages() {
//        return images;
//    }
//
//    public void setImages(char images) {
//        this.images = images;
//    }

}
