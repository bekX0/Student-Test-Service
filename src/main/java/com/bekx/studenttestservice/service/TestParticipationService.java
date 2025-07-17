package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.model.TestParticipation;
import com.bekx.studenttestservice.model.Student;
import com.bekx.studenttestservice.model.Test;
import com.bekx.studenttestservice.repository.TestParticipationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestParticipationService {

    private final TestParticipationRepository repository;

    public TestParticipationService(TestParticipationRepository repository) {
        this.repository = repository;
    }

    public TestParticipation save(TestParticipation participation) {
        return repository.save(participation);
    }

    public Optional<TestParticipation> getByStudentAndTest(Student student, Test test) {
        return repository.findByStudentAndTest(student, test);
    }

    public List<TestParticipation> getByStudent(Student student) {
        return repository.findByStudent(student);
    }

    public List<TestParticipation> getByTest(Test test) {
        return repository.findByTest(test);
    }

    public List<TestParticipation> getAll() {
        return repository.findAll();
    }
}
