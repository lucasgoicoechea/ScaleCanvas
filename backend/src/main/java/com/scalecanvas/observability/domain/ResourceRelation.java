package com.scalecanvas.observability.domain;

import java.time.Instant;

public record ResourceRelation(
        String parentId,
        String childId,
        RelationType relationType,
        String source,
        Double confidence,
        Instant effectiveFrom,
        Instant effectiveTo) {
}
