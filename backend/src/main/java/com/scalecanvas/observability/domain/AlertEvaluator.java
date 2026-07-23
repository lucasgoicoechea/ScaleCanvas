package com.scalecanvas.observability.domain;

public final class AlertEvaluator {

    private AlertEvaluator() {
    }

    public static AlertInstanceState evaluate(
            AlertRule rule,
            Double currentValue,
            double enterWarning,
            double exitWarning,
            double enterCritical,
            double exitCritical) {

        if (currentValue == null) {
            return AlertInstanceState.UNKNOWN;
        }

        double v = currentValue;
        if (v >= enterCritical) {
            return AlertInstanceState.FIRING_CRITICAL;
        }
        if (v >= enterWarning) {
            return AlertInstanceState.FIRING_WARNING;
        }
        if (v < exitWarning) {
            return AlertInstanceState.RESOLVED;
        }
        return AlertInstanceState.PENDING;
    }

    public static AlertInstanceState evaluate(AlertRule rule, Double currentValue) {
        return evaluate(
                rule,
                currentValue,
                MetricNormalizer.DEFAULT_ENTER_WARNING_RATIO,
                MetricNormalizer.DEFAULT_EXIT_WARNING_RATIO,
                MetricNormalizer.DEFAULT_ENTER_CRITICAL_RATIO,
                MetricNormalizer.DEFAULT_EXIT_CRITICAL_RATIO);
    }

    public static AlertInstanceState applyHysteresis(
            AlertInstanceState previous,
            double ratio,
            double enterWarning,
            double exitWarning,
            double enterCritical,
            double exitCritical) {

        return switch (previous) {
            case FIRING_CRITICAL -> ratio < exitCritical ? AlertInstanceState.FIRING_WARNING : AlertInstanceState.FIRING_CRITICAL;
            case FIRING_WARNING -> {
                if (ratio >= enterCritical) {
                    yield AlertInstanceState.FIRING_CRITICAL;
                }
                if (ratio < exitWarning) {
                    yield AlertInstanceState.RESOLVED;
                }
                yield AlertInstanceState.FIRING_WARNING;
            }
            case RESOLVED, PENDING, ACKNOWLEDGED, SUPPRESSED, UNKNOWN -> {
                if (ratio >= enterCritical) {
                    yield AlertInstanceState.FIRING_CRITICAL;
                }
                if (ratio >= enterWarning) {
                    yield AlertInstanceState.FIRING_WARNING;
                }
                yield AlertInstanceState.RESOLVED;
            }
        };
    }
}
