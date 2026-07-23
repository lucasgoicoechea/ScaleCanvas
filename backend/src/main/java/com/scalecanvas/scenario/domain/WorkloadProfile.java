package com.scalecanvas.scenario.domain;

import java.math.BigDecimal;

public record WorkloadProfile(
        long registeredUsers,
        long dailyActiveUsers,
        long concurrentUsers,
        BigDecimal averageRps,
        BigDecimal peakRps,
        BigDecimal burstFactor,
        BigDecimal readPercentage,
        BigDecimal writePercentage,
        long averagePayloadBytes,
        long maximumPayloadBytes,
        int batchJobsPerDay,
        BigDecimal asynchronousWorkPercentage) {
}
