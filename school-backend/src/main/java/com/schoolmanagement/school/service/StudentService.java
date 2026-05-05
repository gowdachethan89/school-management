package com.schoolmanagement.school.service;

import com.schoolmanagement.school.entity.Student;
import com.schoolmanagement.school.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return repository.findById(id).orElse(null);
    }

    public Student createStudent(Student student) {
        return repository.save(student);
    }
    
    public Student updateStudent(Long id, Student student) {
    	Optional<Student> studentExisting = repository.findById(id);
    	if(studentExisting.isPresent()) {
    		Student existing = studentExisting.get();
    		existing.setName(student.getName());
            existing.setAge(student.getAge());
            
            return repository.save(existing); // save updated data
        }
        return null; // or throw exception
    }
    public void deleteStudent(Long id) {
        Student student = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        repository.delete(student);
    }

    public Student updateStudentAge(Long id, int age) {
        Optional<Student> studentExisting = repository.findById(id);
        if(studentExisting.isPresent()) {
            Student existing = studentExisting.get();
            existing.setAge(age);

            return repository.save(existing); // save updated data
        }
        return null; // or throw exception
    }
}