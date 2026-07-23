package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AsyncQueueRule implements ArchitectureRule {
    @Override public String id() { return "ASYNC_LONG_RUNNING_WORK"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var async = context.scenario().workload().asynchronousWorkPercentage();
        int batches = context.scenario().workload().batchJobsPerDay();
        boolean triggered = RuleSupport.atLeast(async, "10") || batches > 0;
        return RuleSupport.outcome(id(), "Move long-running work to a queue",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.DO_NOW : Urgency.WATCH,
                triggered ? RecommendationAction.USE : RecommendationAction.PREPARE,
                ComponentType.MESSAGE_QUEUE,
                "Asynchronous processing protects request latency and allows retries with explicit state.",
                "asyncWork >= 10% or batchJobsPerDay > 0",
                Map.of("asyncWorkPercentage", async.toPlainString(), "batchJobsPerDay", String.valueOf(batches)),
                List.of("Shorter request paths", "Backpressure", "Retry control"),
                List.of("Eventual completion", "Idempotency and dead-letter handling required"),
                "Use a database-backed job table for very low volume");
    }
}
