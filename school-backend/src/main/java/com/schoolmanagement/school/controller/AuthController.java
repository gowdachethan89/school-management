package com.schoolmanagement.school.controller;

import com.schoolmanagement.school.entity.User;
import com.schoolmanagement.school.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

    @PostMapping("/login")
    public ResponseEntity<String> login(Authentication authentication) {
        // If the code reaches here, it means the user is already authenticated
        // by the Spring Security Filter Chain.
        return ResponseEntity.ok("Login successful! Welcome, " + authentication.getName());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            // This class clears the SecurityContextHolder and invalidates the session
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        Map<String, String> result = new HashMap<>();
        result.put("message", "Logged out successfully. Credentials cleared.");

        return ResponseEntity.ok(result);
    }
}
