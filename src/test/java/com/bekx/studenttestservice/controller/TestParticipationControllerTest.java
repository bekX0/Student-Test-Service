package com.bekx.studenttestservice.controller;

import com.bekx.studenttestservice.dto.StudentTestParticipationDto;
import com.bekx.studenttestservice.dto.TestParticipationDto;
import com.bekx.studenttestservice.model.Student;
import com.bekx.studenttestservice.model.TestParticipation;
import com.bekx.studenttestservice.model.TestType;
import com.bekx.studenttestservice.repository.StudentRepository;
import com.bekx.studenttestservice.repository.TestRepository;
import com.bekx.studenttestservice.service.TestParticipationService;
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

@WebMvcTest(TestParticipationController.class)
@Import(TestParticipationControllerTest.MockConfig.class)
class TestParticipationControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private TestParticipationService testParticipationService;
    @Autowired private StudentRepository studentRepository;
    @Autowired private TestRepository testRepository;
    @Autowired private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean public TestParticipationService testParticipationService() {
            return Mockito.mock(TestParticipationService.class);
        }

        @Bean public StudentRepository studentRepository() {
            return Mockito.mock(StudentRepository.class);
        }

        @Bean public TestRepository testRepository() {
            return Mockito.mock(TestRepository.class);
        }
    }

    @Test
    void createParticipation_shouldReturnOk_whenStudentAndTestExist() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Ali");
        student.setLastName("Demir");

        com.bekx.studenttestservice.model.Test test = new com.bekx.studenttestservice.model.Test();
        test.setId(2L);
        test.setName("Matematik");
        test.setType(TestType.MATEMATIK);

        TestParticipation participation = new TestParticipation();
        participation.setId(5L);
        participation.setStudent(student);
        participation.setTest(test);

        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Mockito.when(testRepository.findById(2L)).thenReturn(Optional.of(test));
        Mockito.when(testParticipationService.save(any(TestParticipation.class))).thenReturn(participation);

        mockMvc.perform(post("/participations")
                        .param("studentId", "1")
                        .param("testId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void getByStudentAndTest_shouldReturnDto_whenFound() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Ahmet");
        student.setLastName("Yılmaz");

        com.bekx.studenttestservice.model.Test test = new com.bekx.studenttestservice.model.Test();
        test.setId(2L);
        test.setName("Tarih");

        TestParticipation participation = new TestParticipation();
        participation.setId(10L);
        participation.setStudent(student);
        participation.setTest(test);

        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Mockito.when(testRepository.findById(2L)).thenReturn(Optional.of(test));
        Mockito.when(testParticipationService.getByStudentAndTest(student, test)).thenReturn(Optional.of(participation));

        mockMvc.perform(get("/participations/by-student-test")
                        .param("studentId", "1")
                        .param("testId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participationId").value(10))
                .andExpect(jsonPath("$.studentName").value("Ahmet Yılmaz"))
                .andExpect(jsonPath("$.testName").value("Tarih"));
    }

    @Test
    void getByTest_shouldReturnListOfParticipants() throws Exception {
        Student student = new Student();
        student.setFirstName("Zeynep");
        student.setLastName("Kara");

        com.bekx.studenttestservice.model.Test test = new com.bekx.studenttestservice.model.Test();
        test.setId(2L);
        test.setName("Genel Kültür");

        TestParticipation participation = new TestParticipation();
        participation.setId(33L);
        participation.setStudent(student);
        participation.setTest(test);

        Mockito.when(testRepository.findById(2L)).thenReturn(Optional.of(test));
        Mockito.when(testParticipationService.getByTest(test)).thenReturn(List.of(participation));

        mockMvc.perform(get("/participations/test/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].participationId").value(33))
                .andExpect(jsonPath("$[0].studentName").value("Zeynep Kara"))
                .andExpect(jsonPath("$[0].testName").value("Genel Kültür"));
    }

    @Test
    void getAll_shouldReturnAllParticipations() throws Exception {
        Student student = new Student();
        student.setFirstName("Ali");
        student.setLastName("Yıldız");

        com.bekx.studenttestservice.model.Test test = new com.bekx.studenttestservice.model.Test();
        test.setName("Matematik");

        TestParticipation participation = new TestParticipation();
        participation.setId(55L);
        participation.setStudent(student);
        participation.setTest(test);

        Mockito.when(testParticipationService.getAll()).thenReturn(List.of(participation));

        mockMvc.perform(get("/participations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].participationId").value(55))
                .andExpect(jsonPath("$[0].studentName").value("Ali Yıldız"))
                .andExpect(jsonPath("$[0].testName").value("Matematik"));
    }

    @Test
    void getByStudent_shouldReturnList_whenStudentExists() throws Exception {
        Student student = new Student();
        student.setId(1L);

        com.bekx.studenttestservice.model.Test test = new com.bekx.studenttestservice.model.Test();
        test.setName("Türkçe");

        TestParticipation participation = new TestParticipation();
        participation.setId(101L);
        participation.setTest(test);

        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Mockito.when(testParticipationService.getByStudent(student)).thenReturn(List.of(participation));

        mockMvc.perform(get("/participations/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].testName").value("Türkçe"));
    }

    @Test
    void createParticipation_shouldReturnBadRequest_whenStudentMissing() throws Exception {
        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/participations")
                        .param("studentId", "1")
                        .param("testId", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Öğrenci veya test bulunamadı!"));
    }
}
