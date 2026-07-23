package com.scalecanvas.scenario.domain;

public record ServiceCapacity(
        String serviceName,
        long requestsPerMinute,
        int memoryMb,
        int cpuCount,
        int replicas,
        ServiceBinding serverBinding) {
}
