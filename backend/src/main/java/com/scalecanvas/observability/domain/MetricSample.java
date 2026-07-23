package com.scalecanvas.observability.domain;

import java.time.Instant;
import java.util.Map;

public record MetricSample(
        String resourceId,
        DimensionKey canonicalKey,
        Instant timestamp,
        Double valueOriginal,
        Unit unitOriginal,
        Double valueCanonical,
        Unit unitCanonical,
        String source,
        MetricQuality quality,
        Map<String, String> dimensions) {
}
