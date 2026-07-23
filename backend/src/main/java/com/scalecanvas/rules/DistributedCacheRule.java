package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DistributedCacheRule implements ArchitectureRule {
    @Override public String id() { return "DISTRIBUTED_CACHE_READ_HEAVY"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var read = context.scenario().workload().readPercentage();
        var readRps = context.metrics().readRps();
        boolean triggered = RuleSupport.atLeast(read, "80") && RuleSupport.atLeast(readRps, "400");
        return RuleSupport.outcome(id(), "Introduce a distributed cache for repeated reads",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.WATCH : Urgency.NOT_YET,
                triggered ? RecommendationAction.CONSIDER : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.DISTRIBUTED_CACHE,
                "A high repeated-read workload may benefit after query and index optimization is measured.",
                "readPercentage >= 80 and readRps >= 400",
                Map.of("readPercentage", read.toPlainString(), "readRps", readRps.toPlainString()),
                List.of("Lower database read load", "Lower latency for hot data"),
                List.of("Invalidation complexity", "Stale data", "Additional failure mode"),
                "Optimize SQL, indexes and local in-process caches first");
    }
}
