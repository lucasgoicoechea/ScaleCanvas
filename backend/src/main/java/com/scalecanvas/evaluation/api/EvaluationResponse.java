package com.scalecanvas.evaluation.api;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record EvaluationResponse(
        UUID evaluationId,
        String scenarioName,
        String catalogVersion,
        Instant generatedAt,
        List<VariantResult> results) {
}
