package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.models.InventoryObject;
import com.example.proyectodsa_android.ItemAdapter;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.StoreAdapter;
import com.example.proyectodsa_android.models.StoreObject;
import com.unity3d.player.UnityPlayerGameActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ApiService apiService;
    private ImageButton btnUserStuff, btnStore,btnPlay;
    private TextView tvPuntos;
    private Button btnLogout;
    private TextView tvUsername;
    private String userID;
    private String token;
    private String username;
    private String userEmail;
    private static final int PROFILE_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvUsername = findViewById(R.id.tvUsername);
        tvPuntos = findViewById(R.id.tvPuntos);
        btnUserStuff = findViewById(R.id.btnUserStuff);
        btnStore = findViewById(R.id.btnstore);
        btnLogout = findViewById(R.id.btnLogout);
        btnPlay = findViewById(R.id.btnPlay);

        String username = getIntent().getStringExtra("username");
        userID = getIntent().getStringExtra("userID");
        String token = getIntent().getStringExtra("token");
        userEmail = getIntent().getStringExtra("userEmail");
        tvUsername.setText(username);

        // 验证 token
        if (token == null || token.isEmpty() || userID == null || userID.isEmpty()) {
            Toast.makeText(this, "Missing authentication data!", Toast.LENGTH_SHORT).show();
            Log.e("HomeActivity", "Token or UserID is missing. Redirecting to login.");
            redirectToLogin();
            return;
        }

        Log.d("HomeActivity", "Token received: " + token);
        Log.d("HomeActivity", "UserID received: " + userID);

        // 点击用户名，跳转到 UserProfileActivity 并传递用户信息
        tvUsername.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("userID", userID);
            intent.putExtra("token", token);
            intent.putExtra("userEmail", userEmail);
            startActivityForResult(intent, PROFILE_REQUEST_CODE);  // 使用 startActivityForResult
        });



        // 设置按钮点击事件，传递所有必要的数据
        btnStore.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoreActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("userID", userID);
            intent.putExtra("token", token);
            startActivity(intent);
        });

        btnUserStuff.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserStuffActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("userID", userID);
            intent.putExtra("token", token);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
            prefs.edit().clear().apply();
            redirectToLogin();
        });
        apiService = RetrofitClient.getInstance().getApi();

        btnPlay.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("com.example.proyectodsa_android.v2.playerprefs", MODE_PRIVATE);
            prefs.edit().putString("sceneToLoad", "LevelEditorScene").apply();
            prefs.edit().putString("userId", userID).apply();
            prefs.edit().putString("playerName", username).apply();
            prefs.edit().putString("cookie", token).apply();

           Intent intent = new Intent(this, UnityPlayerGameActivity.class);
           startActivity(intent);
        });

        loadUserPuntos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 更新UI显示的用户名
            String newUsername = data.getStringExtra("username");
            if (newUsername != null) {
                username = newUsername;
                tvUsername.setText(newUsername);
            }
        }
    }
    private void redirectToLogin() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadUserPuntos() {
        Call<Integer> call = apiService.getPuntos(userID, "token=" + token);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int puntos = response.body();
                    tvPuntos.setText("Points: " + puntos);
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load points", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error loading points", Toast.LENGTH_SHORT).show();
                Log.e("HomeActivity", "Error loading points", t);
            }
        });
    }

}