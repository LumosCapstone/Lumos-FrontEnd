package com.lumoscapstone.lumos.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LumosAPI {
    // Notes:
    // Only enter the relative url, base URL will be passed in later
    // Relative url part as an example: https://baseUrlForOurAPI.com/relative_url
    // RetroFit will auto-generate the code for these methods

    // Get list of items based on distance
    // TODO: add getter method which returns a list of items based on a max distance (may need to modify Item.java's member variables)

    // Get item details by item ID
    @GET("api/item/{id}")
    Call<Item> getItem(@Path("id") int itemId);

    // Attempt to reserve an item
    // TODO: add definition of this POST method
    // Resource to help with this: https://www.youtube.com/watch?v=GP5OyYDu_mU&list=PLrnPJCHvNZuCbuD3xpfKzQWOj3AXybSaM&index=3
}
