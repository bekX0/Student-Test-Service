package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.model.Question;
import com.bekx.studenttestservice.model.Test;
import com.bekx.studenttestservice.repository.QuestionRepository;
import com.bekx.studenttestservice.repository.TestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepo;
    private final TestRepository testRepo;

    public QuestionService(QuestionRepository questionRepo, TestRepository testRepo) {
        this.questionRepo = questionRepo;
        this.testRepo = testRepo;
    }

    public ResponseEntity<?> create(Long testId, Question question) {
        Optional<Test> test = testRepo.findById(testId);
        if (test.isEmpty()) {
            return ResponseEntity.badRequest().body("Test bulunamadı");
        }
        question.setTest(test.get());
        return ResponseEntity.ok(questionRepo.save(question));
    }

    public ResponseEntity<?> getByTestId(Long testId) {
        Optional<Test> test = testRepo.findById(testId);
        if (test.isEmpty()) {
            return ResponseEntity.badRequest().body("Test bulunamadı");
        }
        return ResponseEntity.ok(questionRepo.findByTest(test.get()));
    }
}

