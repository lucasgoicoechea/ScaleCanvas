package com.scalecanvas.capacity;

import com.scalecanvas.scenario.domain.ArchitectureScenario;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class CapacityCalculator {
    private static final BigDecimal SECONDS_PER_DAY = new BigDecimal("86400");
    private static final BigDecimal SECONDS_PER_HOUR = new BigDecimal("3600");
    private static final BigDecimal BYTES_PER_GB = new BigDecimal("1073741824");
    private static final BigDecimal MINUTES_PER_MONTH = new BigDecimal("43200");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_UP);

    public DerivedMetrics calculate(ArchitectureScenario scenario) {
        var workload = scenario.workload();
        var data = scenario.data();
        var quality = scenario.quality();

        BigDecimal dailyRequests = workload.averageRps().multiply(SECONDS_PER_DAY, MC);
        BigDecimal peakHourRequests = workload.peakRps().multiply(SECONDS_PER_HOUR, MC);
        BigDecimal dailyTransferGb = dailyRequests
                .multiply(BigDecimal.valueOf(workload.averagePayloadBytes()), MC)
                .divide(BYTES_PER_GB, 4, RoundingMode.HALF_UP);
        BigDecimal readRps = percentageOf(workload.peakRps(), workload.readPercentage());
        BigDecimal writeRps = percentageOf(workload.peakRps(), workload.writePercentage());
        BigDecimal storageAfter12Months = compoundGrowth(
                data.currentStorageGb(),
                data.monthlyGrowthPercentage(),
                12);
        BigDecimal allowedUnavailability = MINUTES_PER_MONTH.multiply(
                BigDecimal.ONE.subtract(
                        quality.availabilitySloPercent().divide(ONE_HUNDRED, 8, RoundingMode.HALF_UP)),
                MC).setScale(2, RoundingMode.HALF_UP);

        return new DerivedMetrics(
                dailyRequests.setScale(0, RoundingMode.HALF_UP),
                peakHourRequests.setScale(0, RoundingMode.HALF_UP),
                dailyTransferGb,
                readRps.setScale(2, RoundingMode.HALF_UP),
                writeRps.setScale(2, RoundingMode.HALF_UP),
                storageAfter12Months.setScale(2, RoundingMode.HALF_UP),
                allowedUnavailability);
    }

    BigDecimal compoundGrowth(BigDecimal current, BigDecimal monthlyPercentage, int months) {
        BigDecimal rate = BigDecimal.ONE.add(monthlyPercentage.divide(ONE_HUNDRED, 12, RoundingMode.HALF_UP));
        return current.multiply(rate.pow(months, MC), MC);
    }

    private BigDecimal percentageOf(BigDecimal value, BigDecimal percentage) {
        return value.multiply(percentage, MC).divide(ONE_HUNDRED, 8, RoundingMode.HALF_UP);
    }
}
