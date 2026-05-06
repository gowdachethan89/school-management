package com.schoolmanagement.school.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DataController {

    // Accessible only by USER role
    @GetMapping("/users/data")
    @PreAuthorize("hasRole('USER')")
    public String getUserData() {
        return "User-specific protected data.";
    }

    // Accessible only by ADMIN role
    @GetMapping("/admins/data")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminData() {
        return "Admin-specific protected data.";
    }

    // Public API (no authentication required)
    @GetMapping("/public/info")
    public String getPublicData() {
        return "Publicly accessible information.";
    }
}
