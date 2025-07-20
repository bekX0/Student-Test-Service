package com.bekx.studenttestservice.controller;

import com.bekx.studenttestservice.dto.AnswerSubmissionDto;
import com.bekx.studenttestservice.model.*;
import com.bekx.studenttestservice.repository.AnswerRepository;
import com.bekx.studenttestservice.repository.QuestionRepository;
import com.bekx.studenttestservice.repository.StudentRepository;
import com.bekx.studenttestservice.repository.TestParticipationRepository;
import com.bekx.studenttestservice.service.StudentAnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/answers")
public class StudentAnswerController {

    private final StudentAnswerService service;

    public StudentAnswerController(StudentAnswerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> submitAnswer(@RequestBody AnswerSubmissionDto request) {
        return service.submitAnswer(request);
    }

    @GetMapping("/stats/{participationId}")
    public ResponseEntity<?> getStats(@PathVariable Long participationId) {
        return service.getStatistics(participationId);
    }

    @PutMapping
    public ResponseEntity<?> updateAnswer(@RequestBody AnswerSubmissionDto request) {
        return service.updateAnswer(request);
    }
}

