package com.schoolmanagement.school.service;

import com.schoolmanagement.school.entity.Student;
import com.schoolmanagement.school.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student getStudentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student createStudent(Student student) {
        return repository.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        Student existing = getStudentById(id); // Reuses logic + throws exception if missing
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        return repository.save(existing);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        repository.delete(student);
    }

    public Student updateStudentAge(Long id, int age) {
        Student existing = getStudentById(id);
        existing.setAge(age);
        return repository.save(existing);
    }
}