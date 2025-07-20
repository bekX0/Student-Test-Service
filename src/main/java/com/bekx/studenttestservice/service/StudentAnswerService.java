package com.bekx.studenttestservice.service;

import com.bekx.studenttestservice.dto.AnswerSubmissionDto;
import com.bekx.studenttestservice.model.*;
import com.bekx.studenttestservice.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentAnswerService {

    private final StudentAnswerRepository repository;
    private final StudentRepository studentRepo;
    private final QuestionRepository questionRepo;
    private final AnswerRepository answerRepo;
    private final TestParticipationRepository participationRepo;

    public StudentAnswerService(
            StudentAnswerRepository repository,
            StudentRepository studentRepo,
            QuestionRepository questionRepo,
            AnswerRepository answerRepo,
            TestParticipationRepository participationRepo
    ) {
        this.repository = repository;
        this.studentRepo = studentRepo;
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.participationRepo = participationRepo;
    }

    public ResponseEntity<?> submitAnswer(AnswerSubmissionDto request) {
        if (exists(request)) {
            return ResponseEntity.badRequest().body("Bu soruya zaten cevap verilmiş. Güncellemek için PUT isteği gönderin.");
        }

        return saveAnswerFromDto(request);
    }

    public ResponseEntity<?> updateAnswer(AnswerSubmissionDto request) {
        Optional<Student> student = studentRepo.findById(request.getStudentId());
        Optional<Question> question = questionRepo.findById(request.getQuestionId());
        Optional<Answer> answer = answerRepo.findById(request.getAnswerId());
        Optional<TestParticipation> participation = participationRepo.findById(request.getParticipationId());

        if (student.isEmpty() || question.isEmpty() || answer.isEmpty() || participation.isEmpty()) {
            return ResponseEntity.badRequest().body("Geçersiz ID'ler");
        }

        Optional<StudentAnswer> existingOpt = repository.findByParticipationAndQuestion(participation.get(), question.get());
        if (existingOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Cevap bulunamadı, önce POST ile oluşturulmalı.");
        }

        StudentAnswer existing = existingOpt.get();
        existing.setSelectedAnswer(answer.get());
        return ResponseEntity.ok(repository.save(existing));
    }

    public ResponseEntity<?> getStatistics(Long participationId) {
        Optional<TestParticipation> participationOpt = participationRepo.findById(participationId);
        if (participationOpt.isEmpty()) return ResponseEntity.notFound().build();

        TestParticipation participation = participationOpt.get();
        List<StudentAnswer> answers = repository.findByParticipation(participation);

        int correct = (int) answers.stream().filter(StudentAnswer::isCorrect).count();
        int total = participation.getTest().getQuestions().size();
        int answered = answers.size();
        int wrong = answered - correct;
        int blank = total - answered;

        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("Toplam", total);
        stats.put("Doğru", correct);
        stats.put("Yanlış", wrong);
        stats.put("Boş", blank);

        return ResponseEntity.ok(stats);
    }

    private ResponseEntity<?> saveAnswerFromDto(AnswerSubmissionDto request) {
        Optional<Student> student = studentRepo.findById(request.getStudentId());
        Optional<Question> question = questionRepo.findById(request.getQuestionId());
        Optional<Answer> answer = answerRepo.findById(request.getAnswerId());
        Optional<TestParticipation> participation = participationRepo.findById(request.getParticipationId());

        if (student.isEmpty() || question.isEmpty() || answer.isEmpty() || participation.isEmpty()) {
            return ResponseEntity.badRequest().body("Geçersiz ID'ler");
        }

        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setStudent(student.get());
        studentAnswer.setQuestion(question.get());
        studentAnswer.setSelectedAnswer(answer.get());
        studentAnswer.setParticipation(participation.get());

        return ResponseEntity.ok(repository.save(studentAnswer));
    }

    private boolean exists(AnswerSubmissionDto dto) {
        Optional<TestParticipation> participation = participationRepo.findById(dto.getParticipationId());
        Optional<Question> question = questionRepo.findById(dto.getQuestionId());

        return participation.isPresent() && question.isPresent()
                && repository.findByParticipationAndQuestion(participation.get(), question.get()).isPresent();
    }

    public List<StudentAnswer> getByParticipation(TestParticipation participation) {
        return repository.findByParticipation(participation);
    }

    public int countCorrectAnswers(TestParticipation participation) {
        return (int) repository.findByParticipation(participation)
                .stream()
                .filter(StudentAnswer::isCorrect)
                .count();
    }
}
