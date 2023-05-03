package com.lumoscapstone.lumos.profile;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lumoscapstone.lumos.R;
import com.lumoscapstone.lumos.api.LumosAPI;
import com.lumoscapstone.lumos.api.User;
import com.lumoscapstone.lumos.databinding.ActivityProfileBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    SharedPreferences mSharedPreferences;

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

        mNameField = String.valueOf(binding.registerNameEditText);
        mEmailField = String.valueOf(binding.registerEmailEditText);
        mPhoneNumberField = String.valueOf(binding.registerPhoneEditText);

        Call<User> call = lumosAPI.getUser(3);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
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
            Log.e("response_error", "Response Code: response.code()");
            ProfileError(response.code());
            return;
        }

        User userResponse = response.body();
        if(response.body() == null){
            ProfileError(-1);
        }
        assert userResponse != null;

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

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.apply();
    }
}
