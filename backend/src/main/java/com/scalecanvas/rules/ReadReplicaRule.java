package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ReadReplicaRule implements ArchitectureRule {
    @Override public String id() { return "READ_REPLICA_READ_HEAVY"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var read = context.scenario().workload().readPercentage();
        var readRps = context.metrics().readRps();
        boolean triggered = RuleSupport.atLeast(read, "85") && RuleSupport.atLeast(readRps, "1000");
        return RuleSupport.outcome(id(), "Consider a PostgreSQL read replica",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.WATCH : Urgency.NOT_YET,
                triggered ? RecommendationAction.CONSIDER : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.READ_REPLICA,
                "Read replicas are justified after the primary is optimized and read traffic dominates.",
                "readPercentage >= 85 and readRps >= 1000",
                Map.of("readPercentage", read.toPlainString(), "readRps", readRps.toPlainString()),
                List.of("Offloads analytical and read traffic", "Adds database redundancy options"),
                List.of("Replica lag", "Read-after-write routing complexity"),
                "Tune the primary and cache immutable reference data first");
    }
}
