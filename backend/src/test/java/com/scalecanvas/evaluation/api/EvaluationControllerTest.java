package com.scalecanvas.evaluation.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalecanvas.TestFixtures;
import com.scalecanvas.capacity.DerivedMetrics;
import com.scalecanvas.evaluation.AdrGenerator;
import com.scalecanvas.evaluation.EvaluationService;
import com.scalecanvas.evaluation.ScenarioVariant;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EvaluationController.class)
class EvaluationControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean EvaluationService service;
    @MockBean AdrGenerator adrGenerator;

    @Test
    void returnsEvaluation() throws Exception {
        var metrics = new DerivedMetrics(
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ONE,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
        var response = new EvaluationResponse(
                UUID.randomUUID(), "Portfolio SaaS", "test", Instant.now(),
                List.of(new VariantResult(ScenarioVariant.BASELINE, metrics, List.of(), List.of())));
        when(service.evaluate(any())).thenReturn(response);

        var request = new EvaluationRequest(null, TestFixtures.scenarioRequest(), List.of(ScenarioVariant.BASELINE));

        mockMvc.perform(post("/api/v1/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.scenarioName").value("Portfolio SaaS"))
                .andExpect(jsonPath("$.results[0].variant").value("BASELINE"));
    }
}
