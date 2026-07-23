package com.scalecanvas.rules;

import com.scalecanvas.scenario.domain.MaturityLevel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class KubernetesRule implements ArchitectureRule {
    @Override public String id() { return "KUBERNETES_OPERATIONAL_TRIGGER"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var org = context.scenario().organization();
        boolean ready = org.teamSize() >= 20
                && org.operationsMaturity() == MaturityLevel.HIGH
                && org.observabilityMaturity() == MaturityLevel.HIGH
                && org.deploymentFrequencyPerWeek() >= 10
                && org.onCallAvailable();
        return RuleSupport.outcome(id(), ready ? "Kubernetes may now be justified" : "Do not adopt Kubernetes yet",
                RuleStatus.TRIGGERED,
                ready ? Urgency.WATCH : Urgency.NOT_YET,
                ready ? RecommendationAction.CONSIDER : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.KUBERNETES,
                ready
                        ? "The organization has enough deployment volume and operational maturity to evaluate a platform layer."
                        : "The current team and operational model would absorb platform complexity without a proven return.",
                "teamSize >= 20, operations/observability HIGH, deployments >= 10/week and on-call available",
                Map.of("teamSize", String.valueOf(org.teamSize()),
                        "operationsMaturity", org.operationsMaturity().name(),
                        "observabilityMaturity", org.observabilityMaturity().name(),
                        "deploymentsPerWeek", String.valueOf(org.deploymentFrequencyPerWeek()),
                        "onCallAvailable", String.valueOf(org.onCallAvailable())),
                List.of("Standard orchestration", "Platform primitives", "Workload scheduling"),
                List.of("Control-plane and networking complexity", "Requires platform ownership"),
                "Use a managed application platform or container service first");
    }
}
