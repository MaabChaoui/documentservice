// src/main/java/com/example/document/controller/AdminController.java
package com.example.document.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.document.dto.AdminAnalyticsDto;
import com.example.document.security.UserContext;
import com.example.document.service.AdminAnalyticsService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminAnalyticsService analyticsService;

    public AdminController(AdminAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/analytics")
    public AdminAnalyticsDto analytics() {
        // guard for non-admins
        if (!"ADMIN".equals(UserContext.get().getRole())) {
            throw new RuntimeException("Forbidden");
        }
        return analyticsService.gather();
    }
}
