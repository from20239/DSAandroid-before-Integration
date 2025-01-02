package com.example.proyectodsa_android.models;

public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;

    // 空构造函数
    public PasswordChangeRequest() {
    }

    // 带参数的构造函数
    public PasswordChangeRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    // Getter 和 Setter
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
