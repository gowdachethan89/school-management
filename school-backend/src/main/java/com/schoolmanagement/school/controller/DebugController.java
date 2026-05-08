package com.schoolmanagement.school.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:3000",
        "https://school-management-one-beryl.vercel.app"
}, allowCredentials = "true")
public class DebugController {

    @GetMapping("/auth-status")
    public Map<String, Object> getAuthStatus(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        response.put("authenticated", auth != null && auth.isAuthenticated());
        response.put("principal", auth != null ? auth.getName() : "null");
        response.put("authorities", auth != null ? auth.getAuthorities().toString() : "null");
        
        // Debug info
        String sessionId = request.getSession(false) != null ? request.getSession(false).getId() : "no-session";
        String headerSessionId = request.getHeader("X-JSESSIONID");
        Cookie[] cookies = request.getCookies();
        String jsessionidCookie = "not-found";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    jsessionidCookie = cookie.getValue();
                    break;
                }
            }
        }
        
        response.put("currentSessionId", sessionId);
        response.put("headerSessionId", headerSessionId);
        response.put("jsessionidCookie", jsessionidCookie);
        
        System.out.println("=== /api/debug/auth-status ===");
        System.out.println("Auth: " + (auth != null ? auth.getName() : "null"));
        System.out.println("Authenticated: " + (auth != null && auth.isAuthenticated()));
        System.out.println("Current Session ID: " + sessionId);
        System.out.println("Header Session ID: " + headerSessionId);
        System.out.println("Cookie Session ID: " + jsessionidCookie);
        System.out.println("All Cookies:");
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("  - " + cookie.getName() + " = " + cookie.getValue());
            }
        } else {
            System.out.println("  NO COOKIES");
        }
        System.out.println("================================");
        
        return response;
    }
}
