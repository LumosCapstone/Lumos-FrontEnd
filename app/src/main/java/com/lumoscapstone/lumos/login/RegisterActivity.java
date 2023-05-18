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
import android.widget.Toast;

import com.lumoscapstone.lumos.R;
import com.lumoscapstone.lumos.api.LumosAPI;
import com.lumoscapstone.lumos.api.UserRegister;
import com.lumoscapstone.lumos.api.UserResponse;
import com.lumoscapstone.lumos.databinding.ActivityRegisterBinding;
import com.lumoscapstone.lumos.homepage.HomePageActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private static final String PREFERENCES_KEY = "com.lumoscapstone.lumos.PREFERENCES_KEY";
    private static final String LOGGED_IN_KEY = "com.lumoscapstone.lumos.LOGGED_IN_KEY";

    // User Shared Preferences
    SharedPreferences mSharedPreferences;
    int mUserId = -1;

    ActivityRegisterBinding binding;

    EditText mNameField;
    EditText mEmailField;
    EditText mPasswordField;
    EditText mPhoneNumberField;

    private String name;
    private String email;
    private String password;
    private String phonenumber;

    Button mRegisterButton;

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://lumos.benjaminwoodward.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final LumosAPI lumosAPI = retrofit.create(LumosAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mNameField = binding.registerNameEditText;
        mEmailField = binding.registerEmailEditText;
        mPasswordField = binding.registerPasswordEditText;
        mPhoneNumberField = binding.registerPhoneEditText;

        mRegisterButton = binding.RegisterButton;
        getPrefs();
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getUserCredentials()) {
                    verifyRegistration();
                }
            }
        });
    }

    private void verifyRegistration() {
        final UserRegister registerUser = new UserRegister(name, email, password, phonenumber);
        Call<UserResponse> call = lumosAPI.registerUser(registerUser);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                handleRegisterResponse(response);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        //after user registers, they are sent to homepage
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(intent);
    }

    private void handleRegisterResponse(Response<UserResponse> response) {
        if (!response.isSuccessful()) {
            // handle the error case
            Log.e("response_error", "Response Code:" + response.code());
            RegisterError(response.code());
            return;
        }

        UserResponse userResponse = response.body();
        if (response.body() == null) {
            RegisterError(-1);
        }
        assert userResponse != null;
        String message = userResponse.getMessage();
        Log.e("Not_error", "Message: " + message);
        Toast.makeText(getApplicationContext(), "Message: " + message, Toast.LENGTH_SHORT).show();
//        mUserId = userResponse.getId();

//        // Update that the user is now logged in shared preferences
        updateSharedPreferences();

        // Send user to home page
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(intent);
    }

    private void RegisterError(int code) {
        if (code == 401) {
            Toast.makeText(RegisterActivity.this, "Invalid credentials. Please try logging in again or register for an account.", Toast.LENGTH_SHORT).show();
        } else if (code == -1) {
            Toast.makeText(RegisterActivity.this, "Unable to retrieve your user information at this time. Please contact support for more information.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(RegisterActivity.this, "Oops we're having trouble on our end, contact support for more information. Response Code: " + code, Toast.LENGTH_LONG).show();
        }

        // Clear fields so user can try again
        mEmailField.setText("");
        mPasswordField.setText("");
    }

    private boolean getUserCredentials() {
        name = mNameField.getText().toString().trim();
        email = mEmailField.getText().toString().trim();
        password = mPasswordField.getText().toString().trim();
        phonenumber = mPhoneNumberField.getText().toString().trim();

        // Check there's no empty fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phonenumber.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Missing field", Toast.LENGTH_SHORT).show();
            return false;

        }
        // User credentials successfully pulled as non-empty strings
        return true;
    }
    private void getPrefs() {
        mSharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
        private void updateSharedPreferences () {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(LOGGED_IN_KEY, true);
            editor.apply();
        }

    }