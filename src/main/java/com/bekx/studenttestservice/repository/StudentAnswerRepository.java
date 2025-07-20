package com.bekx.studenttestservice.repository;

import com.bekx.studenttestservice.model.Question;
import com.bekx.studenttestservice.model.StudentAnswer;
import com.bekx.studenttestservice.model.TestParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByParticipation(TestParticipation participation);
    Optional<StudentAnswer> findByParticipationAndQuestion(TestParticipation participation, Question question);

}

