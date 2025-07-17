package com.bekx.studenttestservice.controller;

import com.bekx.studenttestservice.model.Student;
import com.bekx.studenttestservice.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import(StudentControllerTest.MockConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public StudentService studentService() {
            return Mockito.mock(StudentService.class);
        }
    }

    @Test
    void getAll_shouldReturnListOfStudents() throws Exception {
        Student s1 = new Student();
        s1.setId(1L);
        s1.setFirstName("Ali");
        s1.setLastName("Yılmaz");
        s1.setNumber("1001");

        Student s2 = new Student();
        s2.setId(2L);
        s2.setFirstName("Ayşe");
        s2.setLastName("Demir");
        s2.setNumber("1002");

        Mockito.when(studentService.getAll()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Ali"))
                .andExpect(jsonPath("$[1].lastName").value("Demir"));
    }

    @Test
    void create_shouldReturnCreatedStudent() throws Exception {
        Student input = new Student();
        input.setFirstName("Zeynep");
        input.setLastName("Kara");
        input.setNumber("1003");

        Student saved = new Student();
        saved.setId(3L);
        saved.setFirstName("Zeynep");
        saved.setLastName("Kara");
        saved.setNumber("1003");

        Mockito.when(studentService.save(any(Student.class))).thenReturn(saved);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.firstName").value("Zeynep"))
                .andExpect(jsonPath("$.number").value("1003"));
    }

    @Test
    void getOne_shouldReturnStudent_whenFound() throws Exception {
        Student student = new Student();
        student.setId(5L);
        student.setFirstName("Mehmet");
        student.setLastName("Çelik");
        student.setNumber("2001");

        Mockito.when(studentService.getById(5L)).thenReturn(student);

        mockMvc.perform(get("/students/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.firstName").value("Mehmet"))
                .andExpect(jsonPath("$.lastName").value("Çelik"));
    }

    @Test
    void getOne_shouldReturn404_whenStudentNotFound() throws Exception {
        Mockito.when(studentService.getById(999L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Öğrenci bulunamadı"));

        mockMvc.perform(get("/students/999"))
                .andExpect(status().isNotFound());
    }
}
