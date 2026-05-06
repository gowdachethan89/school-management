package com.schoolmanagement.school.controller;

import com.schoolmanagement.school.entity.User;
import com.schoolmanagement.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signUp(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> getLogin() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Please use POST to /api/public/login with username and password in form data or JSON.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> getLogout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful.");
        return ResponseEntity.ok(response);
    }
}
