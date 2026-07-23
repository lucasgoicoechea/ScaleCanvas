package com.scalecanvas.ai;

import static org.assertj.core.api.Assertions.assertThat;

import com.scalecanvas.capacity.DerivedMetrics;
import com.scalecanvas.evaluation.api.EvaluationResponse;
import com.scalecanvas.evaluation.api.VariantResult;
import com.scalecanvas.evaluation.ScenarioVariant;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ExplanationServiceTest {
    @Test
    void returnsStaticMessageWhenAiDisabled() {
        var service = new ExplanationService(List.of(), false);
        var response = service.explain(request());
        assertThat(response.explanation()).contains("disabled");
        assertThat(response.provider()).isEqualTo("none");
        assertThat(response.model()).isEqualTo("none");
    }

    private ExplanationRequest request() {
        var metrics = new DerivedMetrics(
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ONE,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
        var evaluation = new EvaluationResponse(
                UUID.randomUUID(), "Test", "test", Instant.now(),
                List.of(new VariantResult(ScenarioVariant.BASELINE, metrics, List.of(), List.of())));
        return new ExplanationRequest(evaluation, null);
    }
}
