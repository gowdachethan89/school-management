package com.schoolmanagement.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DataController {

    // Accessible only by USER role
    @GetMapping("users/data")
    public String getUserData() {
        return "User-specific protected data.";
    }

    // Accessible only by ADMIN role
    @GetMapping("admins/data")
    public String getAdminData() {
        return "Admin-specific protected data.";
    }

    // Public API (no authentication required)
    @GetMapping("public/info")
    public String getPublicData() {
        return "Publicly accessible information.";
    }
}
