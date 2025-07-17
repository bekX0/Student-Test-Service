package com.bekx.studenttestservice.repository;

import com.bekx.studenttestservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
