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

    private final TestParticipationService service;

    public TestParticipationController(TestParticipationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createParticipation(@RequestParam Long studentId, @RequestParam Long testId) {
        return service.createParticipation(studentId, testId);
    }

    @GetMapping("/by-student-test")
    public ResponseEntity<?> getByStudentAndTest(@RequestParam Long studentId, @RequestParam Long testId) {
        return service.getByStudentAndTestDetailed(studentId, testId);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getByStudent(@PathVariable Long studentId) {
        return service.getAllByStudent(studentId);
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<?> getByTest(@PathVariable Long testId) {
        return service.getAllByTest(testId);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return service.getAllDetailed();
    }
}

