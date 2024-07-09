package com.example.blog_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "review_like")
public class ReviewLike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "reviewLikes", "reviewImages"}) // 필요한 필드를 제외
    private Review review;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "idx")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "reviews", "reviewLikes"}) // 필요한 필드를 제외
    private User user;
    
    // 기본 생성자
    public ReviewLike() { }
    
    // 생성자
    public ReviewLike(Review review, User user) {
        this.review = review;
        this.user = user;
    }
    
    // Getter 및 Setter 메서드
    public Long getId() {
     return id;   
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Review getReview() {
        return review;
    }
    
    public void setReview(Review review) {
        this.review = review;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
