package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.User;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UserProfileActivity extends AppCompatActivity {
    private EditText etUserId, etUsername, etEmail;
    private Button btnSave, btnChangePassword;
    private ImageButton btnBack;
    private ProgressBar progressBar;
    private ApiService apiService;
    private String token;
    private String userID;

    private String originalUsername;
    private String originalEmail;

    private String formatToken(String token) {
        if(token.startsWith("token=")) {
            return token;
        }
        return "token=" + token;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initializeViews();
        loadUserData();
        setupClickListeners();
    }

    private void initializeViews() {
        etUserId = findViewById(R.id.etUserId);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        apiService = RetrofitClient.getInstance().getApi();

        // 获取传递的数据
        userID = getIntent().getStringExtra("userID");
        token = getIntent().getStringExtra("token");
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveUserInfo());
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            intent.putExtra("userID", userID);
            intent.putExtra("token", token);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        showProgress(true);
        apiService.getUserInfo(userID,formatToken(token)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showProgress(false);
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    etUserId.setText(user.getId());
                    etUsername.setText(user.getUsername());
                    etEmail.setText(user.getMail());

                    // 保存原始值，用于对比是否修改
                    originalUsername = user.getUsername();
                    originalEmail = user.getMail();
                } else {
                    Toast.makeText(UserProfileActivity.this,
                            "Failed to load user data: " + response.code(), Toast.LENGTH_SHORT).show();
                    if(response.code() == 403) {
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showProgress(false);
                Toast.makeText(UserProfileActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserInfo() {
        String newUsername = etUsername.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();

        // 验证输入
        if (!validateInput(newUsername, newEmail)) {
            return;
        }

        // 检查是否有修改
        boolean hasChanges = false;
        if (!newUsername.equals(originalUsername)) {
            updateUsername(newUsername);
            hasChanges = true;
        }
        if (!newEmail.equals(originalEmail)) {
            updateEmail(newEmail);
            hasChanges = true;
        }

        if (!hasChanges) {
            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUsername(String newUsername) {
        showProgress(true);

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                newUsername  // 直接传递字符串，不需要处理引号
        );

        apiService.updateUsername(userID, formatToken(token), requestBody)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        showProgress(false);
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                            originalUsername = user.getUsername();

                            // 更新 SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                            prefs.edit().putString("username", originalUsername).apply();

                            // 返回结果给 HomeActivity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("username", originalUsername);
                            setResult(RESULT_OK, resultIntent);

                            Toast.makeText(UserProfileActivity.this,
                                    "Username updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UserProfileActivity.this,
                                    "Failed to update username: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        showProgress(false);
                        Toast.makeText(UserProfileActivity.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateEmail(String newEmail) {
        showProgress(true);

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                newEmail  // 直接传递字符串，不需要处理引号
        );

        apiService.updateEmail(userID, formatToken(token), requestBody)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        showProgress(false);
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                            originalEmail = user.getMail();
                            Toast.makeText(UserProfileActivity.this,
                                    "Email updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this,
                                    "Failed to update email: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        showProgress(false);
                        Toast.makeText(UserProfileActivity.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private boolean validateInput(String username, String email) {
        if (username.isEmpty()) {
            etUsername.setError("Username cannot be empty");
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email cannot be empty");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email format");
            return false;
        }

        return true;
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        etUsername.setEnabled(!show);
        etEmail.setEnabled(!show);
        btnSave.setEnabled(!show);
        btnChangePassword.setEnabled(!show);
    }
}