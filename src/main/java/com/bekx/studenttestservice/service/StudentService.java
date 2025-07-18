package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.model.Student;
import com.bekx.studenttestservice.repository.StudentRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student save(Student student) {
        return repository.save(student);
    }

    @Cacheable("allStudents")
    public List<Student> getAll() {
        return repository.findAll();
    }

    @Cacheable(value = "studentById", key = "#id")
    public Student getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Öğrenci Bulunamadı!"));
    }
}
