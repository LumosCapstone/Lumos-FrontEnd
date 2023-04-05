package com.lumoscapstone.lumos.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lumoscapstone.lumos.R;

import com.lumoscapstone.lumos.databinding.ActivityHomePageBinding;
import com.lumoscapstone.lumos.login.LoginActivity;

public class HomePageActivity extends AppCompatActivity {
    private static final String PREFERENCES_KEY = "com.lumoscapstone.lumos.PREFERENCES_KEY";

    ActivityHomePageBinding binding;

    // User Shared Preferences
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Temporary Logout Button, should be moved to profile page when it exists
        Button logoutButton = binding.tempLogoutButton;

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
}