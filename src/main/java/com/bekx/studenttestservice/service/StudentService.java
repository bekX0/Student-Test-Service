package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.model.Student;
import com.bekx.studenttestservice.model.TestParticipation;
import com.bekx.studenttestservice.repository.StudentAnswerRepository;
import com.bekx.studenttestservice.repository.StudentRepository;
import com.bekx.studenttestservice.repository.TestParticipationRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final TestParticipationRepository testParticipationRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    public StudentService(StudentRepository repository,
                          TestParticipationRepository testParticipationRepository,
                          StudentAnswerRepository studentAnswerRepository) {
        this.repository = repository;
        this.testParticipationRepository = testParticipationRepository;
        this.studentAnswerRepository = studentAnswerRepository;
    }

    public Student save(Student student) {
        return repository.save(student);
    }

    public Page<Student> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Cacheable(value = "studentById", key = "#id")
    public Student getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Öğrenci Bulunamadı!"));
    }

    public ResponseEntity<?> update(Long id, Student newStudent) {
        try {
            Student existing = getById(id);
            existing.setFirstName(newStudent.getFirstName());
            existing.setLastName(newStudent.getLastName());
            existing.setNumber(newStudent.getNumber());
            return ResponseEntity.ok(repository.save(existing));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> delete(Long id) {
        Optional<Student> studentOpt = repository.findById(id);
        if (studentOpt.isEmpty()) return ResponseEntity.notFound().build();

        Student student = studentOpt.get();

        List<TestParticipation> participations = testParticipationRepository.findByStudent(student);
        for (TestParticipation participation : participations) {
            studentAnswerRepository.deleteAll(studentAnswerRepository.findByParticipation(participation));
        }

        testParticipationRepository.deleteAll(participations);
        repository.delete(student);

        return ResponseEntity.noContent().build();
    }
}
