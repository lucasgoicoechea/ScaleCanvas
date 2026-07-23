package com.scalecanvas.observability.domain;

import java.time.Instant;

public record AlertInstance(
        String id,
        String ruleId,
        String resourceId,
        AlertInstanceState state,
        Instant openedAt,
        Instant updatedAt,
        Instant resolvedAt,
        Double lastValue,
        Double threshold,
        String reason,
        String source,
        String externalAlarmId) {
}
