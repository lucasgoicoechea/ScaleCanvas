package com.scalecanvas.scenario.domain;

import java.math.BigDecimal;

public record DataProfile(
        BigDecimal currentStorageGb,
        BigDecimal monthlyGrowthPercentage,
        int retentionMonths,
        BigDecimal objectStorageGb,
        BigDecimal hotDataPercentage,
        BigDecimal eventVolumePerDay) {
}
