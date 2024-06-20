package com.example.blog_back.dto;

public class PasswordVerificationRequest {
    private String userId;
    private String userPassword;

    // 기본 생성자
    public PasswordVerificationRequest() {}

    // 매개변수가 있는 생성자
    public PasswordVerificationRequest(String userId, String userPassword) {
        this.userId = userId;
        this.userPassword = userPassword;
    }

    // Getter와 Setter 메소드
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}

