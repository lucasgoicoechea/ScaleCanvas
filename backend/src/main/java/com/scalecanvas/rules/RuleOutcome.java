package com.scalecanvas.rules;

import java.util.List;
import java.util.Map;

public record RuleOutcome(
        String ruleId,
        String title,
        String version,
        RuleStatus status,
        Urgency urgency,
        RecommendationAction action,
        ComponentType component,
        String rationale,
        String threshold,
        Map<String, String> evidence,
        List<String> benefits,
        List<String> tradeoffs,
        String simplerAlternative) {
}
