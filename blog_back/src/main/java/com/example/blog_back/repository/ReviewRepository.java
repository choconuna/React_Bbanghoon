package com.example.blog_back.repository;

import com.example.blog_back.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findById(Long id);
    
    List<Review> findByCategory(String category);
    
    List<Review> findByCategoryOrderByUpdatedAtDesc(String category);
    
    @Query("SELECT r FROM Review r ORDER BY r.updatedAt DESC")
    List<Review> findAllOrderByUpdatedAtDesc(); 
}