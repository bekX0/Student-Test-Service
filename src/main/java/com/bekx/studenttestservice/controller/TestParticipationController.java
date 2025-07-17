package com.bekx.studenttestservice.controller;

import com.bekx.studenttestservice.dto.StudentTestParticipationDto;
import com.bekx.studenttestservice.dto.TestParticipationDto;
import com.bekx.studenttestservice.model.Student;
import com.bekx.studenttestservice.model.Test;
import com.bekx.studenttestservice.model.TestParticipation;
import com.bekx.studenttestservice.repository.StudentRepository;
import com.bekx.studenttestservice.repository.TestRepository;
import com.bekx.studenttestservice.service.TestParticipationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/participations")
public class TestParticipationController {
    private final TestParticipationService testParticipationService;
    private final StudentRepository studentRepository;
    private final TestRepository testRepository;

    public TestParticipationController(TestParticipationService participationService, StudentRepository studentRepository, TestRepository testRepository) {
        this.testParticipationService = participationService;
        this.studentRepository = studentRepository;
        this.testRepository = testRepository;
    }

    @PostMapping
    public ResponseEntity<?> createParticipation(@RequestParam Long studentId, @RequestParam Long testId) {
        Optional<Student> student = studentRepository.findById(studentId);
        Optional<Test> test = testRepository.findById(testId);

        if (student.isEmpty() || test.isEmpty()) {
            return ResponseEntity.badRequest().body("Öğrenci veya test bulunamadı!");
        }

        TestParticipation participation = new TestParticipation();
        participation.setStudent(student.get());
        participation.setTest(test.get());

        return ResponseEntity.ok(testParticipationService.save(participation));
    }

    @GetMapping("/by-student-test")
    public ResponseEntity<?> getByStudentAndTest(@RequestParam Long studentId, @RequestParam Long testId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Test> testOpt = testRepository.findById(testId);

        if (studentOpt.isEmpty() || testOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Öğrenci veya test bulunamadı.");
        }

        Optional<TestParticipation> participationOpt =
                testParticipationService.getByStudentAndTest(studentOpt.get(), testOpt.get());

        return participationOpt
                .map(participation -> {
                    String studentName = participation.getStudent().getFirstName() + " " + participation.getStudent().getLastName();
                    String testName = participation.getTest().getName();
                    StudentTestParticipationDto dto = new StudentTestParticipationDto(participation.getId(), studentName, testName);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TestParticipationDto>> getByStudent(@PathVariable Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<TestParticipation> participations = testParticipationService.getByStudent(student.get());
        List<TestParticipationDto> result = participations.stream()
                .map(p -> new TestParticipationDto(p.getId(), p.getTest().getName()))
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<?> getByTest(@PathVariable Long testId) {
        Optional<Test> testOpt = testRepository.findById(testId);
        if (testOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<TestParticipation> participations = testParticipationService.getByTest(testOpt.get());

        List<StudentTestParticipationDto> dtoList = participations.stream().map(participation -> {
            String studentName = participation.getStudent().getFirstName() + " " + participation.getStudent().getLastName();
            String testName = participation.getTest().getName();
            return new StudentTestParticipationDto(participation.getId(), studentName, testName);
        }).toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping
    public ResponseEntity<List<StudentTestParticipationDto>> getAll() {
        List<TestParticipation> participations = testParticipationService.getAll();

        List<StudentTestParticipationDto> dtoList = participations.stream().map(participation -> {
            String studentName = participation.getStudent().getFirstName() + " " + participation.getStudent().getLastName();
            String testName = participation.getTest().getName();
            return new StudentTestParticipationDto(participation.getId(), studentName, testName);
        }).toList();

        return ResponseEntity.ok(dtoList);
    }
}
