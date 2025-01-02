package com.example.proyectodsa_android;

import com.example.proyectodsa_android.models.InventoryObject;
import com.example.proyectodsa_android.models.LoginRequest;
import com.example.proyectodsa_android.models.StoreObject;
import com.example.proyectodsa_android.models.User;
import com.example.proyectodsa_android.models.PasswordChangeRequest;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("users/login")
    Call<User> login(@Body LoginRequest loginRequest);

    @PUT("users/register")
    Call<User> register(@Body User user);

    @PUT("users/changeUsername/{userID}")
    Call<User> changeUsername(
            @Path("userID") String userID,
            @Header("Cookie") String token,
            @Body RequestBody newUsername
    );

    @PUT("users/changeEmail/{userID}")
    Call<User> changeEmail(
            @Path("userID") String userID,
            @Header("Cookie") String token,
            @Body RequestBody newEmail
    );

    @PUT("users/changePassword/{userID}")
    Call<User> changePassword(
            @Path("userID") String userID,
            @Header("Cookie") String token,
            @Body PasswordChangeRequest passwordChangeRequest
    );


    @GET("users/getObjects/{userID}")
    Call<List<InventoryObject>> getUserObjects(
            @Path("userID") String userID,
            @Header("Cookie") String token
    );

    @GET("users/userinfo/{userID}")
    Call<User> getUserInfo(
            @Path("userID") String userID,
            @Header("Cookie") String token
    );

    @GET("shop/money/{userID}")
    Call<Double> getUserMoney(
            @Path("userID") String userID,
            @Header("Cookie") String token
    );

    @GET("shop/listObjects")
    Call<List<StoreObject>> getStoreItems();


    @POST("shop/buy/{objectID}/{userID}/{quantity}")
    Call<Void> buyObject(
            @Path("objectID") String objectID,
            @Path("userID") String userID,
            @Path("quantity") int quantity,
            @Header("Cookie") String token
    );
}
