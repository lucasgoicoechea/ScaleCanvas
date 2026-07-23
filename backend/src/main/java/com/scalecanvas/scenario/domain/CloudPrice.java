package com.scalecanvas.scenario.domain;

import java.math.BigDecimal;

public record CloudPrice(
        CloudProvider provider,
        String serviceType,
        String region,
        BigDecimal unitMonthlyCost) {
}
