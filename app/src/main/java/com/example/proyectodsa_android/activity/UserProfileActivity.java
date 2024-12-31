package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodsa_android.R;

public class UserProfileActivity extends AppCompatActivity {
    private TextView tvUsername, tvUserId, tvUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvUsername = findViewById(R.id.tvUsername);
        tvUserId = findViewById(R.id.tvUserId);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        Button btnBack = findViewById(R.id.btnBack);

        // 获取从 HomeActivity 传递过来的数据
        String username = getIntent().getStringExtra("username");
        String userId = getIntent().getStringExtra("userID");
        String userEmail = getIntent().getStringExtra("userEmail");

        // 设置显示的用户信息
        tvUsername.setText("Username: " + username);
        tvUserId.setText("User ID: " + userId);
        tvUserEmail.setText("Email: " + userEmail);

        btnBack.setOnClickListener(v -> finish());
    }
}