package com.lumoscapstone.lumos.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lumoscapstone.lumos.R;
import com.lumoscapstone.lumos.databinding.ActivityRegisterBinding;
import com.lumoscapstone.lumos.homepage.HomePageActivity;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    private EditText mNameField, mEmailField,  mPasswordField, mPhoneField, mAddressField;

    private String mName, mEmail, mPassword, mPhone, mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mNameField = binding.registerNameEditText;
        mEmailField = binding.registerEmailEditText;
        mPasswordField = binding.registerPasswordEditText;
        mPhoneField = binding.registerPhoneEditText;
        mAddressField = binding.registerAddressEditText;

        Button registerButton = binding.registerRegisterButton;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAllFields()) {
                    Toast.makeText(RegisterActivity.this, "Successfully Registered.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, HomePageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkAllFields(){
        mName = mNameField.getText().toString().trim();
        mEmail = mEmailField.getText().toString().trim();
        mPassword = mPasswordField.getText().toString().trim();
        mPhone = mPhoneField.getText().toString().trim();
        mAddress = mAddressField.getText().toString().trim();

        // Check there's no empty fields
        if(mName.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please enter your name.", Toast.LENGTH_LONG).show();
            return false;
        }else if(mEmail.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please enter your email address.", Toast.LENGTH_LONG).show();
            return false;
        } else if(mPassword.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please enter your password.", Toast.LENGTH_LONG).show();
            return false;
        } else if(mPhone.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please enter your phone number.", Toast.LENGTH_LONG).show();
            return false;
        } else if(mAddress.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please enter your address.", Toast.LENGTH_LONG).show();
            return false;
        }

        // User credentials successfully pulled as non-empty strings
        return true;
    }
}