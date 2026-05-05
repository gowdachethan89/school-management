package com.schoolmanagement.school.repository;

import com.schoolmanagement.school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
