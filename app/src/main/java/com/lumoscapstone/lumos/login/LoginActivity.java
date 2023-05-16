package com.lumoscapstone.lumos.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lumoscapstone.lumos.R;
import com.lumoscapstone.lumos.databinding.ActivityLoginBinding;
import com.lumoscapstone.lumos.homepage.HomePageActivity;

public class LoginActivity extends AppCompatActivity {
    // KEYS
    private static final String PREFERENCES_KEY = "com.lumoscapstone.lumos.PREFERENCES_KEY";
    private static final String LOGGED_IN_KEY = "com.lumoscapstone.lumos.LOGGED_IN_KEY";

    // User Shared Preferences
    SharedPreferences mSharedPreferences;

    // Binding Variables
    ActivityLoginBinding binding;

    // Edit Texts
    private EditText mEmailField;
    private EditText mPasswordField;

    // User Information
    private String mEmail;
    private String mPassword;

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

                // 1. Grab user info, only continue if no fields were empty
                if(getUserCredentials()){
                    // temporary variables to simulate valid login credentials of a user stored in our database
                    String validUserEmail = "test";
                    String validUserPassword = "123";

                    // TODO: 2. Create a boolean function to check if user is in our database (once we integrate database)
                    if(mEmail.equals(validUserEmail)){ // eventually replace condition with: private boolean checkForUserInDatabase()

                        // TODO: 3. Validate user entered password matches the one in our database (once we integrate database)
                        if(mPassword.equals(validUserPassword)){  // eventually replace condition with: private boolean validatePassword()
                            // TODO: (?) Possibly update user preferences before going to homepage
                            // populate name, email, password, etc using database values via updateSharedPreferences()
                            // Can use a supplementary function: private void findUserByEmail(mEmail);
                            // It would return an object of type user with the above listed values as attributes

                            // Update that the user is now logged in shared preferences
                            updateSharedPreferences();

                            // Send user to home page
                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                        }

                    } else { // User email wasn't found in database, prompt user to sign up for an account
                        Toast.makeText(LoginActivity.this, "No user with that email found.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "Please either register for an account with us or try logging in again.", Toast.LENGTH_LONG).show();

                        // Resend login activity page so user can try again
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
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
            Toast.makeText(LoginActivity.this, "Please enter an email address and password.", Toast.LENGTH_LONG).show();
            return false;
        } else if(mEmail.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter your email address.", Toast.LENGTH_LONG).show();
            return false;
        } else if(mPassword.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_LONG).show();
            return false;
        }

        // User credentials successfully pulled as non-empty strings
        return true;
    }

    private void updateSharedPreferences(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putBoolean(LOGGED_IN_KEY, true);
        editor.apply();
    }
}