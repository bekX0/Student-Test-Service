package com.bekx.studenttestservice.controller;

import com.bekx.studenttestservice.model.TestType;
import com.bekx.studenttestservice.service.TestService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
@Import(TestControllerTest.MockConfig.class)
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestService testService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public TestService testService() {
            return Mockito.mock(TestService.class);
        }
    }

    @Test
    void create_shouldReturnCreatedTest() throws Exception {
        com.bekx.studenttestservice.model.Test input = new com.bekx.studenttestservice.model.Test();
        input.setName("Kültür Sınavı");
        input.setType(TestType.GENEL_KULTUR);

        com.bekx.studenttestservice.model.Test saved = new com.bekx.studenttestservice.model.Test();
        saved.setId(10L);
        saved.setName("Kültür Sınavı");
        saved.setType(TestType.GENEL_KULTUR);

        Mockito.when(testService.save(any(com.bekx.studenttestservice.model.Test.class))).thenReturn(saved);

        mockMvc.perform(post("/tests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Kültür Sınavı"))
                .andExpect(jsonPath("$.type").value("GENEL_KULTUR"));
    }

    @Test
    void getAll_shouldReturnListOfTests() throws Exception {
        com.bekx.studenttestservice.model.Test t1 = new com.bekx.studenttestservice.model.Test();
        t1.setId(1L);
        t1.setName("Tarih Sınavı");
        t1.setType(TestType.TARIH);

        com.bekx.studenttestservice.model.Test t2 = new com.bekx.studenttestservice.model.Test();
        t2.setId(2L);
        t2.setName("Matematik Sınavı");
        t2.setType(TestType.MATEMATIK);

        Mockito.when(testService.getAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/tests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Tarih Sınavı"))
                .andExpect(jsonPath("$[1].type").value("MATEMATIK"));
    }
}
