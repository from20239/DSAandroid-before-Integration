package com.example.proyectodsa_android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.PasswordChangeRequest;
import com.example.proyectodsa_android.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnSavePassword;
    private ImageButton btnBack;
    private ProgressBar progressBar;
    private ApiService apiService;
    private String token, userID;

    private String formatToken(String token) {
        if(token.startsWith("token=")) {
            return token;
        }
        return "token=" + token;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSavePassword = findViewById(R.id.btnSavePassword);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        apiService = RetrofitClient.getInstance().getApi();

        userID = getIntent().getStringExtra("userID");
        token = getIntent().getStringExtra("token");
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSavePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!validateInput(currentPassword, newPassword, confirmPassword)) {
            return;
        }

        showProgress(true);

        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setOldPassword(currentPassword);
        request.setNewPassword(newPassword);

        apiService.changePassword(userID, formatToken(token), request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this,
                            "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (response.code() == 403) {
                        etCurrentPassword.setError("Current password is incorrect");
                        etCurrentPassword.requestFocus();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this,
                                "Failed to change password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showProgress(false);
                Toast.makeText(ChangePasswordActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String currentPassword, String newPassword, String confirmPassword) {
        if (currentPassword.isEmpty()) {
            etCurrentPassword.setError("Please enter current password");
            etCurrentPassword.requestFocus();
            return false;
        }

        if (newPassword.isEmpty()) {
            etNewPassword.setError("Please enter new password");
            etNewPassword.requestFocus();
            return false;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters");
            etNewPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Please confirm new password");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        etCurrentPassword.setEnabled(!show);
        etNewPassword.setEnabled(!show);
        etConfirmPassword.setEnabled(!show);
        btnSavePassword.setEnabled(!show);
    }
}