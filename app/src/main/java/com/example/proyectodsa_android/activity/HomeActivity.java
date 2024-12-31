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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ImageButton btnUserStuff;
    private ImageButton btnStore;
    private Button btnLogout;
    private TextView tvUsername;
    private String userID;
    private String token;
    private String username;
    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvUsername = findViewById(R.id.tvUsername);
        btnUserStuff = findViewById(R.id.btnUserStuff);
        btnStore = findViewById(R.id.btnstore);
        btnLogout = findViewById(R.id.btnLogout);

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
            intent.putExtra("userEmail", userEmail); // 传递用户邮箱
            startActivity(intent);
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

    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}