package com.example.blog_back.service;

import com.example.blog_back.entity.User;
import com.example.blog_back.entity.Review;
import com.example.blog_back.repository.ReviewLikeRepository;
import com.example.blog_back.entity.ReviewLike;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewLikeService {

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    public List<ReviewLike> findAll() {
        return reviewLikeRepository.findAll();
    }

    public Optional<ReviewLike> findById(Long id) {
        return reviewLikeRepository.findById(id);
    }
    
    public Optional<ReviewLike> findByUserAndReview(User user, Review review) {
        return reviewLikeRepository.findByUserAndReview(user, review);
    }

    public ReviewLike save(ReviewLike reviewLike) {
        return reviewLikeRepository.save(reviewLike);
    }

    public void delete(ReviewLike reviewLike) {
        reviewLikeRepository.delete(reviewLike);
    }

    public List<ReviewLike> findByReviewId(Long reviewId) {
        return reviewLikeRepository.findByReviewId(reviewId);
    }
}
