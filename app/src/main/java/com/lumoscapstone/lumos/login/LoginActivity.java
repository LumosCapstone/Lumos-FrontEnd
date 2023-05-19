package com.lumoscapstone.lumos.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lumoscapstone.lumos.R;
import com.lumoscapstone.lumos.api.LoginRequest;
import com.lumoscapstone.lumos.api.LoginResponse;
import com.lumoscapstone.lumos.api.LumosAPI;
import com.lumoscapstone.lumos.api.User;
import com.lumoscapstone.lumos.databinding.ActivityLoginBinding;
import com.lumoscapstone.lumos.homepage.HomePageActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    // KEYS
    private static final String PREFERENCES_KEY = "com.lumoscapstone.lumos.PREFERENCES_KEY";
    private static final String LOGGED_IN_KEY = "com.lumoscapstone.lumos.LOGGED_IN_KEY";
    private static final String USER_ID_KEY = "com.lumoscapstone.lumos.USER_ID_KEY";


    // User Shared Preferences
    SharedPreferences mSharedPreferences;

    // Binding Variables
    ActivityLoginBinding binding;

    // Edit Texts
    private EditText mEmailField;
    private EditText mPasswordField;

    // User Information
    int mUserId = -1;
    private String mEmail;
    private String mPassword;

    // Set up RetroFit object
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://lumos.benjaminwoodward.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final LumosAPI lumosAPI = retrofit.create(LumosAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Activity Binding
        mEmailField = binding.loginEmailEditText;
        mPasswordField = binding.loginPasswordEditText;

        Button mLoginButton = binding.loginLoginButton;
        Button mRegisterButton = binding.loginRegisterButton;

        // Get user shared preferences
        getPrefs();

        // Check if user is still logged in using sharedPrefs, if so send directly to landing page
        checkLoggedIn();

        // User clicks login button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab user info, only continue if no fields were empty
                if(getUserCredentials()){
                    verifyUserLogin();
                }
            }
        });

        // User clicks register button
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send user to register activity page
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void verifyUserLogin() {
        LoginRequest loginRequest = new LoginRequest(mEmail, mPassword);

        Call<LoginResponse> call = lumosAPI.verifyLogin(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                handleLoginResponse(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleLoginResponse(Response<LoginResponse> response) {
        if (!response.isSuccessful()) {
            // handle the error case
            Log.e("response_error", "Response Code: response.code()");
            loginError(response.code());
            return;
        }

        LoginResponse loginResponse = response.body();
        assert loginResponse != null;
        mUserId = loginResponse.getId();

        // Update that the user is now logged in shared preferences
        updateSharedPreferences();

        // Send user to home page
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(intent);
    }

    private void loginError(int code){
        if(code == 401){
            Toast.makeText(LoginActivity.this, "Invalid credentials. Please try logging in again or register for an account.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "Oops we're having trouble on our end, contact support for more information. Response Code: " + code, Toast.LENGTH_LONG).show();
        }

        // Clear fields so user can try again
        mEmailField.setText("");
        mPasswordField.setText("");
    }

    // Check if user shared prefs to see if user is logged in already
    private void checkLoggedIn(){
        boolean loggedIn = mSharedPreferences.getBoolean(LOGGED_IN_KEY, false);

        if(loggedIn){
            // If user is still logged in, redirect to homepage
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
        }
    }

    // Retrieve's current user's shared preferences, if null set loggedIn to false by default
    private void getPrefs() {
        mSharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    // Grab email and password from user populated fields
    private boolean getUserCredentials(){
        mEmail = mEmailField.getText().toString().trim();
        mPassword = mPasswordField.getText().toString().trim();

        // Check there's no empty fields
        if(mEmail.isEmpty() && mPassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter an email address and password.", Toast.LENGTH_SHORT).show();
            return false;
        } else if(mEmail.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
            return false;
        } else if(mPassword.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // User credentials successfully pulled as non-empty strings
        return true;
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LOGGED_IN_KEY, true);
        editor.putInt(USER_ID_KEY, mUserId);
        editor.apply();
    }
}