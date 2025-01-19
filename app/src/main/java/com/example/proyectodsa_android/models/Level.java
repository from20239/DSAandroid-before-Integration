package com.example.proyectodsa_android.models;

public class Level {
    private String id;
    private String userId;
    private String levelName;

    // Getters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getLevelName() {
        return levelName;
    }

    // toString for debugging
    @Override
    public String toString() {
        return "Level{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", levelName='" + levelName + '\'' +
                '}';
    }
}
