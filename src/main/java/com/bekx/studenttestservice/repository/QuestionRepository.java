package com.bekx.studenttestservice.repository;

import com.bekx.studenttestservice.model.Question;
import com.bekx.studenttestservice.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTest(Test test);
}
