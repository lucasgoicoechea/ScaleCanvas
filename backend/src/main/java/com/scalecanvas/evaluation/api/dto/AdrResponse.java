package com.scalecanvas.evaluation.api.dto;

import java.time.Instant;

public record AdrResponse(
        String id,
        String title,
        String context,
        String options,
        String decision,
        String consequences,
        String generatedFromEvaluationId,
        Instant generatedAt) {
}
