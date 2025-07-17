package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.repository.StudentAnswerRepository;
import com.bekx.studenttestservice.model.StudentAnswer;
import com.bekx.studenttestservice.model.TestParticipation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentAnswerService {

    private final StudentAnswerRepository repository;

    public StudentAnswerService(StudentAnswerRepository repository) {
        this.repository = repository;
    }

    public StudentAnswer save(StudentAnswer answer) {
        return repository.save(answer);
    }

    public List<StudentAnswer> getByParticipation(TestParticipation participation) {
        return repository.findByParticipation(participation);
    }

    public int countCorrectAnswers(TestParticipation participation) {
        return Math.toIntExact(repository.findByParticipation(participation)
                .stream()
                .filter(StudentAnswer::isCorrect)
                .count());
    }

    public long countIncorrectAnswers(TestParticipation participation) {
        return repository.findByParticipation(participation)
                .stream()
                .filter(a -> !a.isCorrect())
                .count();
    }
}