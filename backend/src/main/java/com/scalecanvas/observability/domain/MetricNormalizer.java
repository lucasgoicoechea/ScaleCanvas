package com.scalecanvas.observability.domain;

import java.time.Instant;
import java.util.List;

public final class MetricNormalizer {

    public static final double DEFAULT_ENTER_WARNING_RATIO = 0.70;
    public static final double DEFAULT_EXIT_WARNING_RATIO = 0.65;
    public static final double DEFAULT_ENTER_CRITICAL_RATIO = 0.85;
    public static final double DEFAULT_EXIT_CRITICAL_RATIO = 0.80;

    private MetricNormalizer() {
    }

    public static DimensionSnapshot normalize(
            DimensionKey key,
            Double current,
            Double maximum,
            Unit originalUnit,
            Unit canonicalUnit,
            Instant timestamp,
            Instant staleAfter,
            List<String> reasons) {
        ResourceLifecycleState state;
        Double ratio = null;
        Double visualRatio = null;

        if (current == null) {
            state = ResourceLifecycleState.UNKNOWN;
        } else if (maximum == null || maximum <= 0) {
            state = ResourceLifecycleState.NO_CAPACITY;
        } else if (staleAfter != null && timestamp != null && timestamp.isAfter(staleAfter)) {
            state = ResourceLifecycleState.STALE;
        } else {
            ratio = current / maximum;
            visualRatio = clamp01(ratio);
            state = lifecycleStateForRatio(ratio);
        }

        return new DimensionSnapshot(
                key,
                current,
                maximum,
                ratio,
                visualRatio,
                originalUnit,
                canonicalUnit,
                state,
                timestamp,
                staleAfter,
                reasons == null ? List.of() : List.copyOf(reasons),
                List.of());
    }

    public static ResourceLifecycleState lifecycleStateForRatio(double ratio) {
        if (ratio >= DEFAULT_ENTER_CRITICAL_RATIO) {
            return ResourceLifecycleState.CRITICAL;
        }
        if (ratio >= DEFAULT_ENTER_WARNING_RATIO) {
            return ResourceLifecycleState.WARNING;
        }
        return ResourceLifecycleState.HEALTHY;
    }

    public static double clamp01(double value) {
        if (value < 0.0) {
            return 0.0;
        }
        if (value > 1.0) {
            return 1.0;
        }
        return value;
    }

    public static double overflowRatio(double ratio) {
        return Math.max(ratio - 1.0, 0.0);
    }

    public static double computeRatio(Double cpuRatio, Double gpuRatio) {
        if (cpuRatio == null && gpuRatio == null) {
            return Double.NaN;
        }
        return Math.max(
                cpuRatio == null ? Double.NEGATIVE_INFINITY : cpuRatio,
                gpuRatio == null ? Double.NEGATIVE_INFINITY : gpuRatio);
    }
}
