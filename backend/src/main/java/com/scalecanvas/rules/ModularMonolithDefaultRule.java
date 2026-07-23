package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ModularMonolithDefaultRule implements ArchitectureRule {
    @Override public String id() { return "MODULAR_MONOLITH_DEFAULT"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        int team = context.scenario().organization().teamSize();
        var peak = context.scenario().workload().peakRps();
        boolean fits = team <= 12 && RuleSupport.below(peak, "5000");
        return RuleSupport.outcome(
                id(),
                "Start with a modular monolith",
                fits ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                fits ? Urgency.DO_NOW : Urgency.WATCH,
                fits ? RecommendationAction.USE : RecommendationAction.CONSIDER,
                ComponentType.MODULAR_MONOLITH,
                fits
                        ? "The workload and team size do not justify distributed ownership and operations."
                        : "The system may be approaching organizational or throughput limits that require a deliberate decomposition review.",
                "teamSize <= 12 and peakRps < 5000",
                Map.of("teamSize", String.valueOf(team), "peakRps", peak.toPlainString()),
                List.of("Simple deployment", "Local transactions", "Lower operational cost"),
                List.of("Requires enforced module boundaries", "One deployment unit"),
                "Keep a layered monolith only if module boundaries are still immature");
    }
}
