package com.example.proyectodsa_android;

import com.example.proyectodsa_android.models.InventoryObject;
import com.example.proyectodsa_android.models.Level;
import com.example.proyectodsa_android.models.LoginRequest;
import com.example.proyectodsa_android.models.PasswordChangeRequest;
import com.example.proyectodsa_android.models.StoreObject;
import com.example.proyectodsa_android.models.User;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("users/login")
    Call<User> login(@Body LoginRequest loginRequest);

    @PUT("users/register")
    Call<User> register(@Body User user);


    @GET("users/getObjects/{userID}")
    Call<List<InventoryObject>> getUserObjects(
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
    @GET("users/puntos/{userID}")
    Call<Integer> getPuntos(
            @Path("userID") String userID,
            @Header("Cookie") String token
    );
    @GET("users/userinfo/{userID}")
    Call<User> getUserInfo(
            @Path("userID") String userID,
            @Header("Cookie") String token
    );



    @PUT("users/changeUsername/{userID}")
        Call<User> updateUsername(
                @Path("userID") String userID,
                @Header("Cookie") String token,
                @Body RequestBody username
        );

    @PUT("users/changeEmail/{userID}")
        Call<User> updateEmail(
                @Path("userID") String userID,
                @Header("Cookie") String token,
                @Body RequestBody email
        );


    @PUT("users/changePassword/{userID}")
    Call<User> changePassword(
            @Path("userID") String userID,
            @Header("Cookie") String token,
            @Body PasswordChangeRequest request
    );

    @POST("levels/uploadLevel")
    Call<Void> uploadLevel(
            @Header("Cookie") String token,
            @Header("Content-Type") String contentType,
            @Body String levelJson
    );

    @GET("levels/user/{userId}")
    Call<List<Level>> getLevelsByUserId(
            @Path("userId") String userId
    );
}
