package com.example.blog_back.controller;

import com.example.blog_back.entity.User;
import com.example.blog_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping("/check-duplicate-id/{id}")
    public ResponseEntity<Boolean> checkId(@PathVariable String id) {
        System.out.println("아이디 중복 확인 요청: " + id);
        Boolean isDuplicated = userService.isIdDuplicated(id);
        System.out.println("아이디 중복 여부: " + isDuplicated);
        return ResponseEntity.ok(isDuplicated);
    }

    @GetMapping("/check-duplicate-nickname/{nickname}")
    public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname) {
        Boolean isDuplicated = userService.isNicknameDuplicated(nickname);
        return ResponseEntity.ok(isDuplicated);
    }

    @GetMapping("/check-duplicate-email/{email}")
    public ResponseEntity<Boolean> checkEmail(@PathVariable String email) {
        Boolean isDuplicated = userService.isEmailDuplicated(email);
        return ResponseEntity.ok(isDuplicated);
    }
    
    @PostMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestBody UserSearchIdRequest request) {
        String userName = request.getUserName();
        String userEmail = request.getUserEmail();
        
        User user = userService.findUserByNameAndEmail(userName, userEmail);
        
        if(user != null) {
            return ResponseEntity.ok((user.getUserId()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/find-pw") 
    public ResponseEntity<?> findUserPw(@RequestBody UserSearchPwRequest request) {
        String userId = request.getUserId();
        String userEmail = request.getUserEmail();
        
        User user = userService.findUserByIdAndEmail(userId, userEmail);
        
        if(user != null) {
            return ResponseEntity.ok((user.getUserPassword()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserId(@PathVariable String userId) {
        User user = userService.findUserById(userId);
        
        if(user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    static class UserSearchIdRequest {
        private String userName;
        private String userEmail;
        
        public String getUserName() {
            return userName;
        }
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public String getUserEmail() {
            return userEmail;
        }
        
        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }
    }
    
    static class UserSearchPwRequest {
        private String userId;
        private String userEmail;
        
        public String getUserId() {
            return userId;
        }
        
        public void setUserId(String userId) {
            this.userId = userId;
        }
        
        public String getUserEmail() {
            return userEmail;
        }
        
        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }
    }
}
