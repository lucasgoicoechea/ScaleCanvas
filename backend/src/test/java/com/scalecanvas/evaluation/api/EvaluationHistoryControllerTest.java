package com.scalecanvas.evaluation.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.scalecanvas.TestFixtures;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EvaluationHistoryController.class)
class EvaluationHistoryControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean EvaluationService service;

    @Test
    void returnsEvaluationHistory() throws Exception {
        when(service.listEvaluations()).thenReturn(List.of(
                new EvaluationSummary(UUID.randomUUID(), UUID.randomUUID(), "Portfolio SaaS", "1.0.0", Instant.now())));

        mockMvc.perform(get("/api/v1/evaluations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].scenarioName").value("Portfolio SaaS"));
    }

    @Test
    void returnsEvaluationById() throws Exception {
        var metrics = new com.scalecanvas.capacity.DerivedMetrics(
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ONE,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
        var response = new EvaluationResponse(
                UUID.randomUUID(), "Portfolio SaaS", "test", Instant.now(),
                List.of(new VariantResult(ScenarioVariant.BASELINE, metrics, List.of(), List.of())));
        when(service.getEvaluation(any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/evaluations/" + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scenarioName").value("Portfolio SaaS"))
                .andExpect(jsonPath("$.results[0].variant").value("BASELINE"));
    }
}
