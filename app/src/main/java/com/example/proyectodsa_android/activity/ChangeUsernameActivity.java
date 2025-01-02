package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.User;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeUsernameActivity extends AppCompatActivity {
    private EditText etNewUsername;
    private Button btnConfirmChangeUsername, btnBackToProfile;

    private String userID;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_username);

        etNewUsername = findViewById(R.id.etNewUsername);
        btnConfirmChangeUsername = findViewById(R.id.btnConfirmChangeUsername);
        btnBackToProfile = findViewById(R.id.btnBackToProfile);

        // 获取 userID 和 token
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        token = intent.getStringExtra("token");

        // 确认修改用户名
        btnConfirmChangeUsername.setOnClickListener(v -> {
            String newUsername = etNewUsername.getText().toString().trim();

            if (!newUsername.isEmpty()) {
                changeUsername(newUsername);
            } else {
                Toast.makeText(this, "Please enter a new username", Toast.LENGTH_SHORT).show();
            }
        });

        // 返回到用户资料页面
        btnBackToProfile.setOnClickListener(v -> finish());
    }

    /**
     * 调用 API 修改用户名
     */
    private void changeUsername(String newUsername) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), newUsername);

        RetrofitClient.getInstance().getApi().changeUsername(userID, token, requestBody).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("ChangeUsername", "Request: userID=" + userID + ", token=" + token + ", newUsername=" + newUsername);

                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Toast.makeText(ChangeUsernameActivity.this, "Username changed successfully!", Toast.LENGTH_SHORT).show();
                    Log.d("ChangeUsername", "Response: New username=" + user.getUsername());

                    /// 将新的用户名返回给调用的活动
                    Intent backIntent = new Intent();
                    backIntent.putExtra("newUsername", user.getUsername());
                    setResult(RESULT_OK, backIntent);
                    finish();
                } else {
                    Log.e("ChangeUsername", "Error response: " + response.code());
                    Toast.makeText(ChangeUsernameActivity.this, "Failed to change username. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ChangeUsername", "Request failed: " + t.getMessage());
                Toast.makeText(ChangeUsernameActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
