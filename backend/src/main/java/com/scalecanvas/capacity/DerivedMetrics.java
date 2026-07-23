package com.scalecanvas.capacity;

import java.math.BigDecimal;

public record DerivedMetrics(
        BigDecimal dailyRequests,
        BigDecimal peakHourRequests,
        BigDecimal dailyTransferGb,
        BigDecimal readRps,
        BigDecimal writeRps,
        BigDecimal storageAfter12MonthsGb,
        BigDecimal allowedUnavailabilityMinutesPerMonth) {
}
