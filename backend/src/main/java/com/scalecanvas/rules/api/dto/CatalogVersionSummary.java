package com.scalecanvas.rules.api.dto;

import java.time.Instant;

public record CatalogVersionSummary(
        String id,
        String version,
        String name,
        String source,
        Instant createdAt,
        boolean active) {
}
