package com.scalecanvas.scenario.api.dto;

import java.time.Instant;
import java.util.UUID;

public record ScenarioVersionSummary(
        UUID id,
        UUID scenarioId,
        String versionLabel,
        Instant createdAt) {
}
