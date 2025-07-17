package com.bekx.studenttestservice.repository;

import com.bekx.studenttestservice.model.StudentAnswer;
import com.bekx.studenttestservice.model.TestParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByParticipation(TestParticipation participation);
}

