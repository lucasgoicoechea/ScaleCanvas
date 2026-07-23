package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class HorizontalReplicasRule implements ArchitectureRule {
    @Override public String id() { return "HORIZONTAL_REPLICAS"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var peak = context.scenario().workload().peakRps();
        var slo = context.scenario().quality().availabilitySloPercent();
        boolean triggered = RuleSupport.atLeast(peak, "250") || RuleSupport.atLeast(slo, "99.9");
        return RuleSupport.outcome(id(), "Run stateless horizontal replicas",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.DO_NOW : Urgency.WATCH,
                triggered ? RecommendationAction.USE : RecommendationAction.PREPARE,
                ComponentType.HORIZONTAL_REPLICAS,
                "Replicas increase capacity and remove a single application-process failure point.",
                "peakRps >= 250 or availabilitySlo >= 99.9",
                Map.of("peakRps", peak.toPlainString(), "availabilitySlo", slo.toPlainString()),
                List.of("Elastic capacity", "Rolling deployments", "Process redundancy"),
                List.of("Requires stateless sessions", "Needs load balancing and health checks"),
                "Tune one instance and measure saturation before scaling blindly");
    }
}
