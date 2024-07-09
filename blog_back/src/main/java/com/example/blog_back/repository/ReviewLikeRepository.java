package com.example.blog_back.repository;

import com.example.blog_back.entity.User;
import com.example.blog_back.entity.Review;
import com.example.blog_back.entity.ReviewLike;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository  extends JpaRepository<ReviewLike, Long>  {
    
    // Review 엔티티의 ID를 기반으로 ReviewLike를 찾는 메서드
    List<ReviewLike> findByReviewId(Long reviewId);
    
    // Review 엔티티와 User 엔티티를 기반으로 ReviewLike를 찾는 메서드
    Optional<ReviewLike> findByUserAndReview(User user, Review review);
} 
