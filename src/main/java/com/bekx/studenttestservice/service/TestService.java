package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.model.Test;
import com.bekx.studenttestservice.repository.TestRepository;
import com.bekx.studenttestservice.model.Question;
import com.bekx.studenttestservice.model.Answer;
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
}
