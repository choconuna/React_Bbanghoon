package com.example.blog_back.service;

import com.example.blog_back.entity.ReviewImage;
import com.example.blog_back.repository.ReviewImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewImageService {
    @Autowired
    private ReviewImageRepository reviewImageRepository;
    
    public List<ReviewImage> findAll() {
        return reviewImageRepository.findAll();
    }
    
    public Optional<ReviewImage> findById(Long id) {
        return reviewImageRepository.findById(id);
    }
    
    public ReviewImage save(ReviewImage reviewImage) {
        return reviewImageRepository.save(reviewImage);
    }
    
    public void deleteById(Long id) {
        reviewImageRepository.deleteById(id);
    }
    
    public List<ReviewImage> findByReviewId(Long reviewId) {
        return reviewImageRepository.findByReviewId(reviewId);
    }
    
    public void deleteReviewImages(List<Long> imageIds) {
        reviewImageRepository.deleteAllByIds(imageIds);
    }
}
