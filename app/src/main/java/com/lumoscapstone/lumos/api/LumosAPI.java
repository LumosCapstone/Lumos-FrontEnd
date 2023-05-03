package com.lumoscapstone.lumos.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LumosAPI {
    // Notes:
    // Only enter the relative url, base URL will be passed in later
    // Relative url part as an example: https://baseUrlForOurAPI.com/relative_url
    // Example URL: https://lumos.benjaminwoodward.dev/api/item/1
    // RetroFit will auto-generate the code for these methods

    // Get user information via id
    @GET("api/user/{id}")
    Call<User> getUser(@Path("id") int userId);

    // Verify user log in + grab user id
    @POST("api/login")
    Call<LoginResponse> verifyLogin(@Body LoginRequest loginRequest);

    // Get list of items based on distance
    // TODO: add getter method which returns a list of items based on a max distance (may need to modify Item.java's member variables)

    // Get item details by item ID (not sure if correct)
//    @GET("api/item/{id}")
//    Call<Item> getItem(@Path("id") int itemId);
    @POST("api/register")
    Call<User> registerUser(@Body UserRegister register);
    // Attempt to reserve an item
    // TODO: add definition of this POST method
    // Resource to help with this: https://www.youtube.com/watch?v=GP5OyYDu_mU&list=PLrnPJCHvNZuCbuD3xpfKzQWOj3AXybSaM&index=3
}
