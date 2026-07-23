package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ShardingNotYetRule implements ArchitectureRule {
    @Override public String id() { return "SHARDING_NOT_YET"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var storage = context.metrics().storageAfter12MonthsGb();
        var writes = context.metrics().writeRps();
        boolean candidate = RuleSupport.atLeast(storage, "1000") && RuleSupport.atLeast(writes, "5000");
        return RuleSupport.outcome(id(), candidate ? "Start a sharding feasibility study" : "Do not shard the database yet",
                RuleStatus.TRIGGERED,
                candidate ? Urgency.WATCH : Urgency.NOT_YET,
                candidate ? RecommendationAction.PREPARE : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.SHARDING,
                candidate
                        ? "Projected data and write throughput justify identifying a stable partition key before saturation."
                        : "Partitioning, indexing and vertical scaling remain simpler and safer.",
                "storageAfter12MonthsGb >= 1000 and writeRps >= 5000",
                Map.of("storageAfter12MonthsGb", storage.toPlainString(), "writeRps", writes.toPlainString()),
                List.of("Higher write capacity", "Horizontal data distribution"),
                List.of("Cross-shard queries", "Rebalancing", "Operational complexity"),
                "Use native table partitioning and archive cold data first");
    }
}
