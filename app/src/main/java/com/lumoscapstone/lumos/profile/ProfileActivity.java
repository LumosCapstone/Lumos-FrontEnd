package com.lumoscapstone.lumos.profile;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lumoscapstone.lumos.R;
import com.lumoscapstone.lumos.api.LumosAPI;
import com.lumoscapstone.lumos.api.ProfileResponse;
import com.lumoscapstone.lumos.api.User;
import com.lumoscapstone.lumos.api.UserRegister;
import com.lumoscapstone.lumos.databinding.ActivityProfileBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    private static final String PREFERENCES_KEY = "com.lumoscapstone.lumos.PREFERENCES_KEY";
    private static final String LOGGED_IN_KEY = "com.lumoscapstone.lumos.LOGGED_IN_KEY";

    ActivityProfileBinding binding;

    SharedPreferences mSharedPreferences;

    int mUserid;
    String mNameField;
    String mEmailField;
    String mPhoneNumberField;

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://lumos.benjaminwoodward.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final LumosAPI lumosAPI = retrofit.create(LumosAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getPrefs();

        mNameField = String.valueOf(binding.profileNameEditText);
        mEmailField = String.valueOf(binding.profileEmailEditText);
        mPhoneNumberField = String.valueOf(binding.profilePhoneEditText);


        Call<User> call = lumosAPI.getUser(mUserid);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                final User user = new User(mUserid,mNameField, mEmailField, mPhoneNumberField);
                handleProfileResponse(response);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void handleProfileResponse(Response<User> response) {
        if (!response.isSuccessful()) {
            // handle the error case
            Log.e("response_error", "Response Code: " + response.code());
            ProfileError(response.code());
            return;
        }

        User profileResponse = response.body();
        if(response.body() == null){
            ProfileError(-1);
        }
        assert profileResponse != null;
        String message = profileResponse.getText();
        Log.e("Not_error", "Message: " + message);
        Toast.makeText(getApplicationContext(), "Message: " + message, Toast.LENGTH_SHORT).show();

        // Update that the user is now logged in shared preferences
        updateSharedPreferences();
    }

    private void ProfileError(int code){
        if(code == 401){
            Toast.makeText(ProfileActivity.this, "Invalid credentials. Please try logging in again or register for an account.", Toast.LENGTH_SHORT).show();
        } else if(code == -1){
            Toast.makeText(ProfileActivity.this, "Unable to retrieve your user information at this time. Please contact support for more information.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ProfileActivity.this, "Oops we're having trouble on our end, contact support for more information. Response Code: " + code, Toast.LENGTH_LONG).show();
        }
    }

    private void getPrefs() {
        mSharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.apply();
    }
}
