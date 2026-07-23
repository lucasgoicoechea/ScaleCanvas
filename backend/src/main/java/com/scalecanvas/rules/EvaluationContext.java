package com.scalecanvas.rules;

import com.scalecanvas.capacity.DerivedMetrics;
import com.scalecanvas.scenario.domain.ArchitectureScenario;

public record EvaluationContext(ArchitectureScenario scenario, DerivedMetrics metrics) {
}
