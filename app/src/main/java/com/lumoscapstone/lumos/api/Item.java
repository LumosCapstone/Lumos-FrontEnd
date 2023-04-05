package com.lumoscapstone.lumos.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Based on backend's API_outline.md as of April 4th, 2023
public class Item {
    // Object Keys
    private int id;
    private String name;
    private String type;
    private int quantity;
    private String seller;
    private String sellerEmail;
    private String sellerPhone;
    private List<String> imageURL;
    // TODO: (?) maybe uncomment distance variable, it's seen being returned using the get items based on max distance API call but not in item/:id API call
    // private String distance; // Currently represented as string according to API Outline: "distance": "< 5 miles"

    // JSON object as a string
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

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public List<String> getImageURL() {
        return imageURL;
    }

    public String getText() {
        return text;
    }
}
