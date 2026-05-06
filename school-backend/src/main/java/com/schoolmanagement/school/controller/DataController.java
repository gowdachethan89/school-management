package com.schoolmanagement.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    // Accessible only by ADMIN role
    @GetMapping("/api/admin/info")
    public String getAdminData() {
        return "Admin-specific protected data.";
    }

    // Accessible only by TEACHER role
    @GetMapping("/api/teacher/info")
    public String getTeacherData() {
        return "Teacher-specific protected data.";
    }

    // Public API (no authentication required)
    @GetMapping("/api/public/info")
    public String getPublicData() {
        return "Publicly accessible information.";
    }
}
