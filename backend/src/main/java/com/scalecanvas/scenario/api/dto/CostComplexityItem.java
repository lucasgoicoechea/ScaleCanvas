package com.scalecanvas.scenario.api.dto;

import java.math.BigDecimal;

public record CostComplexityItem(
        String serviceName,
        BigDecimal estimatedMonthlyCost,
        int complexityScore,
        String driver) {
}
