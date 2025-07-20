package com.bekx.studenttestservice.controller;


import com.bekx.studenttestservice.model.Question;
import com.bekx.studenttestservice.repository.QuestionRepository;
import com.bekx.studenttestservice.repository.TestRepository;
import com.bekx.studenttestservice.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long testId, @RequestBody Question question) {
        return service.create(testId, question);
    }

    @GetMapping("/by-test/{testId}")
    public ResponseEntity<?> getByTest(@PathVariable Long testId) {
        return service.getByTestId(testId);
    }
}
