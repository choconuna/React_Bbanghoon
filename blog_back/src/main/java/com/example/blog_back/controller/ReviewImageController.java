package com.example.blog_back.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.blog_back.entity.Review;
import com.example.blog_back.entity.ReviewImage;
import com.example.blog_back.service.FirebaseStorageService;
import com.example.blog_back.service.ReviewImageService;
import com.example.blog_back.service.ReviewService;

import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/reviewImages")
public class ReviewImageController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private ReviewImageService reviewImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadReviewImage(@RequestParam("file") MultipartFile file,
            @RequestParam("reviewId") Long reviewId, @RequestParam("index") Integer index) throws IOException {
        try {
            // 파일 이름 설정
            String fileName = "reviewImage/" + reviewId + "/" + reviewId + "_" + index + ".png";
            System.out.println("upload file name: " + fileName);

            // Firebase Storage에 이미지 업로드
            String imageUrl = firebaseStorageService.uploadFirebaseBucket(file, fileName);

            System.out.println("업로드된 이미지 URL: " + imageUrl);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            System.out.println("이미지 업로드 실패!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    @PostMapping
    public ResponseEntity<ReviewImage> createReviewImage(@RequestParam("review_id") Long review_id,
            @RequestParam("image_url") String image_url) throws IOException {

        System.out.println("저장할 review_image: " + review_id + "\n" + image_url);

        Optional<Review> optionalReview = reviewService.findById(review_id);
        if (optionalReview.isPresent()) { // review_id에 해당하는 review 데이터가 존재할 경우
            Review review = optionalReview.get();

            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setReview(review); // review 데이터에 reviewImage 데이터 추가
            reviewImage.setImageUrl(image_url);

            ReviewImage savedReviewImage = reviewImageService.save(reviewImage);
            return ResponseEntity.ok(savedReviewImage);
        } else {
            System.out.println("이미지 저장 실패!");
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @DeleteMapping("/images/{reviewId}")
    public ResponseEntity<Void> deleteReviewImages(@PathVariable("reviewId") Long reviewId) {
        try {
            Optional<Review> optionalReview = reviewService.findById(reviewId);
            if (!optionalReview.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Review review = optionalReview.get();
            List<ReviewImage> reviewImages = new ArrayList<>(review.getReviewImages());
            System.out.println("삭제될 리뷰 이미지 개수: " + reviewImages.size());

            int index = 0;
            for (ReviewImage reviewImage : reviewImages) {
                String imageUrl = reviewImage.getImageUrl(); // https://storage.googleapis.com/... 형식의 URL
                String bucketPath = imageUrl.substring(imageUrl.indexOf("/v1/b/") + 6); // bucket 경로 추출
                String bucketName = bucketPath.substring(0, bucketPath.indexOf("/")); // bucket 이름 추출
                String objectPath = "reviewImage/" + reviewImage.getReview().getId() + "/" + reviewImage.getReview().getId() + "_" + index + ".png";
                String gsUrl = "gs://" + bucketName + "/" + objectPath; // gs:// 형식의 경로
                System.out.println("삭제할 이미지 URL: " + gsUrl);
                index++;
                
                // 리뷰 이미지 데이터 제거
                reviewImageService.deleteById(reviewImage.getId());

                try {
                    // FirebaseStorageService를 사용하여 Blob 삭제
                    firebaseStorageService.deleteFirebaseBucket(objectPath);
                    System.out.println("Firebase Storage에서 이미지 삭제 성공: " + gsUrl);
                } catch (Exception e) {
                    System.out.println("리뷰 이미지 삭제 실패: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("삭제된 자식 리뷰 이미지 데이터: " + reviewImageService.findByReviewId(reviewId));

            // 리뷰 데이터에서 이미지 목록 제거 및 저장
            review.getReviewImages().clear();
            reviewService.save(review);
            System.out.println("삭제된 부모 리뷰 이미지 데이터: " + review.getReviewImages());

            System.out.println("리뷰 이미지 삭제 완료!");

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("리뷰 이미지 삭제 실패: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
