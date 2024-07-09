package com.example.blog_back.service;

import com.example.blog_back.entity.Review;
import com.example.blog_back.entity.User;
import com.example.blog_back.entity.ReviewLike;
import com.example.blog_back.repository.ReviewRepository;
import com.example.blog_back.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewLikeService reviewLikeService;

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

    public Review toggleLikeCount(Long user_idx, Long review_id, boolean isLikedState) {
        Optional<Review> optionalReview = reviewRepository.findById(review_id);
        User user = userRepository.findByIdx(user_idx); // 현재 좋아요한 사용자 정보를 가져옴

        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            if (isLikedState) { // 좋아요가 선택된 상태인 경우
                review.increaseLikeCount();

                // ReviewLike 엔티티 생성 및 저장
                ReviewLike reviewLike = new ReviewLike();
                reviewLike.setUser(user);
                reviewLike.setReview(review);
                reviewLike = reviewLikeService.save(reviewLike); // ReviewLike 저장

                user.addReviewLike(reviewLike);
                review.addReviewLike(reviewLike);
            } else { // 좋아요가 취소된 상태인 경우
                review.decreaseLikeCount();

                // ReviewLike 엔티티 제거
                Optional<ReviewLike> optionalReviewLike = reviewLikeService.findByUserAndReview(user, review);
                if (optionalReviewLike.isPresent()) {
                    ReviewLike reviewLikeToRemove = optionalReviewLike.get();
                    review.removeReviewLike(reviewLikeToRemove);
                    user.removeReviewLike(reviewLikeToRemove);
                    reviewLikeService.delete(reviewLikeToRemove); // ReviewLike 객체
                }
            }
            return reviewRepository.save(review);
        } else {
            throw new RuntimeException("Review not found width id: " + review_id);
        }
    }
}
