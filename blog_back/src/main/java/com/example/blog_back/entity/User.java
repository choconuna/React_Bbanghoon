package com.example.blog_back.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user")
@JsonIgnoreProperties({"reviews", "reviewLikes"}) // 리뷰 필드를 직렬화에서 제외
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
    
    @Column(name = "position") 
    private String position; // 관리자인지, 회원인지

    @Column(name = "regdate")
    private Date regdate;

    @Column(name = "profileImageName")
    private String profileImageName; // 프로필 이미지 파일 이름

    public enum ProfileImageName {
        BASIC("basic"),
        CUSTOMIZE("customize");

        private final String value;

        ProfileImageName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    
    // 양방향 관계 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // 양방향 연관 관계에서의 무한 재귀 문제 방지 위함
    private List<Review> reviews = new ArrayList<>();
    
    // 양방향 관계 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // 양방향 연관 관계에서의 무한 재귀 문제 방지 위함
    private List<ReviewLike> reviewLikes = new ArrayList<>();
    
    // 기본 생성자
    public User() { 
        this.position = "member";
        this.profileImageName = "basic";
    }

    // 모든 필드를 초기화하는 생성자
    public User(String userId, String userPassword, String userName, String userNickname, String userEmail, Date regdate) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userEmail = userEmail;
        this.position = "member"; // 회원으로 설정
        this.regdate = regdate;
        this.profileImageName = "basic"; // 기본 프로필 이미지 파일 이름 설정
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
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
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
    
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setUser(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setUser(null);
    }
    
    public List<ReviewLike> getReviewLikes() {
        return reviewLikes;
    }
    
    public void setReviewLikes(List<ReviewLike> reviewLikes) {
        this.reviewLikes = reviewLikes;
    }
    
    public void addReviewLike(ReviewLike reviewLike) {
        reviewLikes.add(reviewLike);
        reviewLike.setUser(this);
    }
    
    public void removeReviewLike(ReviewLike reviewLike) {
        reviewLikes.remove(reviewLike);
        reviewLike.setUser(null);
    }
}
