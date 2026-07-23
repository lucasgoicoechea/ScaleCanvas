package com.scalecanvas.observation;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class EvaluationMetrics {

    private final Counter evaluationCounter;
    private final Counter recommendationCounter;

    public EvaluationMetrics(MeterRegistry registry) {
        this.evaluationCounter = Counter.builder("scalecanvas.evaluations.total")
                .description("Total evaluations executed")
                .register(registry);
        this.recommendationCounter = Counter.builder("scalecanvas.recommendations.total")
                .description("Total recommendations emitted")
                .register(registry);
    }

    public void incrementEvaluation() {
        evaluationCounter.increment();
    }

    public void incrementRecommendation(int count) {
        recommendationCounter.increment(count);
    }
}
