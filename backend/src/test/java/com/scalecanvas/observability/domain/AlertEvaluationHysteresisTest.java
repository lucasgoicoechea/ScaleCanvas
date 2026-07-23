package com.scalecanvas.observability.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AlertEvaluationHysteresisTest {

    private final AlertRule rule = new AlertRule(
            "rule-1", "cpu", "host", DimensionKey.CPU, "GTE",
            0.70, 0.85, "avg", "PT5M", 1,
            "PT5M", "PT5M", "PT5M", "UNKNOWN", true, java.util.List.of());

    @Test
    void entersWarningAtThreshold() {
        assertEquals(AlertInstanceState.FIRING_WARNING, AlertEvaluator.evaluate(rule, 0.72));
    }

    @Test
    void entersCriticalAtThreshold() {
        assertEquals(AlertInstanceState.FIRING_CRITICAL, AlertEvaluator.evaluate(rule, 0.90));
    }

    @Test
    void resolvedBelowExitWarning() {
        assertEquals(AlertInstanceState.RESOLVED, AlertEvaluator.evaluate(rule, 0.60));
    }

    @Test
    void unknownOnNullValue() {
        assertEquals(AlertInstanceState.UNKNOWN, AlertEvaluator.evaluate(rule, null));
    }

    @Test
    void hysteresisKeepsCriticalUntilExitCritical() {
        AlertInstanceState state = AlertEvaluator.applyHysteresis(
                AlertInstanceState.FIRING_CRITICAL, 0.82,
                0.70, 0.65, 0.85, 0.80);
        assertEquals(AlertInstanceState.FIRING_CRITICAL, state);
    }

    @Test
    void hysteresisLeavesCriticalAtExitCritical() {
        AlertInstanceState state = AlertEvaluator.applyHysteresis(
                AlertInstanceState.FIRING_CRITICAL, 0.79,
                0.70, 0.65, 0.85, 0.80);
        assertEquals(AlertInstanceState.FIRING_WARNING, state);
    }

    @Test
    void hysteresisStaysCriticalAboveExitCritical() {
        AlertInstanceState state = AlertEvaluator.applyHysteresis(
                AlertInstanceState.FIRING_CRITICAL, 0.87,
                0.70, 0.65, 0.85, 0.80);
        assertEquals(AlertInstanceState.FIRING_CRITICAL, state);
    }

    @Test
    void hysteresisKeepsWarningUntilExitWarning() {
        AlertInstanceState state = AlertEvaluator.applyHysteresis(
                AlertInstanceState.FIRING_WARNING, 0.62,
                0.70, 0.65, 0.85, 0.80);
        assertEquals(AlertInstanceState.RESOLVED, state);
    }

    @Test
    void hysteresisStaysWarningBetweenThresholds() {
        AlertInstanceState state = AlertEvaluator.applyHysteresis(
                AlertInstanceState.FIRING_WARNING, 0.68,
                0.70, 0.65, 0.85, 0.80);
        assertEquals(AlertInstanceState.FIRING_WARNING, state);
    }

    @Test
    void hysteresisElevatesWarningToCritical() {
        AlertInstanceState state = AlertEvaluator.applyHysteresis(
                AlertInstanceState.FIRING_WARNING, 0.90,
                0.70, 0.65, 0.85, 0.80);
        assertEquals(AlertInstanceState.FIRING_CRITICAL, state);
    }
}
