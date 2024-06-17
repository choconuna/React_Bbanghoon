package com.example.blog_back.repository;

import com.example.blog_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByUserNickname(String userNickname);
    boolean existsByUserEmail(String userEmail);
    
    User findByUserId(String userId);
    User findByUserNameAndUserEmail(String userName, String userEmail);
    User findByUserIdAndUserEmail(String userId, String userEmail);
}
