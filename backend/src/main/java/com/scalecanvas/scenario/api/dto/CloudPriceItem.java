package com.scalecanvas.scenario.api.dto;

import java.math.BigDecimal;

public record CloudPriceItem(
        String serviceName,
        String cloudProvider,
        String serviceType,
        String region,
        BigDecimal unitMonthlyCost,
        BigDecimal quantity,
        BigDecimal monthlySubtotal,
        BigDecimal yearlySubtotal,
        String driver) {
}
