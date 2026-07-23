package com.scalecanvas.observability.domain;

import java.time.Instant;
import java.util.List;

public record DimensionSnapshot(
        DimensionKey key,
        Double current,
        Double maximum,
        Double ratio,
        Double visualRatio,
        Unit originalUnit,
        Unit canonicalUnit,
        ResourceLifecycleState state,
        Instant timestamp,
        Instant staleAfter,
        List<String> reasons,
        List<DimensionSnapshot> subDimensions) {
}
