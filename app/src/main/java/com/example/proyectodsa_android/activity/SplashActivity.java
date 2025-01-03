package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.InventoryObject;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences("auth", MODE_PRIVATE);
        apiService = RetrofitClient.getInstance().getApi();

        // Animación de carga de inicio
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Verificar token después de 1 segundo de retraso
        new Handler().postDelayed(() -> {
            validateToken();
        }, 1000);
    }

    private void validateToken() {
        String username = prefs.getString("username", null);
        String token = prefs.getString("token", null);

        if (username == null || token == null) {
            navigateToAuth();
            return;
        }


        apiService.getUserObjects(username, token).enqueue(new Callback<List<InventoryObject>>() {
            @Override
            public void onResponse(Call<List<InventoryObject>> call, Response<List<InventoryObject>> response) {
                if (response.isSuccessful()) {
                    navigateToHome(username, token);
                } else {
                    Log.d("SplashActivity", "Token validation failed with code: " + response.code());
                    prefs.edit().clear().apply();
                    navigateToAuth();
                }
            }

            @Override
            public void onFailure(Call<List<InventoryObject>> call, Throwable t) {
                Log.e("SplashActivity", "Network error: " + t.getMessage());
                navigateToAuth();
            }
        });
    }

    private void navigateToAuth() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToHome(String username, String token) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("token", token);
        startActivity(intent);
        finish();
    }
}