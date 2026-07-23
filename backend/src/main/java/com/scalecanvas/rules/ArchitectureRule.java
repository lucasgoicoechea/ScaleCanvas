package com.scalecanvas.rules;

public interface ArchitectureRule {
    String id();
    RuleOutcome evaluate(EvaluationContext context);
}
