package com.scalecanvas.evaluation.api;

import com.scalecanvas.evaluation.ScenarioVariant;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record EvaluationRequest(
        UUID scenarioId,
        @NotNull @Valid ScenarioRequest scenario,
        @NotEmpty List<ScenarioVariant> variants) {
}
