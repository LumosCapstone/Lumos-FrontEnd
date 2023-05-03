package com.lumoscapstone.lumos.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lumoscapstone.lumos.R;
import com.lumoscapstone.lumos.api.LoginResponse;
import com.lumoscapstone.lumos.api.LumosAPI;
import com.lumoscapstone.lumos.api.User;
import com.lumoscapstone.lumos.api.UserRegister;
import com.lumoscapstone.lumos.databinding.ActivityRegisterBinding;
import com.lumoscapstone.lumos.homepage.HomePageActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    // User Shared Preferences
    SharedPreferences mSharedPreferences;
    int mUserId = -1;

    ActivityRegisterBinding binding;

    EditText mNameField;
    EditText mEmailField;
    EditText mPasswordField;
    EditText mPhoneNumberField;

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

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            final String nameField = mNameField.toString();
            final String emailField = mEmailField.toString();
            final String passwordField = mPasswordField.toString();
            final String phonenumberField = mPhoneNumberField.toString();

            final UserRegister registerUser = new UserRegister(nameField, emailField, passwordField, phonenumberField);
            @Override
            public void onClick(View view) {
                //call to register user
                Call<User> call = lumosAPI.registerUser(registerUser);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        handleRegisterResponse(response);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                //after user registers, they are sent to homepage
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleRegisterResponse(Response<User> response) {
        if (!response.isSuccessful()) {
            // handle the error case
            Log.e("response_error", "Response Code: response.code()");
            RegisterError(response.code());
            return;
        }

        User userResponse = response.body();
        if(response.body() == null){
            RegisterError(-1);
        }
        assert userResponse != null;
        mUserId = userResponse.getId();

        // Update that the user is now logged in shared preferences
        updateSharedPreferences();

        // Send user to home page
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(intent);
    }
    private void RegisterError(int code){
        if(code == 401){
            Toast.makeText(RegisterActivity.this, "Invalid credentials. Please try logging in again or register for an account.", Toast.LENGTH_SHORT).show();
        } else if(code == -1){
            Toast.makeText(RegisterActivity.this, "Unable to retrieve your user information at this time. Please contact support for more information.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(RegisterActivity.this, "Oops we're having trouble on our end, contact support for more information. Response Code: " + code, Toast.LENGTH_LONG).show();
        }

        // Clear fields so user can try again
        mEmailField.setText("");
        mPasswordField.setText("");
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.apply();
    }

}