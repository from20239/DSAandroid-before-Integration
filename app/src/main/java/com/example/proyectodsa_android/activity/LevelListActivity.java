package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.activity.HomeActivity;
import com.example.proyectodsa_android.adapters.LevelAdapter;
import com.example.proyectodsa_android.models.Level;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LevelListActivity extends AppCompatActivity {

    private static final String TAG = "LevelListActivity";
    private RecyclerView recyclerView;
    private LevelAdapter levelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        recyclerView = findViewById(R.id.recyclerViewLevels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnBack = findViewById(R.id.btnBack);

        fetchLevels();

        btnBack.setOnClickListener(v -> finish());
    }

    private void fetchLevels() {
        ApiService apiService = RetrofitClient.getInstance().getApi();
        String userId = "f1bfaf81-d4f8-11ef-97e0-04d4c47abfd9"; // Replace with dynamic userId if needed

        apiService.getLevelsByUserId(userId).enqueue(new Callback<List<Level>>() {
            @Override
            public void onResponse(Call<List<Level>> call, Response<List<Level>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Level> levelList = response.body();
                    levelAdapter = new LevelAdapter(LevelListActivity.this, levelList);
                    recyclerView.setAdapter(levelAdapter);
                } else {
                    Log.e(TAG, "Failed to fetch levels: " + response.code());
                    Toast.makeText(LevelListActivity.this, "Failed to load levels", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Level>> call, Throwable t) {
                Log.e(TAG, "Error fetching levels", t);
                Toast.makeText(LevelListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
