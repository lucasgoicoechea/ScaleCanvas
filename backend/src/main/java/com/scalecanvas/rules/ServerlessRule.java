package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ServerlessRule implements ArchitectureRule {
    @Override public String id() { return "SERVERLESS_BURSTY_WORKLOAD"; }

    @Override
    public RuleOutcome evaluate(EvaluationContext context) {
        var burst = context.scenario().workload().burstFactor();
        var average = context.scenario().workload().averageRps();
        boolean triggered = RuleSupport.atLeast(burst, "8") && RuleSupport.below(average, "50");
        return RuleSupport.outcome(id(), "Evaluate serverless for highly bursty low-baseline work",
                triggered ? RuleStatus.TRIGGERED : RuleStatus.NOT_TRIGGERED,
                triggered ? Urgency.WATCH : Urgency.NOT_YET,
                triggered ? RecommendationAction.CONSIDER : RecommendationAction.AVOID_FOR_NOW,
                ComponentType.SERVERLESS,
                "A low baseline with large bursts can benefit from scale-to-zero economics.",
                "burstFactor >= 8 and averageRps < 50",
                Map.of("burstFactor", burst.toPlainString(), "averageRps", average.toPlainString()),
                List.of("Automatic burst scaling", "Pay-per-use for intermittent traffic"),
                List.of("Cold starts", "Runtime and duration limits", "Provider coupling"),
                "Use a managed container platform when request duration or portability matters more");
    }
}
