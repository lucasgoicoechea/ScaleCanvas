package com.scalecanvas.evaluation.api;

import java.time.Instant;
import java.util.UUID;

public record EvaluationSummary(
        UUID evaluationId,
        UUID scenarioId,
        String scenarioName,
        String catalogVersion,
        Instant generatedAt) {
}
