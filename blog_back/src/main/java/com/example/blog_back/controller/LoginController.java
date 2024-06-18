package com.example.blog_back.controller;

import com.example.blog_back.entity.User;
import com.example.blog_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LoginController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user, HttpServletRequest request) {
        String result = userService.loginUser(user.getUserId(), user.getUserPassword());
        if(result.equals("Success")) { // 로그인 성공 시 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userNickname", userService.getUserNickname(user.getUserId()));
            session.setAttribute("profileImageName", userService.getProfileImageName(user.getUserId()));
        } else {
            // 로그인 실패 시 세션 생성하지 않음
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // 기존 세션 무효화
            }
            
            if(result.equals("User not found")) {
                return ResponseEntity.status(404).body(result);
            } else if(result.equals("Invalid password")) {
                return ResponseEntity.status(401).body(result);
            } else {
                return ResponseEntity.status(500).body(result);
            }
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/session")
    public ResponseEntity<?> getSessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            String userId = (String) session.getAttribute("userId");
            String userNickname = (String)session.getAttribute("userNickname");
            String profileImageName = (String)session.getAttribute("profileImageName");
            System.out.println("세션 정보: " + userId + " " + userNickname + " " + profileImageName);
            return ResponseEntity.ok(new UserSession(userId, userNickname, profileImageName));
        } else {
            return ResponseEntity.status(400).body("No active session");
        }
    }
    
    
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션을 가져오고, 없으면 null 반환
        System.out.println("가져온 세션: " + session);

        if (session != null) {
            session.invalidate(); // 세션 무효화
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(400).body("No active session"); // 세션이 없을 경우
        }
    }
    
    @GetMapping("/login")
    public String handleLoginRedirect() {
        return "redirect:http://localhost:3000"; // 로그인 후 리다이렉트할 경로
    }
    
    @GetMapping("/login/logout") 
    public String handleLogoutRedirect() {
        return "redirect:http://localhost:3000"; // 로그아웃 후 리다이렉트할 경로
    }
}

class UserSession {
    private String userId;
    private String userNickname;
    private String profileImageName;
    
    public UserSession(String userId, String userNickname, String profileImageName) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.profileImageName = profileImageName;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getUserNickname() {
        return userNickname;
    }
    
    public String getProfileImageName() {
        return profileImageName;
    }
}
