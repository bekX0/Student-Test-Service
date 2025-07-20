package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.model.Test;
import com.bekx.studenttestservice.repository.TestRepository;
import com.bekx.studenttestservice.model.Question;
import com.bekx.studenttestservice.model.Answer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestService {
    private final TestRepository repository;

    public TestService(TestRepository repository) {
        this.repository = repository;
    }

    public Test save(Test test) {
        for (Question question : test.getQuestions()) {
            question.setTest(test);
            if (question.getAnswers() != null) {
                for (Answer answer : question.getAnswers()) {
                    answer.setQuestion(question);
                }
            }
        }
        return repository.save(test);
    }

    public List<Test> getAll() {
        return repository.findAll();
    }

    public Test getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test bulunamadı!"));
    }

    public ResponseEntity<?> getByIdSafe(Long id) {
        try {
            return ResponseEntity.ok(getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> update(Long id, Test updatedTest) {
        try {
            Test existing = getById(id);
            existing.setName(updatedTest.getName());
            existing.setType(updatedTest.getType());
            return ResponseEntity.ok(repository.save(existing));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> delete(Long id) {
        try {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

