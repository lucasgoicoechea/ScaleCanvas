package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ObjectStorageRule implements ArchitectureRule {
    @Override public String id() { return "OBJECT_STORAGE_FOR_BINARY"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var objectGb = context.scenario().data().objectStorageGb();
        long maxPayload = context.scenario().workload().maximumPayloadBytes();
        boolean triggered = RuleSupport.atLeast(objectGb, "10") || maxPayload >= 1_048_576;
        return RuleSupport.outcome(id(), "Store binary objects outside the relational database",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.DO_NOW : Urgency.WATCH,
                triggered ? RecommendationAction.USE : RecommendationAction.PREPARE,
                ComponentType.OBJECT_STORAGE,
                "Object storage is better suited to large immutable payloads and lifecycle policies.",
                "objectStorageGb >= 10 or maximumPayloadBytes >= 1048576",
                Map.of("objectStorageGb", objectGb.toPlainString(), "maximumPayloadBytes", String.valueOf(maxPayload)),
                List.of("Cheaper capacity", "Lifecycle policies", "Direct delivery options"),
                List.of("Separate authorization", "Consistency between metadata and object"),
                "Keep tiny attachments in the database only when operational simplicity dominates");
    }
}
