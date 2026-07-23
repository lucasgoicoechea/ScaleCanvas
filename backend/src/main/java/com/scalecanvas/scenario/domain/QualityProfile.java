package com.scalecanvas.scenario.domain;

import java.math.BigDecimal;

public record QualityProfile(
        int targetP50Ms,
        int targetP95Ms,
        int targetP99Ms,
        BigDecimal availabilitySloPercent,
        int rtoMinutes,
        int rpoMinutes,
        ConsistencyLevel consistencyLevel,
        GeographicScope geographicScope) {
}
