package com.bekx.studenttestservice.controller;

import com.bekx.studenttestservice.dto.AnswerSubmissionDto;
import com.bekx.studenttestservice.model.*;
import com.bekx.studenttestservice.repository.*;
import com.bekx.studenttestservice.service.StudentAnswerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentAnswerController.class)
@Import(StudentAnswerControllerTest.MockConfig.class)
class StudentAnswerControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private StudentAnswerService studentAnswerService;
    @Autowired private StudentRepository studentRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private AnswerRepository answerRepository;
    @Autowired private TestParticipationRepository testParticipationRepository;
    @Autowired private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean public StudentAnswerService studentAnswerService() {
            return Mockito.mock(StudentAnswerService.class);
        }
        @Bean public StudentRepository studentRepository() {
            return Mockito.mock(StudentRepository.class);
        }
        @Bean public QuestionRepository questionRepository() {
            return Mockito.mock(QuestionRepository.class);
        }
        @Bean public AnswerRepository answerRepository() {
            return Mockito.mock(AnswerRepository.class);
        }
        @Bean public TestParticipationRepository testParticipationRepository() {
            return Mockito.mock(TestParticipationRepository.class);
        }
    }

    @Test
    void submitAnswer_shouldReturnOk_whenValidRequest() throws Exception {
        AnswerSubmissionDto request = new AnswerSubmissionDto(1L, 2L, 3L, 4L);

        Student student = new Student(); student.setId(1L);
        Question question = new Question(); question.setId(2L);
        Answer answer = new Answer(); answer.setId(3L);
        TestParticipation participation = new TestParticipation(); participation.setId(4L);

        StudentAnswer studentAnswer = new StudentAnswer(); studentAnswer.setId(999L);

        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Mockito.when(questionRepository.findById(2L)).thenReturn(Optional.of(question));
        Mockito.when(answerRepository.findById(3L)).thenReturn(Optional.of(answer));
        Mockito.when(testParticipationRepository.findById(4L)).thenReturn(Optional.of(participation));
        Mockito.when(studentAnswerService.save(any(StudentAnswer.class))).thenReturn(studentAnswer);

        mockMvc.perform(post("/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(999));
    }

    @Test
    void submitAnswer_shouldReturnBadRequest_whenAnyIdInvalid() throws Exception {
        AnswerSubmissionDto request = new AnswerSubmissionDto(1L, 2L, 3L, 4L);
        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Geçersiz veri. Tüm ID’ler geçerli olmalıdır."));
    }

    @Test
    void getStats_shouldReturnStats_whenParticipationExists() throws Exception {
        com.bekx.studenttestservice.model.Test test = new com.bekx.studenttestservice.model.Test();
        Question q1 = new Question(); q1.setId(1L);
        Question q2 = new Question(); q2.setId(2L);
        Question q3 = new Question(); q3.setId(3L);
        test.setQuestions(List.of(q1, q2, q3));

        TestParticipation participation = new TestParticipation();
        participation.setId(5L);
        participation.setTest(test);

        StudentAnswer correctAnswer = new StudentAnswer();
        Answer correct = new Answer(); correct.setCorrect(true);
        correctAnswer.setSelectedAnswer(correct);

        StudentAnswer wrongAnswer = new StudentAnswer();
        Answer wrong = new Answer(); wrong.setCorrect(false);
        wrongAnswer.setSelectedAnswer(wrong);

        Mockito.when(testParticipationRepository.findById(5L)).thenReturn(Optional.of(participation));
        Mockito.when(studentAnswerService.countCorrectAnswers(participation)).thenReturn(1);
        Mockito.when(studentAnswerService.getByParticipation(participation))
                .thenReturn(List.of(correctAnswer, wrongAnswer));

        mockMvc.perform(get("/answers/stats/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Toplam").value(3))
                .andExpect(jsonPath("$.Doğru").value(1))
                .andExpect(jsonPath("$.Yanlış").value(1))
                .andExpect(jsonPath("$.Boş").value(1));
    }

    @Test
    void getStats_shouldReturnNotFound_whenParticipationNotFound() throws Exception {
        Mockito.when(testParticipationRepository.findById(88L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/answers/stats/88"))
                .andExpect(status().isNotFound());
    }
}
