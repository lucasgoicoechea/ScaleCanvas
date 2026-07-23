package com.scalecanvas.capacity;

import static org.assertj.core.api.Assertions.assertThat;

import com.scalecanvas.TestFixtures;
import com.scalecanvas.scenario.application.ScenarioMapper;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CapacityCalculatorTest {
    private final CapacityCalculator calculator = new CapacityCalculator();
    private final ScenarioMapper mapper = new ScenarioMapper();

    @Test
    void calculatesTransparentDerivedMetrics() {
        var scenario = mapper.toDomain(UUID.randomUUID(), TestFixtures.scenarioRequest());

        DerivedMetrics metrics = calculator.calculate(scenario);

        assertThat(metrics.dailyRequests()).isEqualByComparingTo(new BigDecimal("10368000"));
        assertThat(metrics.peakHourRequests()).isEqualByComparingTo(new BigDecimal("2880000"));
        assertThat(metrics.readRps()).isEqualByComparingTo(new BigDecimal("680.00"));
        assertThat(metrics.writeRps()).isEqualByComparingTo(new BigDecimal("120.00"));
        assertThat(metrics.storageAfter12MonthsGb()).isGreaterThan(new BigDecimal("300"));
        assertThat(metrics.allowedUnavailabilityMinutesPerMonth()).isEqualByComparingTo(new BigDecimal("43.20"));
    }

    @Test
    void compoundGrowthKeepsCurrentValueAtZeroGrowth() {
        assertThat(calculator.compoundGrowth(new BigDecimal("100"), BigDecimal.ZERO, 12))
                .isEqualByComparingTo(new BigDecimal("100"));
    }
}
