package com.example.blog_back.handler;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.blog_back.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import jakarta.servlet.ServletException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
        HttpSession session = request.getSession();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        session.setAttribute("userId", userDetails.getUsername());
        session.setAttribute("userNickname", userService.getUserNickname(userDetails.getUsername()));
        session.setAttribute("position", userService.getPosition(userDetails.getUsername()));
        session.setAttribute("profileImageName", userService.getProfileImageName(userDetails.getUsername()));
        
        // 로그인 성공 후 리다이렉트할 경로 설정
        getRedirectStrategy().sendRedirect(request, response, "/");
    }
}
