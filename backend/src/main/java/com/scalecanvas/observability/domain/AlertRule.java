package com.scalecanvas.observability.domain;

import java.util.List;

public record AlertRule(
        String id,
        String name,
        String scopeSelector,
        DimensionKey metricKey,
        String operator,
        Double warningThreshold,
        Double criticalThreshold,
        String aggregation,
        String window,
        Integer minimumSamples,
        String enterDuration,
        String exitDuration,
        String cooldown,
        String noDataPolicy,
        Boolean enabled,
        List<String> notificationTargets) {
}
