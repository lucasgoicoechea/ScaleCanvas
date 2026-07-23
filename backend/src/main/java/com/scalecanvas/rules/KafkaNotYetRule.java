package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class KafkaNotYetRule implements ArchitectureRule {
    @Override public String id() { return "KAFKA_NOT_YET"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var events = context.scenario().data().eventVolumePerDay();
        boolean highVolume = RuleSupport.atLeast(events, "10000000");
        return RuleSupport.outcome(id(), highVolume ? "Evaluate an event streaming platform" : "Kafka is not justified yet",
                RuleStatus.TRIGGERED,
                highVolume ? Urgency.WATCH : Urgency.NOT_YET,
                highVolume ? RecommendationAction.CONSIDER : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.KAFKA,
                highVolume
                        ? "Event volume is high enough to investigate replay, partitions and durable streams."
                        : "A simple queue or transactional outbox is easier to operate for the current event volume.",
                "eventVolumePerDay >= 10000000 plus a replay/streaming requirement",
                Map.of("eventVolumePerDay", events.toPlainString()),
                List.of("Durable replay", "Partitioned throughput", "Stream ecosystem"),
                List.of("Operational overhead", "Schema and partition governance"),
                "Use a managed queue and transactional outbox first");
    }
}
