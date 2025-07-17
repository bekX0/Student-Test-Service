package com.bekx.studenttestservice.repository;

import com.bekx.studenttestservice.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}