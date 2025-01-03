package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.models.LoginRequest;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etLoginIdentifier, etLoginPassword;
    private Button btnRegister, btnLogin;
    private TextView tvSwitchToLogin, tvSwitchToRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initializeViews();
        apiService = RetrofitClient.getInstance().getApi();
        setupClickListeners();
    }

    private void initializeViews() {
        viewFlipper = findViewById(R.id.viewFlipper);

        // Register views
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvSwitchToLogin = findViewById(R.id.tvSwitchToLogin);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);


        // Login views
        etLoginIdentifier = findViewById(R.id.etLoginIdentifier);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSwitchToRegister = findViewById(R.id.tvSwitchToRegister);
    }

    private void setupClickListeners() {
        tvSwitchToLogin.setOnClickListener(v -> viewFlipper.setDisplayedChild(1));
        tvSwitchToRegister.setOnClickListener(v -> viewFlipper.setDisplayedChild(0));
        btnRegister.setOnClickListener(v -> handleRegister());
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();


        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, password, email);


        Log.d("AuthActivity", "Registering user: " +
                "username=" + user.getUsername() +
                ", email=" + user.getMail() +
                ", password=" + user.getPassword());

        apiService.register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AuthActivity.this,
                            "Registration successful", Toast.LENGTH_SHORT).show();
                    viewFlipper.setDisplayedChild(1);
                    etLoginIdentifier.setText(username);
                    etLoginPassword.setText(password);
                } else {
                    Toast.makeText(AuthActivity.this,
                            "Registration failed: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("AuthActivity", "Registration failed: " + response.code() +
                            " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AuthActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("AuthActivity", "Registration error", t);
            }
        });
    }
    private void handleLogin() {
        String identifier = etLoginIdentifier.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (identifier.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest;
        if (Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
            loginRequest = new LoginRequest(null, identifier, password);
        } else {
            loginRequest = new LoginRequest(identifier, null, password);
        }

        apiService.login(loginRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    String token = response.headers().get("Set-Cookie");
                    handleLoginSuccess(response.body(), token);
                } else {
                    Toast.makeText(AuthActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginSuccess(User user, String rawToken) {

        Log.d("AuthActivity", "Raw token: " + rawToken);

        if (rawToken != null) {
            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
            prefs.edit()
                    .putString("username", user.getUsername())
                    .putString("token", rawToken)  // 保存完整的Cookie字符串
                    .apply();


            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", user.getUsername());
            intent.putExtra("userID", user.getId());
            intent.putExtra("token", rawToken);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Login failed: No token received", Toast.LENGTH_SHORT).show();
        }
    }
}