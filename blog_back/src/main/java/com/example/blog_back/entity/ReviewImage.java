package com.example.blog_back.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore; 

@Entity
@Table(name = "review_image")
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "review_id")
    @JsonIgnore // 양방향 연관 관계에서이 무한 재귀 문제 방지 위함
    private Review review;

    @Column(name = "image_url")
    private String imageUrl;

    // 기본 생성자
    public ReviewImage() {  }
    
    // 모든 필드를 초기화하는 생성자
    public ReviewImage(Review review, String imageUrl) {
        this.review = review;
        this.imageUrl = imageUrl;
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
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}