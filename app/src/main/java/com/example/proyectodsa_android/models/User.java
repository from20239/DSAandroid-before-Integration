package com.example.proyectodsa_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose(serialize = false)
    private String id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("mail")
    @Expose
    private String mail;

    @SerializedName("money")
    @Expose(serialize = false)
    private double money;

    @SerializedName("puntos")
    @Expose(serialize = false)
    private int puntos;

    // 默认构造函数
    public User() {}

    // 用于注册的构造函数
    public User(String username, String password, String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
    }


    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {

        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}