package com.scalecanvas.evaluation.api;

import com.scalecanvas.capacity.DerivedMetrics;
import com.scalecanvas.evaluation.ScenarioVariant;
import com.scalecanvas.rules.RuleOutcome;
import java.util.List;

public record VariantResult(
        ScenarioVariant variant,
        DerivedMetrics derivedMetrics,
        List<RuleOutcome> recommendations,
        List<RiskFinding> risks) {
}
