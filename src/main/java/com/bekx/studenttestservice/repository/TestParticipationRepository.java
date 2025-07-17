package com.bekx.studenttestservice.repository;

import com.bekx.studenttestservice.model.TestParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bekx.studenttestservice.model.Student;
import com.bekx.studenttestservice.model.Test;

import java.util.List;
import java.util.Optional;

public interface TestParticipationRepository extends JpaRepository<TestParticipation, Long> {
    List<TestParticipation> findByStudent(Student student);

    List<TestParticipation> findByTest(Test test);

    Optional<TestParticipation> findByStudentAndTest(Student student, Test test);
}
