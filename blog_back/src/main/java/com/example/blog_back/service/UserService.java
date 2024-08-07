package com.example.blog_back.service;

import com.example.blog_back.entity.User;
import com.example.blog_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public boolean isIdDuplicated(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public boolean isNicknameDuplicated(String userNickname) {
        return userRepository.existsByUserNickname(userNickname);
    }

    public boolean isEmailDuplicated(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }
    
    public String loginUser(String userId, String password) {
        User user = userRepository.findByUserId(userId);
        
        if (user == null) {
            return "User not found";
        } 
        if(!user.getUserPassword().equals(password)) {
            return "Invalid password";
        }
        return "Success";
    }    
    
    public String getUserNickname(String userId) {
        User user = userRepository.findByUserId(userId);
        return user != null ? user.getUserNickname() : null;
    }
    
    public String getProfileImageName(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            return user.getProfileImageName();
        } else {
            throw new RuntimeException("User not found");
        }
    }   
    
    public String getPosition(String userId) {
        User user = userRepository.findByUserId(userId);
        return user != null ? user.getPosition() : null;
    }
    
    public Long getIdx(String userId) {
        User user = userRepository.findByUserId(userId);
        return user != null ? user.getIdx() : null;
    }
    
    public User findByIdx(Long idx) {
        User user = userRepository.findByIdx(idx);
        
        return user;
    }
    
    public User findUserById(String userId) {
        User user = userRepository.findByUserId(userId);
        
        return user;
    }
    
    public User updateUser(String userId, User updateUser) {
        User existingUser = userRepository.findByUserId(userId);
        if(existingUser != null) {
            existingUser.setUserPassword(updateUser.getUserPassword());
            existingUser.setUserName(updateUser.getUserName());
            existingUser.setUserNickname(updateUser.getUserNickname());
            existingUser.setUserEmail(updateUser.getUserEmail());
            existingUser.setPosition(updateUser.getPosition());
            existingUser.setRegdate(updateUser.getRegdate());
            existingUser.setProfileImageName(updateUser.getProfileImageName());
            return userRepository.save(existingUser);
        }
        return null;
    }
    
    // 사용자 인증 로직
    public boolean authenticateUser(String userId, String userPassword) {
        User user = userRepository.findByUserId(userId);
        if(user != null && user.getUserPassword().equals(userPassword)) {
            return true;
        }
        return false;
    }
    
    // 사용자 기존 비밀번호 확인 로직
    public boolean verifyUserPassword(String userId, String userPassword) {
        User user = userRepository.findByUserId(userId);
        if (user != null && user.getUserPassword().equals(userPassword)) {
            return true;
        }
        return false;
    }
    
    public User findUserByNameAndEmail(String userName, String userEmail) {
        return userRepository.findByUserNameAndUserEmail(userName, userEmail);
    }
    
    public User findUserByIdAndEmail(String userId, String userEmail) {
        return userRepository.findByUserIdAndUserEmail(userId, userEmail);
    }
}
