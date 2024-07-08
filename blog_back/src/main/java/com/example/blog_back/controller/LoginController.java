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

import com.example.blog_back.dto.PasswordVerificationRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user, HttpServletRequest request) {
        String result = userService.loginUser(user.getUserId(), user.getUserPassword());
        if (result.equals("Success")) { // 로그인 성공 시 세션에 사용자 정보 저장
            HttpSession session = request.getSession();

            session.setAttribute("idx", userService.getIdx(user.getUserId()));
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userNickname", userService.getUserNickname(user.getUserId()));
            session.setAttribute("position", userService.getPosition(user.getUserId()));
            session.setAttribute("profileImageName", userService.getProfileImageName(user.getUserId()));

            // 세션 정보 출력
            System.out.println("세션에 저장된 idx: " + session.getAttribute("idx"));
            System.out.println("세션 생성됨, 세션 ID: " + session.getId());
            System.out.println("세션에 저장된 userId: " + session.getAttribute("userId"));
            System.out.println("세션에 저장된 userNickname: " + session.getAttribute("userNickname"));
            System.out.println("세션에 저장된 position: " + session.getAttribute("position"));
            System.out.println("세션에 저장된 profileImageName: " + session.getAttribute("profileImageName"));
        } else {
            // 로그인 실패 시 세션 생성하지 않음
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // 기존 세션 무효화
            }

            if (result.equals("User not found")) {
                return ResponseEntity.status(404).body(result);
            } else if (result.equals("Invalid password")) {
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
        if (session != null) {
            String userId = (String) session.getAttribute("userId");
            String userNickname = (String) session.getAttribute("userNickname");
            String position = (String) session.getAttribute("position");
            String profileImageName = (String) session.getAttribute("profileImageName");
            Long idx = (Long) session.getAttribute("idx");
            System.out.println("세션 정보: " + idx + " " + userId + " " + userNickname + " " + position + " " + profileImageName);
            return ResponseEntity.ok(new UserSession(idx, userId, userNickname, position, profileImageName));
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

    @PostMapping("/verfiyPassword")
    public ResponseEntity<String> verifyPassword(@RequestBody PasswordVerificationRequest requestDto,
            HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            System.out.println("세션 없음");
            return ResponseEntity.status(401).body("User not authenticated");
        }

        String userId = (String) session.getAttribute("userId");
        String userPassword = requestDto.getUserPassword();

        System.out.println("가져온 세션: " + session + ", 사용자 ID: " + userId + ", 입력된 비밀번호: " + userPassword);

        // 서비스 계층에서 사용자의 기존 비밀번호 확인
        boolean isPasswordValid = userService.verifyUserPassword(userId, userPassword);

        if (isPasswordValid) {
            return ResponseEntity.ok("Password verified");
        } else {
            return ResponseEntity.status(401).body("Invalid Password");
        }
    }
}

class UserSession {
    private Long idx;
    private String userId;
    private String userNickname;
    private String position;
    private String profileImageName;

    public UserSession(Long idx, String userId, String userNickname, String position, String profileImageName) {
        this.idx = idx;
        this.userId = userId;
        this.userNickname = userNickname;
        this.position = position;
        this.profileImageName = profileImageName;
    }

    public Long getIdx() {
        return idx;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getPosition() {
        return position;
    }

    public String getProfileImageName() {
        return profileImageName;
    }
}
