package com.schoolmanagement.school.controller;

import com.schoolmanagement.school.entity.Student;
import com.schoolmanagement.school.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:3000"
})// Allow your React app port
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // GET all students
    @GetMapping
    public List<Student> getStudents() {
        return service.getAllStudents();
    }

    // GET student by ID
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return service.getStudentById(id);
    }

    // create students
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return service.createStudent(student);
    }
    
    // update student by id
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return service.updateStudent(id, student);
    }

    // update student age by id
    @PatchMapping("/{id}")
    public Student updateStudentAge(@PathVariable Long id, @RequestParam(name = "age") int age) {
        return service.updateStudentAge(id, age);
    }

    //delete student by id
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return "Student deleted successfully";
    }
}
