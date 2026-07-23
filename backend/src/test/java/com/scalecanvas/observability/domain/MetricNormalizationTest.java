package com.scalecanvas.observability.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class MetricNormalizationTest {

    private final Instant ts = Instant.parse("2026-07-19T15:00:00Z");

    @Test
    void ratioComputedAndClamped() {
        var snap = MetricNormalizer.normalize(DimensionKey.CPU, 3.0, 4.0, Unit.CORE, Unit.CORE,
                ts, null, List.of());
        assertEquals(0.75, snap.ratio(), 1e-9);
        assertEquals(0.75, snap.visualRatio(), 1e-9);
        assertEquals(ResourceLifecycleState.WARNING, snap.state());
    }

    @Test
    void noCapacityWhenMaximumIsNull() {
        var snap = MetricNormalizer.normalize(DimensionKey.CPU, 3.0, null, Unit.CORE, Unit.CORE,
                ts, null, List.of());
        assertNull(snap.ratio());
        assertNull(snap.visualRatio());
        assertEquals(ResourceLifecycleState.NO_CAPACITY, snap.state());
    }

    @Test
    void noCapacityWhenMaximumNonPositive() {
        var snap = MetricNormalizer.normalize(DimensionKey.CPU, 3.0, 0.0, Unit.CORE, Unit.CORE,
                ts, null, List.of());
        assertEquals(ResourceLifecycleState.NO_CAPACITY, snap.state());
        assertNull(snap.ratio());
    }

    @Test
    void unknownWhenCurrentIsNull() {
        var snap = MetricNormalizer.normalize(DimensionKey.CPU, null, 4.0, Unit.CORE, Unit.CORE,
                ts, null, List.of());
        assertEquals(ResourceLifecycleState.UNKNOWN, snap.state());
    }

    @Test
    void staleWhenTimestampAfterStaleBoundary() {
        var staleAfter = Instant.parse("2026-07-19T14:00:00Z");
        var snap = MetricNormalizer.normalize(DimensionKey.CPU, 3.0, 4.0, Unit.CORE, Unit.CORE,
                ts, staleAfter, List.of());
        assertEquals(ResourceLifecycleState.STALE, snap.state());
    }

    @Test
    void overflowRatioAndClamp() {
        assertEquals(0.2, MetricNormalizer.overflowRatio(1.2), 1e-9);
        assertEquals(0.0, MetricNormalizer.overflowRatio(0.5), 1e-9);
        assertEquals(1.0, MetricNormalizer.clamp01(1.5), 1e-9);
        assertEquals(0.0, MetricNormalizer.clamp01(-0.2), 1e-9);
    }

    @Test
    void cpuAndGpuNotSummed() {
        double compute = MetricNormalizer.computeRatio(0.4, 0.9);
        assertTrue(compute > 0.4);
        assertEquals(0.9, compute, 1e-9);
    }
}
