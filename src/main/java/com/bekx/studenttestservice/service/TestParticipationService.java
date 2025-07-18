package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.model.TestParticipation;
import com.bekx.studenttestservice.model.Student;
import com.bekx.studenttestservice.model.Test;
import com.bekx.studenttestservice.repository.TestParticipationRepository;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "participationByStudentAndTest", key = "#student.id + '-' + #test.id")
    public Optional<TestParticipation> getByStudentAndTest(Student student, Test test) {
        return repository.findByStudentAndTest(student, test);
    }

    @Cacheable(value = "participationsByStudent", key = "#student.id")
    public List<TestParticipation> getByStudent(Student student) {
        return repository.findByStudent(student);
    }

    @Cacheable(value = "participationsByTest", key = "#test.id")
    public List<TestParticipation> getByTest(Test test) {
        return repository.findByTest(test);
    }

    @Cacheable("allParticipations")
    public List<TestParticipation> getAll() {
        return repository.findAll();
    }
}
