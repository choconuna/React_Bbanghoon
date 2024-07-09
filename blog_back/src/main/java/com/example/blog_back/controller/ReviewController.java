package com.example.blog_back.controller;

import com.example.blog_back.entity.Review;
import com.example.blog_back.entity.User;
import com.example.blog_back.service.ReviewService;
import com.example.blog_back.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Map<String, Object> reviewData) {
        Long user_idx = Long.valueOf(reviewData.get("user_idx").toString());
        String category = (String) reviewData.get("category");
        String title = (String) reviewData.get("title");
        String content = (String) reviewData.get("content");
        int secret = Integer.parseInt(reviewData.get("secret").toString());

        User user = userService.findByIdx(user_idx);

        System.out.println("저장할 review: " + user + " " + category + " " + title + " " + content + " " + secret);

        Review review = new Review();
        review.setUser(user);
        review.setCategory(category);
        review.setTitle(title);
        review.setContent(content);
        if (secret == 1)
            review.setSecret(true);
        else
            review.setSecret(false);

        user.addReview(review); // 양방향 연관관계이기 때문에, 사용자의 데이터에 review 데이터 추가

        Review savedReview = reviewService.save(review);
        System.out.println("저장된 review: " + savedReview);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable("category") String category) {
        List<Review> reviews = reviewService.findByCategoryOrderByUpdatedAtDesc(category);

        reviews.forEach(review -> {
            System.out.println("Review ID: " + review.getId());
            System.out.println(
                    "User Nickname: " + (review.getUser() != null ? review.getUser().getUserNickname() : "null"));
        });

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.findAllOrderByUpdatedAtDesc();

        reviews.forEach(review -> {
            System.out.println("Review ID: " + review.getId());
            System.out.println(
                    "User Nickname: " + (review.getUser() != null ? review.getUser().getUserNickname() : "null"));
        });

        System.out.println("Reviews: " + ResponseEntity.ok(reviews));
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/increaseViewCount/{reviewId}")
    public ResponseEntity<Review> increaseViewCount(@PathVariable("reviewId") Long reviewId) {
        Review updatedReview = reviewService.increaseViewCount(reviewId);
        return ResponseEntity.ok(updatedReview);
    }
    
    @PostMapping("/toggleLikeCount")
    public ResponseEntity<Review> increaseLikeCount(@RequestBody Map<String, Object> likeData) {
        Long user_idx = Long.valueOf(likeData.get("user_idx").toString());
        Long review_id = Long.valueOf(likeData.get("review_id").toString());
        int isLiked = Integer.parseInt(likeData.get("isLiked").toString());
        
        boolean isLikedState = true;
        if(isLiked == 1) { // 좋아요를 취소한 경우
            isLikedState = false;
        }
        
        System.out.println("좋아요 카운트 처리 실행");
        System.out.println("좋아요 처리 데이터: " + user_idx + " " + review_id + " " + isLiked + " " + isLikedState);
        Review updatedReview = reviewService.toggleLikeCount(user_idx, review_id, isLikedState);
        
        return ResponseEntity.ok(updatedReview);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable("id") Long id) {
        Optional<Review> optionalReview = reviewService.findById(id);
        if (optionalReview.isPresent()) { // id에 해당하는 review 데이터가 존재할 경우
            Review review = optionalReview.get();
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/update/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable("id") Long id, @RequestBody Map<String, Object> reviewData) {
        try {
            Optional<Review> optionalReview = reviewService.findById(id);
            if(!optionalReview.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Review review = optionalReview.get();
            review.setCategory((String) reviewData.get("category"));
            review.setTitle((String) reviewData.get("title"));
            review.setContent((String) reviewData.get("content"));
            review.setSecret(Integer.parseInt(reviewData.get("secret").toString()) == 1);
            
            review.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            
            Review updatedReview = reviewService.save(review);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
