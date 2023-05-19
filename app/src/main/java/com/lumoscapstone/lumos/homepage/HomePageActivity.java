package com.lumoscapstone.lumos.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lumoscapstone.lumos.R;

import com.lumoscapstone.lumos.api.Item;
import com.lumoscapstone.lumos.api.LoginResponse;
import com.lumoscapstone.lumos.api.LumosAPI;
import com.lumoscapstone.lumos.api.ReserveItemResponse;
import com.lumoscapstone.lumos.databinding.ActivityHomePageBinding;
import com.lumoscapstone.lumos.login.LoginActivity;
import com.lumoscapstone.lumos.profile.ProfileActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePageActivity extends AppCompatActivity {
    private static final String PREFERENCES_KEY = "com.lumoscapstone.lumos.PREFERENCES_KEY";

    ActivityHomePageBinding binding;

    // User Shared Preferences
    SharedPreferences mSharedPreferences;

    private float mLatitude;
    private float mLongitude;
    private int maxDistance;
    private List<String> itemTypes;
    private List<Item> currentItems;
    private Item currItem = null; // populated when itemIsReserved(itemId) is called
    private boolean checkIsReserved;
    boolean itemReserved;

    // Set up RetroFit object
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://lumos.benjaminwoodward.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final LumosAPI lumosAPI = retrofit.create(LumosAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Temporary Logout Button, should be moved to profile page when it exists
        Button logoutButton = binding.tempLogoutButton;
        Button profileButton = binding.tempProfileButton;

        // Get user shared preferences
        getPrefs();

        // Load all items within 9999 miles
        // onLoad();

        // Test: itemIsReserved(int itemId) (some item ID's to test: 1, 4, 6)
        // itemIsReserved(1);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear all user preferences
                resetAllUserPreferences();

                // Redirect to login page
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Redirect to profile page
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getPrefs(){
        mSharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
    }

    private void resetAllUserPreferences(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Grabs all items in database but would help to ask backend team
    private void onLoad(){
        // Using this max distance grabs all items, will want to replace lat and longi values with user
        // values from location services
        Call<List<Item>> call = lumosAPI.getItems(10, 10, 9999, null);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                handleItemLoad(response);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(HomePageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void handleItemLoad(Response<List<Item>> response){
        if(!response.isSuccessful()){
            Toast.makeText(getApplicationContext(), "Error: Unable to load any items at this time.", Toast.LENGTH_LONG).show();
            Log.e("response_error", "Response Code: response.code()");
            return;
        } else {

            List<Item> items = response.body();
            currentItems = items;

            for(Item item : items){
                // TODO: display items in fragment list
            }
        }
    }

    // Check if item is reserved and returns a boolean, this also get item information based on item id you pass in
    // this item info is stored in Item variable called currItem (will remain null if item is reserved/ store previously
    // requested Items info so be weary of this)
    private boolean itemIsReserved(int itemId){
        Call<Item> callItem = lumosAPI.getItemById(itemId);

        callItem.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                handleGetItemById(response);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(HomePageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Toast.makeText(getApplicationContext(), "Item " + itemId + " is reserved: " + checkIsReserved, Toast.LENGTH_SHORT).show();
        return checkIsReserved;
    }

    private void handleGetItemById(Response<Item> response){
        if(!response.isSuccessful()){
            checkIsReserved = true;
        } else {
            checkIsReserved = false;
            currItem = response.body();
        }
    }

//    private boolean reserveItem(int itemId, int userId){
//        Call<ReserveItemResponse> reserveItemCall = lumosAPI.reserveItemById(itemId, userId);
//
//        reserveItemCall.enqueue(new Callback<ReserveItemResponse>() {
//            @Override
//            public void onResponse(Call<ReserveItemResponse> call, Response<ReserveItemResponse> response) {
//                handleReserveItem(response);
//            }
//
//            @Override
//            public void onFailure(Call<ReserveItemResponse> call, Throwable t) {
//                Toast.makeText(HomePageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        // for now confirm all reservations by seller so call this function
////        sellerConfirmReservation(itemId, userId);
//
//        Toast.makeText(getApplicationContext(), "Item was reserved: " + itemReserved, Toast.LENGTH_LONG).show();
//
//        return itemReserved;
//    }
//
//    private void handleReserveItem(Response<ReserveItemResponse> response){
//        if(!response.isSuccessful()){
//            Toast.makeText(getApplicationContext(), "Error while attempting to reserve item. Response Code: " + response.code(), Toast.LENGTH_LONG).show();
//            itemReserved = false;
//        } else {
//            ReserveItemResponse reserveItemResponse = response.body();
//
//            assert reserveItemResponse != null;
//            if(reserveItemResponse.getError().equals("none")){
//                Toast.makeText(getApplicationContext(), reserveItemResponse.getOk(), Toast.LENGTH_LONG).show();
//                itemReserved = true;
//            } else {
//                Toast.makeText(getApplicationContext(), reserveItemResponse.getError(), Toast.LENGTH_LONG).show();
//                itemReserved = false;
//            }
//        }
//    }
//
//    private void sellerConfirmReservation(int itemId, int reserverUserId){
//        Call<ReserveItemResponse> confirmReservationCall = lumosAPI.confirmItemReservation(itemId, reserverUserId);
//
//        confirmReservationCall.enqueue(new Callback<ReserveItemResponse>() {
//            @Override
//            public void onResponse(Call<ReserveItemResponse> call, Response<ReserveItemResponse> response) {
//                handleSellerReservationConfirmation(response);
//            }
//
//            @Override
//            public void onFailure(Call<ReserveItemResponse> call, Throwable t) {
//                Toast.makeText(HomePageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void handleSellerReservationConfirmation(Response<ReserveItemResponse> response){
//        if(!response.isSuccessful()){
//            Toast.makeText(getApplicationContext(), "Seller reservation confirmation failed.", Toast.LENGTH_LONG).show();
//        }
//
//        ReserveItemResponse reservationResponse = response.body();
//        assert reservationResponse != null;
//        if(reservationResponse.getError().equals("none")){
//            Toast.makeText(getApplicationContext(), "Seller reservation confirmation: "  + reservationResponse.getOk(), Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getApplicationContext(), "Seller reservation confirmation error: " + reservationResponse.getError(), Toast.LENGTH_LONG).show();
//        }
//    }
//
//    // itemId = item to be reutnred
//    // userId = userId of the user who would like to return currently reserved item
//    private void returnItem(int itemId, int userId){
//        Call<ReserveItemResponse> returnItemCall = lumosAPI.returnItem(itemId, userId);
//
//        returnItemCall.enqueue(new Callback<ReserveItemResponse>() {
//            @Override
//            public void onResponse(Call<ReserveItemResponse> call, Response<ReserveItemResponse> response) {
//                handleItemReturn(response);
//            }
//
//            @Override
//            public void onFailure(Call<ReserveItemResponse> call, Throwable t) {
//                Toast.makeText(HomePageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void handleItemReturn(Response<ReserveItemResponse> response){
//        if(!response.isSuccessful()){
//            Toast.makeText(getApplicationContext(), "Unable to return item at this time.", Toast.LENGTH_LONG).show();
//        }
//
//        ReserveItemResponse reservationResponse = response.body();
//        assert reservationResponse != null;
//        if(reservationResponse.getError().equals("none")){
//            Toast.makeText(getApplicationContext(), reservationResponse.getOk(), Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getApplicationContext(), reservationResponse.getError(), Toast.LENGTH_LONG).show();
//        }
//    }

}