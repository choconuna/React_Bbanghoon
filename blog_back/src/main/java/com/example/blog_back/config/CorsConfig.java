package com.example.blog_back.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@ComponentScan("com.example.blog_back.controller")
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration(); // CORS 설정 관리
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // "http://localhost:3000"으로부터의 요청을 허용하도록 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용된 HTTP 메서드
        configuration.setAllowCredentials(true); // 요청에 대한 인증 정보를 서버에 전달할 수 있음
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // Authorization, Cache-Control, Content-Type 헤더가 허용되도록 설정
         
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // URL 경로별로 CORS 설정을 관리하는 데 사용되는 객체
        source.registerCorsConfiguration("/**", configuration); // 모든 요청에 대해 CORS 정책이 적용됨
        
        return new CorsFilter(source);
    }
}
