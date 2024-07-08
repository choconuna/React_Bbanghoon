package com.example.blog_back.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; 

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_idx", referencedColumnName = "idx")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "reviews"}) // 무한 재귀 방지
    private User user;

    @Column(name = "category")
    private String category;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;
    
    @Column(name = "like_count") // 좋아요 수
    private int likeCount;
    
    @Column(name = "view_count") // 조회수
    private int viewCount;
    
    @Column(name = "secret") // 공개 범위 설정(TRUE: 회원 공개, FALSE: 전체 공개)
    private boolean secret;
    
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();
    
    public void addReviewImage(ReviewImage reviewImage) {
        reviewImages.add(reviewImage);
        reviewImage.setReview(this);
    }
    
    public void removeReviewImage(ReviewImage reviewImage) {
        reviewImages.remove(reviewImage);
        reviewImage.setReview(null);
    }
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 생성 시간 자동 설정
    @PrePersist // 엔티티가 데이터베이스에 저장되기 전에 실행되는 콜백 메서드
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.createdAt = now;
        this.updatedAt = now;
    }
    
    // 조회수 증가 메서드
    public void increaseViewCount() {
        this.viewCount++;
    }

    // 기본 생성자
    public Review() { 
        this.likeCount = 0;
        this.viewCount = 0;
    }
    
    // 모든 필드를 초기화하는 생성자
    public Review(User user, String category, String title, String content, boolean secret) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.secret = secret;
        this.likeCount = 0;
        this.viewCount = 0;   
        this.reviewImages = new ArrayList<>();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
    
    // Getter 및 Setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getCategory() {
        return category;
    } 
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    
    public int getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    
    public boolean getSecret() {
        return secret;
    }
    
    public void setSecret(boolean secret) {
        this.secret = secret;
    }
    
    public List<ReviewImage> getReviewImages() {
        return reviewImages;
    }
    
    public void setReviewImages(List<ReviewImage> reviewImages) {
        this.reviewImages = reviewImages;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
