package com.example.blog_back.repository;

import com.example.blog_back.entity.ReviewImage;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    
    // Review 엔티티의 ID를 기반으로 ReviewImage를 찾는 메서드
    List<ReviewImage> findByReviewId(Long reviewId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM ReviewImage ri WHERE ri.id IN :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ReviewImage ri WHERE ri.review.id = :reviewId")
    void deleteAllByReviewId(@Param("reviewId") Long reviewId);
}