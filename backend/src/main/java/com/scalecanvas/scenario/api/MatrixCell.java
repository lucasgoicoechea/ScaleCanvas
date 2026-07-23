package com.scalecanvas.scenario.api;

public record MatrixCell(
        String serviceName,
        int serverIndex,
        double utilizationPercent,
        long requestsPerMinuteShare,
        int replicas) {
}
