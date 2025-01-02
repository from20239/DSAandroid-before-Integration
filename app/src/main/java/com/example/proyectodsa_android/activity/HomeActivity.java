package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodsa_android.R;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_PROFILE = 1;

    private ImageButton btnUserStuff;
    private ImageButton btnStore;
    private Button btnLogout;
    private TextView tvUsername;

    private String userID;
    private String token;
    private String username;
    private String userEmail;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvUsername = findViewById(R.id.tvUsername);
        btnUserStuff = findViewById(R.id.btnUserStuff);
        btnStore = findViewById(R.id.btnstore);
        btnLogout = findViewById(R.id.btnLogout);

        username = getIntent().getStringExtra("username");
        userID = getIntent().getStringExtra("userID");
        token = getIntent().getStringExtra("token");
        userEmail = getIntent().getStringExtra("userEmail");
        userPassword = getIntent().getStringExtra("userPassword");

        tvUsername.setText(username);

        if (token == null || token.isEmpty() || userID == null || userID.isEmpty()) {
            Toast.makeText(this, "Missing authentication data!", Toast.LENGTH_SHORT).show();
            Log.e("HomeActivity", "Token or UserID is missing. Redirecting to login.");
            redirectToLogin();
            return;
        }

        tvUsername.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("userID", userID);
            intent.putExtra("token", token);
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("userPassword", userPassword);
            startActivityForResult(intent, REQUEST_PROFILE);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
            prefs.edit().clear().apply();
            redirectToLogin();
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PROFILE && resultCode == RESULT_OK && data != null) {
            String updatedUsername = data.getStringExtra("newUsername");
            if (updatedUsername != null) {
                tvUsername.setText(updatedUsername);
                username = updatedUsername;
            }
        }
    }
}
