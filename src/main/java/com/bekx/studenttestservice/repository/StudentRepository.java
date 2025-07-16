package com.bekx.studenttestservice.repository;

import com.bekx.studenttestservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
