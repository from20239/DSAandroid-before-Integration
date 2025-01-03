package com.example.proyectodsa_android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.RetrofitClient;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerGameActivity;

import java.io.IOException;

import retrofit2.Response;

public class UnityWrapperActivity extends UnityPlayerGameActivity {
    static ApiService apiService;
    static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getIntent().getStringExtra("cookie");
        apiService = RetrofitClient.getInstance().getApi();
    }

    public static void sendNewLevel(String json){
        if(token == null || token.isEmpty()){
            sendToast("Unexpected internal error");
            return;
        }
        try{
            Response<Void> r = apiService.uploadLevel(token,"application/json", json).execute();
            if(!r.isSuccessful()){
                sendToast("Unexpected internal error");
            }else{
                closeActivity();
            }
        }catch(IOException ignored){
            sendToast("Unexpected internal error");
        }
    }

    public static void closeActivity(){
        UnityPlayer.currentActivity.finish();
    }

    public static void sendToast(String text){
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast toast = Toast.makeText(UnityPlayer.currentActivity, text, Toast.LENGTH_SHORT);
            toast.show();
        });
    }
}
