package com.bekx.studenttestservice.controller;

import com.bekx.studenttestservice.model.Test;
import com.bekx.studenttestservice.service.TestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {

    private final TestService service;

    public TestController(TestService service) {
        this.service = service;
    }

    @PostMapping
    public Test create(@Valid @RequestBody Test test) {
        return service.save(test);
    }

    @GetMapping
    public List<Test> getAll() {
        return service.getAll();
    }
}
