package com.lumoscapstone.lumos.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Call<UserResponse> registerUser(@Body UserRegister register);
    // Attempt to reserve an item
    // TODO: add definition of this POST method
    // Resource to help with this: https://www.youtube.com/watch?v=GP5OyYDu_mU&list=PLrnPJCHvNZuCbuD3xpfKzQWOj3AXybSaM&index=3

    @GET("api/item")
    Call<List<Item>> getItems(
            @Query("lat") float lat,
            @Query("long") float longi,
            @Query("max_distance") int maxDistance,
            @Query("type") String itemType);

    @GET("api/item/{id}")
    Call<Item> getItemById(@Path("id") int itemId);

    // These don't work due to what is believed to be RetroFit limitation, look into using URLEncoded format (API must support it) or changing the API call to take query params
    // or path variables only. Last method to try would be a QueryMap.
//    @GET("api/item/reserve/{id}")
//    Call<ReserveItemResponse> reserveItemById(@Path("id") int itemId, @Query("user_id") int userId);
//
//    @POST("api/item/confirm-reservation/{id}")
//    Call<ReserveItemResponse> confirmItemReservation(@Path("id") int itemId, @Query("user_id") int reserverUserId);
//
//    @POST("api/item/return/{id}")
//    Call<ReserveItemResponse> returnItem(@Path("id") int itemId, @Query("user_id") int userId);
}
