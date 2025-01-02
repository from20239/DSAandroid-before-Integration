package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CHANGE_USERNAME = 1;
    private static final int REQUEST_CHANGE_EMAIL = 2;
    private static final int REQUEST_CHANGE_PASSWORD = 3;

    private TextView tvUsername, tvUserId, tvUserEmail, tvUserPassword;
    private Button btnChangeUsername, btnChangeEmail, btnChangePassword, btnBack;

    private String userID;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvUsername = findViewById(R.id.tvUsername);
        tvUserId = findViewById(R.id.tvUserId);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPassword = findViewById(R.id.tvUserPassword);

        btnChangeUsername = findViewById(R.id.btnChangeUsername);
        btnChangeEmail = findViewById(R.id.btnChangeEmail);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        token = intent.getStringExtra("token");

        refreshUserInfo();

        btnChangeUsername.setOnClickListener(v -> {
            Intent changeUsernameIntent = new Intent(this, ChangeUsernameActivity.class);
            changeUsernameIntent.putExtra("userID", userID);
            changeUsernameIntent.putExtra("token", token);
            startActivityForResult(changeUsernameIntent, REQUEST_CHANGE_USERNAME);
        });

        // 跳转到 ChangeEmailActivity
        btnChangeEmail.setOnClickListener(v -> {
            Intent changeEmailIntent = new Intent(this, ChangeEmailActivity.class);
            changeEmailIntent.putExtra("userID", userID);
            changeEmailIntent.putExtra("token", token);
            startActivityForResult(changeEmailIntent, REQUEST_CHANGE_EMAIL);
        });

        // 跳转到 ChangePasswordActivity
        btnChangePassword.setOnClickListener(v -> {
            Intent changePasswordIntent = new Intent(this, ChangePasswordActivity.class);
            changePasswordIntent.putExtra("userID", userID);
            changePasswordIntent.putExtra("token", token);
            startActivityForResult(changePasswordIntent, REQUEST_CHANGE_PASSWORD);
        });

        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent();
            backIntent.putExtra("newUsername", tvUsername.getText().toString().replace("Username: ", ""));
            setResult(RESULT_OK, backIntent);
            finish();
        });
    }

    private void refreshUserInfo() {
        RetrofitClient.getInstance().getApi().getUserInfo(userID, token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    tvUsername.setText("Username: " + user.getUsername());
                    tvUserId.setText("User ID: " + user.getId());
                    tvUserEmail.setText("Email: " + user.getMail());
                    tvUserPassword.setText("Password: *****");
                    Log.d("UserProfile", "User info refreshed successfully: " + user.toString());
                } else {
                    Log.e("UserProfile", "Failed to fetch user info. Response code: " + response.code());
                    Toast.makeText(UserProfileActivity.this, "Failed to fetch user info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserProfile", "Error fetching user info: " + t.getMessage());
                Toast.makeText(UserProfileActivity.this, "Error fetching user info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CHANGE_USERNAME:
                    String newUsername = data.getStringExtra("newUsername");
                    if (newUsername != null) {
                        tvUsername.setText("Username: " + newUsername);
                    }
                    break;

                case REQUEST_CHANGE_EMAIL:
                    String newEmail = data.getStringExtra("newEmail");
                    if (newEmail != null) {
                        tvUserEmail.setText("Email: " + newEmail);
                    }
                    break;

                case REQUEST_CHANGE_PASSWORD:
                    Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
