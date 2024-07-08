package com.example.blog_back.service;

import com.example.blog_back.entity.Review;
import com.example.blog_back.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
    
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }
    
    public Review save(Review review) {
        return reviewRepository.save(review);
    }
    
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
    
    public List<Review> findByCategory(String category) {
        return reviewRepository.findByCategory(category);
    }
    
    public List<Review> findByCategoryOrderByUpdatedAtDesc(String category) {
        return reviewRepository.findByCategoryOrderByUpdatedAtDesc(category);
    }
    
    public List<Review> findAllOrderByUpdatedAtDesc() {
        return reviewRepository.findAllOrderByUpdatedAtDesc();
    }
    
    public Review increaseViewCount(Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) { // id에 해당하는 review 데이터가 존재할 경우
            Review review = optionalReview.get();
            review.increaseViewCount();
            return reviewRepository.save(review);
        } else {
            throw new RuntimeException("Review not found with id: " + id);
        }
    }
}
