package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MultiAzRule implements ArchitectureRule {
    @Override public String id() { return "MULTI_AZ_FOR_SLO"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var slo = context.scenario().quality().availabilitySloPercent();
        boolean triggered = RuleSupport.atLeast(slo, "99.9");
        return RuleSupport.outcome(id(), "Deploy across failure domains",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.DO_NOW : Urgency.WATCH,
                triggered ? RecommendationAction.USE : RecommendationAction.PREPARE,
                ComponentType.MULTI_AZ,
                "The availability target cannot rely on a single process, host or availability zone.",
                "availabilitySlo >= 99.9",
                Map.of("availabilitySlo", slo.toPlainString(),
                        "allowedMinutesPerMonth", context.metrics().allowedUnavailabilityMinutesPerMonth().toPlainString()),
                List.of("Failure-domain resilience", "Safer maintenance"),
                List.of("Higher cost", "Database failover must be tested"),
                "Document backup/restore and run one-zone recovery exercises first");
    }
}
