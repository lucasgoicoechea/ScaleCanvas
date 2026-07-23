package com.scalecanvas.rules;

import com.scalecanvas.scenario.domain.DeploymentService;
import com.scalecanvas.scenario.domain.ServerType;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ServerlessTriggerRule implements ArchitectureRule {
    @Override public String id() { return "SERVERLESS_TRIGGER"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var deployment = context.scenario().deployment();
        boolean serverlessPlatform = deployment.serverType() == ServerType.SERVERLESS
                || deployment.deploymentService() == DeploymentService.LAMBDA
                || deployment.deploymentService() == DeploymentService.CLOUD_RUN;
        long totalRpm = deployment.serviceTopology().services().stream()
                .mapToLong(s -> s.requestsPerMinute()).sum();
        long serviceCount = deployment.serviceTopology().services().size();
        boolean lowTraffic = serviceCount > 0 && totalRpm / serviceCount < 100;
        boolean triggered = serverlessPlatform && lowTraffic;
        return RuleSupport.outcome(id(), "Serverless may fit low-traffic service fleet",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.WATCH : Urgency.NOT_YET,
                triggered ? RecommendationAction.CONSIDER : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.SERVERLESS,
                triggered
                        ? "Many small services with low per-service RPM are candidates for scale-to-zero platforms."
                        : "Current traffic pattern does not clearly justify a serverless-first model.",
                "serverType == SERVERLESS or deploymentService in (LAMBDA, CLOUD_RUN) and avgRPM < 100",
                Map.of("totalRpm", String.valueOf(totalRpm),
                        "serviceCount", String.valueOf(serviceCount)),
                List.of("Pay-per-invocation", "No idle capacity"),
                List.of("Cold start latency", "Runtime limits", "Vendor lock-in"),
                "Use containers when latency predictability or portability is required");
    }
}
