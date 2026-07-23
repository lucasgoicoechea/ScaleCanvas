package com.scalecanvas.rules;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

final class RuleSupport {
    private RuleSupport() {
    }

    static boolean atLeast(BigDecimal value, String threshold) {
        return value.compareTo(new BigDecimal(threshold)) >= 0;
    }

    static boolean below(BigDecimal value, String threshold) {
        return value.compareTo(new BigDecimal(threshold)) < 0;
    }

    static RuleOutcome outcome(
            String id,
            String title,
            RuleStatus status,
            Urgency urgency,
            RecommendationAction action,
            ComponentType component,
            String rationale,
            String threshold,
            Map<String, String> evidence,
            List<String> benefits,
            List<String> tradeoffs,
            String alternative) {
        return new RuleOutcome(id, title, "1", status, urgency, action, component, rationale,
                threshold, evidence, benefits, tradeoffs, alternative);
    }
}
