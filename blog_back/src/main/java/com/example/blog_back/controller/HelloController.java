package com.example.blog_back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    
    @GetMapping
    @ResponseBody
    public String home() {
        return "Bakery Web's Spring Boot!";
    }
    
}
