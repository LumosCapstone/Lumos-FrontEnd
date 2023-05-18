package com.lumoscapstone.lumos.homepage;

import androidx.appcompat.app.AppCompatActivity;
import com.lumoscapstone.lumos.R;
import com.lumoscapstone.lumos.databinding.ActivityChatBinding;
import com.lumoscapstone.lumos.databinding.ActivityLoginBinding;

import android.os.Bundle;
import android.view.View;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}