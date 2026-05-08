package com.schoolmanagement.school.controller;

import com.schoolmanagement.school.entity.Student;
import com.schoolmanagement.school.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:3000",
        "https://school-management-one-beryl.vercel.app"
}, allowCredentials = "true")// Allow your React app port
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        // Debug logging
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("=== GET /api/students ===");
        System.out.println("Authentication: " + (auth != null ? auth.getName() : "null"));
        System.out.println("Authenticated: " + (auth != null && auth.isAuthenticated()));
        System.out.println("Authorities: " + (auth != null ? auth.getAuthorities() : "null"));
        System.out.println("========================");

        return ResponseEntity.ok(service.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student created = service.createStudent(student);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(service.updateStudent(id, student));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> updateStudentAge(@PathVariable Long id, @RequestParam int age) {
        return ResponseEntity.ok(service.updateStudentAge(id, age));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student deleted successfully");
        return ResponseEntity.ok(response);
    }
}