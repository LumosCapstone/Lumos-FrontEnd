package com.lumoscapstone.lumos.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationBarView;
import com.lumoscapstone.lumos.R;

import com.lumoscapstone.lumos.api.Item;
import com.lumoscapstone.lumos.api.LumosAPI;
import com.lumoscapstone.lumos.databinding.ActivityHomePageBinding;
import com.lumoscapstone.lumos.login.LoginActivity;

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

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.search:
                        replaceFragment(new SearchFragment());
                        break;
                    case R.id.chat:
                        replaceFragment(new ChatFragment());
                        break;
                    case R.id.profile:
                        replaceFragment(new ProfileFragment());
                        break;
                }

                return true;
            }
        });

        // Temporary Logout Button, should be moved to profile page when it exists
        /*Button logoutButton = binding.tempLogoutButton;

        // Get user shared preferences
        getPrefs();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear all user preferences
                resetAllUserPreferences();

                // Redirect to login page
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });*/


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void getPrefs(){
        mSharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
    }

    private void resetAllUserPreferences(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

//    private void verifyUserLogin() {
//        LoginRequest loginRequest = new LoginRequest(mEmail, mPassword);
//
//        Call<LoginResponse> call = lumosAPI.verifyLogin(loginRequest);
//
//        call.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                handleLoginResponse(response);
//            }

    private void grabItem(){
        int itemId = 1;
        Call<Item> item = lumosAPI.getItemInfo(1);
        item.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {

            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }


        });

    }
//    private void handleLoginResponse(Response<LoginResponse> response) {
//        if (!response.isSuccessful()) {
//            // handle the error case
//            Log.e("response_error", "Response Code: response.code()");
//            loginError(response.code());
//            return;
//        }
//
//        LoginResponse loginResponse = response.body();
//        if(response.body() == null){
//            loginError(-1);
//        }
//        assert loginResponse != null;
//        mUserId = loginResponse.getId();
//
//        // Update that the user is now logged in shared preferences
//        updateSharedPreferences();
//
//        // Send user to home page
//        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
//        startActivity(intent);
//    }



//    private void handleLoginResponse(Response<LoginResponse> response) {
//        if (!response.isSuccessful()) {
//            // handle the error case
//            Log.e("response_error", "Response Code: response.code()");
//            loginError(response.code());
//            return;
//        }
//
//        LoginResponse loginResponse = response.body();
//        if(response.body() == null){
//            loginError(-1);
//        }
//        assert loginResponse != null;
//        mUserId = loginResponse.getId();
//
//        // Update that the user is now logged in shared preferences
//        updateSharedPreferences();
//
//        // Send user to home page
//        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
//        startActivity(intent);
//    }

    private void itemInfoResponse(Response<Item> response){
        if (!response.isSuccessful()){
            return;
        }
        Item item = response.body();


    }

}