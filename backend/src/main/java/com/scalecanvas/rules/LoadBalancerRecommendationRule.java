package com.scalecanvas.rules;

import com.scalecanvas.scenario.domain.LoadBalancerType;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class LoadBalancerRecommendationRule implements ArchitectureRule {
    @Override public String id() { return "LOAD_BALANCER_RECOMMENDATION"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var deployment = context.scenario().deployment();
        boolean hasReplicas = deployment.serviceTopology().services().stream()
                .anyMatch(s -> s.replicas() > 1);
        boolean noLb = deployment.loadBalancerType() == LoadBalancerType.NONE;
        boolean triggered = hasReplicas && noLb;
        return RuleSupport.outcome(id(), "Add a load balancer for replicated services",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.DO_NOW : Urgency.NOT_YET,
                triggered ? RecommendationAction.USE : RecommendationAction.PREPARE,
                ComponentType.LOAD_BALANCER,
                triggered
                        ? "Replicated services need a load balancer to distribute traffic across instances."
                        : "Load balancing is already configured or not required for the current topology.",
                "any(replicas > 1) and loadBalancerType == NONE",
                Map.of("loadBalancerType", deployment.loadBalancerType().name()),
                List.of("Traffic distribution", "Health checks", "Zero-downtime deployments"),
                List.of("Additional infrastructure", "Health probe tuning"),
                "Use a process manager only for single-instance non-production workloads");
    }
}
