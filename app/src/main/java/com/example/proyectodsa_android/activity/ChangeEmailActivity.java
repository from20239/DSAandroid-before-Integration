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

public class ChangeEmailActivity extends AppCompatActivity {
    private EditText etNewEmail;
    private Button btnConfirmChangeEmail, btnBackToProfileEmail;

    private String userID;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_email);

        etNewEmail = findViewById(R.id.etNewEmail);
        btnConfirmChangeEmail = findViewById(R.id.btnConfirmChangeEmail);
        btnBackToProfileEmail = findViewById(R.id.btnBackToProfileEmail);

        // 获取 userID 和 token
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        token = intent.getStringExtra("token");

        btnConfirmChangeEmail.setOnClickListener(v -> {
            String newEmail = etNewEmail.getText().toString();
            if (newEmail.isEmpty()) {
                Toast.makeText(this, "Please enter a new email", Toast.LENGTH_SHORT).show();
                return;
            }

            changeEmail(newEmail);
        });

        btnBackToProfileEmail.setOnClickListener(v -> finish());
    }

    private void changeEmail(String newEmail) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("text/plain; charset=utf-8"),  // 修改为 text/plain
                newEmail
        );

        RetrofitClient.getInstance().getApi().changeEmail(userID, token, requestBody).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangeEmailActivity.this, "Email changed successfully!", Toast.LENGTH_SHORT).show();
                    Intent backIntent = new Intent();
                    backIntent.putExtra("newEmail", newEmail);
                    setResult(RESULT_OK, backIntent);
                    finish();
                } else {
                    Log.e("ChangeEmail", "Error response: " + response.code());
                    Toast.makeText(ChangeEmailActivity.this, "Failed to change email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ChangeEmail", "Request failed: " + t.getMessage());
                Toast.makeText(ChangeEmailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
