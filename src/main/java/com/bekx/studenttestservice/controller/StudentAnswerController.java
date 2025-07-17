package com.bekx.studenttestservice.controller;

import com.bekx.studenttestservice.dto.AnswerSubmissionDto;
import com.bekx.studenttestservice.model.*;
import com.bekx.studenttestservice.repository.AnswerRepository;
import com.bekx.studenttestservice.repository.QuestionRepository;
import com.bekx.studenttestservice.repository.StudentRepository;
import com.bekx.studenttestservice.repository.TestParticipationRepository;
import com.bekx.studenttestservice.service.StudentAnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/answers")
public class StudentAnswerController {

    private final StudentAnswerService service;
    private final TestParticipationRepository participationRepo;
    private final StudentRepository studentRepo;
    private final QuestionRepository questionRepo;
    private final AnswerRepository answerRepo;

    public StudentAnswerController(
            StudentAnswerService service,
            TestParticipationRepository participationRepo,
            StudentRepository studentRepo,
            QuestionRepository questionRepo,
            AnswerRepository answerRepo
    ) {
        this.service = service;
        this.participationRepo = participationRepo;
        this.studentRepo = studentRepo;
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
    }


    @PostMapping
    public ResponseEntity<?> submitAnswer(@RequestBody AnswerSubmissionDto request) {
        Optional<Student> student = studentRepo.findById(request.getStudentId());
        Optional<Question> question = questionRepo.findById(request.getQuestionId());
        Optional<Answer> answer = answerRepo.findById(request.getAnswerId());
        Optional<TestParticipation> participation = participationRepo.findById(request.getParticipationId());

        if (student.isEmpty() || question.isEmpty() || answer.isEmpty() || participation.isEmpty()) {
            return ResponseEntity.badRequest().body("Geçersiz veri. Tüm ID’ler geçerli olmalıdır.");
        }

        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setStudent(student.get());
        studentAnswer.setQuestion(question.get());
        studentAnswer.setSelectedAnswer(answer.get());
        studentAnswer.setParticipation(participation.get());

        return ResponseEntity.ok(service.save(studentAnswer));
    }

    @GetMapping("/stats/{participationId}")
    public ResponseEntity<?> getStats(@PathVariable Long participationId) {
        return participationRepo.findById(participationId)
                .map(participation -> {
                    int correct = service.countCorrectAnswers(participation);
                    int total = participation.getTest().getQuestions().size();
                    int answered = service.getByParticipation(participation).size();
                    int wrong = answered - correct;
                    int blank = total - answered;

                    Map<String, Integer> stats = new HashMap<>();
                    stats.put("Toplam", total);
                    stats.put("Doğru", correct);
                    stats.put("Yanlış", wrong);
                    stats.put("Boş", blank);
                    return ResponseEntity.ok(stats);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
