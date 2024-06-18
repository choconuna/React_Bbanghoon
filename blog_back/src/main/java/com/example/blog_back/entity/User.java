package com.example.blog_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "userId")
    private String userId;

    @Column(name = "userPassword")
    private String userPassword;

    @Column(name = "userName")
    private String userName;

    @Column(name = "userNickname")
    private String userNickname;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "regdate")
    private Date regdate;
    
    @Column(name = "profileImageName")
    private String profileImageName; // 프로필 이미지 파일 이름

    // 기본 생성자
    public User() {}

    // 모든 필드를 초기화하는 생성자
    public User(String userId, String userPassword, String userName, String userNickname, String userEmail, Date regdate) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userEmail = userEmail;
        this.regdate = regdate;
        this.profileImageName = "basic_profile.jpg"; // 기본 프로필 이미지 파일 이름 설정
    }

    // Getter 및 Setter 메서드
    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }
    
    public String getProfileImageName() {
        return profileImageName;
    }

    public void setProfileImageName(String profileImageName) {
        this.profileImageName = profileImageName;
    }
}
