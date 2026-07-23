package com.scalecanvas.rules;

import com.scalecanvas.scenario.domain.ServerType;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ContainerMinUnitRule implements ArchitectureRule {
    @Override public String id() { return "CONTAINER_MIN_UNIT"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var deployment = context.scenario().deployment();
        boolean container = deployment.serverType() == com.scalecanvas.scenario.domain.ServerType.CONTAINER;
        boolean smallUnit = deployment.minimumUnitMemoryMb() < 2048 || deployment.minimumUnitCpuCount() < 2;
        boolean triggered = container && smallUnit;
        return RuleSupport.outcome(id(), "Review container minimum unit size",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.WATCH : Urgency.NOT_YET,
                triggered ? RecommendationAction.CONSIDER : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.CONTAINER,
                triggered
                        ? "A unit below 2 GB memory or 2 vCPU may cause noisy-neighbor issues under load."
                        : "The declared container unit meets the minimum practical baseline.",
                "serverType == CONTAINER and (minimumUnitMemoryMb < 2048 or minimumUnitCpuCount < 2)",
                Map.of("minimumUnitMemoryMb", String.valueOf(deployment.minimumUnitMemoryMb()),
                        "minimumUnitCpuCount", String.valueOf(deployment.minimumUnitCpuCount())),
                List.of("Predictable performance", "Better scheduling"),
                List.of("Higher cost per unit", "Less density"),
                "Use smaller units only for dev or low-traffic services");
    }
}
