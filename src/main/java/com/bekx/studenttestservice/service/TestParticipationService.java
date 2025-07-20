package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.dto.StudentTestParticipationDto;
import com.bekx.studenttestservice.dto.TestParticipationDto;
import com.bekx.studenttestservice.model.TestParticipation;
import com.bekx.studenttestservice.model.Student;
import com.bekx.studenttestservice.model.Test;
import com.bekx.studenttestservice.repository.StudentRepository;
import com.bekx.studenttestservice.repository.TestParticipationRepository;
import com.bekx.studenttestservice.repository.TestRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestParticipationService {

    private final TestParticipationRepository participationRepo;
    private final StudentRepository studentRepo;
    private final TestRepository testRepo;

    public TestParticipationService(
            TestParticipationRepository participationRepo,
            StudentRepository studentRepo,
            TestRepository testRepo) {
        this.participationRepo = participationRepo;
        this.studentRepo = studentRepo;
        this.testRepo = testRepo;
    }

    public TestParticipation save(TestParticipation participation) {
        return participationRepo.save(participation);
    }

    public Optional<TestParticipation> getByStudentAndTest(Student student, Test test) {
        return participationRepo.findByStudentAndTest(student, test);
    }

    public List<TestParticipation> getByStudent(Student student) {
        return participationRepo.findByStudent(student);
    }

    public List<TestParticipation> getByTest(Test test) {
        return participationRepo.findByTest(test);
    }

    public List<TestParticipation> getAll() {
        return participationRepo.findAll();
    }

    public ResponseEntity<?> createParticipation(Long studentId, Long testId) {
        Optional<Student> student = studentRepo.findById(studentId);
        Optional<Test> test = testRepo.findById(testId);

        if (student.isEmpty() || test.isEmpty()) {
            return ResponseEntity.badRequest().body("Öğrenci veya test bulunamadı!");
        }

        TestParticipation participation = new TestParticipation();
        participation.setStudent(student.get());
        participation.setTest(test.get());
        return ResponseEntity.ok(participationRepo.save(participation));
    }

    public ResponseEntity<?> getByStudentAndTestDetailed(Long studentId, Long testId) {
        Optional<Student> student = studentRepo.findById(studentId);
        Optional<Test> test = testRepo.findById(testId);

        if (student.isEmpty() || test.isEmpty()) {
            return ResponseEntity.badRequest().body("Öğrenci veya test bulunamadı.");
        }

        Optional<TestParticipation> participation =
                participationRepo.findByStudentAndTest(student.get(), test.get());

        return participation
                .map(p -> {
                    String studentName = p.getStudent().getFirstName() + " " + p.getStudent().getLastName();
                    String testName = p.getTest().getName();
                    return ResponseEntity.ok(new StudentTestParticipationDto(p.getId(), studentName, testName));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> getAllByStudent(Long studentId) {
        Optional<Student> student = studentRepo.findById(studentId);
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<TestParticipation> list = participationRepo.findByStudent(student.get());
        List<TestParticipationDto> dtoList = list.stream()
                .map(p -> new TestParticipationDto(p.getId(), p.getTest().getName()))
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    public ResponseEntity<?> getAllByTest(Long testId) {
        Optional<Test> test = testRepo.findById(testId);
        if (test.isEmpty()) return ResponseEntity.notFound().build();

        List<TestParticipation> list = participationRepo.findByTest(test.get());
        List<StudentTestParticipationDto> dtoList = list.stream()
                .map(p -> {
                    String name = p.getStudent().getFirstName() + " " + p.getStudent().getLastName();
                    return new StudentTestParticipationDto(p.getId(), name, p.getTest().getName());
                }).toList();

        return ResponseEntity.ok(dtoList);
    }

    public ResponseEntity<?> getAllDetailed() {
        List<TestParticipation> list = participationRepo.findAll();
        List<StudentTestParticipationDto> dtoList = list.stream()
                .map(p -> {
                    String name = p.getStudent().getFirstName() + " " + p.getStudent().getLastName();
                    return new StudentTestParticipationDto(p.getId(), name, p.getTest().getName());
                }).toList();

        return ResponseEntity.ok(dtoList);
    }
}

