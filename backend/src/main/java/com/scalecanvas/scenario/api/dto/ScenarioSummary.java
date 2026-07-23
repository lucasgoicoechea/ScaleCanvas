package com.scalecanvas.scenario.api.dto;

import java.time.Instant;
import java.util.UUID;

public record ScenarioSummary(
        UUID id,
        String name,
        String description,
        String productType,
        Instant createdAt,
        Instant updatedAt) {
}
