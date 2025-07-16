package com.bekx.studenttestservice.repository;

import com.bekx.studenttestservice.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
