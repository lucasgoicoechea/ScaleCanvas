package com.scalecanvas.rules;

import com.scalecanvas.scenario.domain.GeographicScope;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CdnRule implements ArchitectureRule {
    @Override public String id() { return "CDN_STATIC_OR_GLOBAL"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var scope = context.scenario().quality().geographicScope();
        boolean triggered = scope == GeographicScope.GLOBAL || scope == GeographicScope.MULTI_COUNTRY;
        return RuleSupport.outcome(id(), "Use a CDN and edge caching for global delivery",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.DO_NOW : Urgency.NOT_YET,
                triggered ? RecommendationAction.USE : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.CDN,
                "Global users benefit from static content and cacheable responses served near them.",
                "geographicScope in [MULTI_COUNTRY, GLOBAL]",
                Map.of("geographicScope", scope.name()),
                List.of("Lower global latency", "Origin offload", "DDoS absorption options"),
                List.of("Cache invalidation", "Regional behavior must be observable"),
                "Serve from one region until measured latency justifies edge delivery");
    }
}
