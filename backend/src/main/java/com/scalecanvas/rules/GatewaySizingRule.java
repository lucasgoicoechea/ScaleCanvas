package com.scalecanvas.rules;

import com.scalecanvas.scenario.domain.GatewayType;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GatewaySizingRule implements ArchitectureRule {
    @Override public String id() { return "GATEWAY_SIZING"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var deployment = context.scenario().deployment();
        int serviceCount = deployment.serviceTopology().services().size();
        boolean noGateway = deployment.gatewayType() == GatewayType.NONE;
        boolean triggered = serviceCount >= 3 && noGateway;
        return RuleSupport.outcome(id(), "Introduce a gateway for multi-service ingress",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.WATCH : Urgency.NOT_YET,
                triggered ? RecommendationAction.CONSIDER : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.API_GATEWAY,
                triggered
                        ? "With three or more services, a gateway centralizes auth, routing and observability."
                        : "A gateway is optional at this service count or one is already configured.",
                "serviceCount >= 3 and gatewayType == NONE",
                Map.of("serviceCount", String.valueOf(serviceCount),
                        "gatewayType", deployment.gatewayType().name()),
                List.of("Single ingress point", "Request shaping", "Centralized security"),
                List.of("Extra hop", "Gateway ownership", "Provider-specific features"),
                "Use a simple load balancer only when the topology is a single service");
    }
}
