package com.scalecanvas.evaluation.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EvaluationTimelineItem(
        UUID evaluationId,
        UUID scenarioId,
        String scenarioName,
        String catalogVersion,
        Instant generatedAt,
        String variant,
        BigDecimal peakRps,
        BigDecimal storage12MonthsGb,
        BigDecimal allowedDowntimeMinutesPerMonth,
        int recommendationCount) {
}