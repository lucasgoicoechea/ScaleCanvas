package com.scalecanvas.ai;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExplanationController.class)
class ExplanationControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean ExplanationService service;

    @Test
    void returnsExplanation() throws Exception {
        when(service.explain(any())).thenReturn(new ExplanationResponse(Instant.now(), "OK", "ollama", "qwen3.5:9b"));

        var request = new ExplanationRequest(
                new com.scalecanvas.evaluation.api.EvaluationResponse(
                        UUID.randomUUID(), "Test", "test", Instant.now(), List.of()),
                null);

        mockMvc.perform(post("/api/v1/explanations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.explanation").value("OK"))
                .andExpect(jsonPath("$.provider").value("ollama"))
                .andExpect(jsonPath("$.model").value("qwen3.5:9b"));
    }
}
