package com.scalecanvas.evaluation;

import com.scalecanvas.scenario.domain.ArchitectureScenario;
import com.scalecanvas.scenario.domain.DataProfile;
import com.scalecanvas.scenario.domain.DeploymentProfile;
import com.scalecanvas.scenario.domain.WorkloadProfile;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class ScenarioScaler {
    ArchitectureScenario scale(ArchitectureScenario source, ScenarioVariant variant) {
        BigDecimal factor = switch (variant) {
            case BASELINE -> BigDecimal.ONE;
            case GROWTH_X2 -> new BigDecimal("2");
            case GROWTH_X10 -> new BigDecimal("10");
        };
        if (factor.compareTo(BigDecimal.ONE) == 0) {
            return source;
        }
        WorkloadProfile workload = source.workload();
        DataProfile data = source.data();
        return new ArchitectureScenario(
                source.id(),
                source.name(),
                source.description(),
                source.productType(),
                new WorkloadProfile(
                        scaleLong(workload.registeredUsers(), factor),
                        scaleLong(workload.dailyActiveUsers(), factor),
                        scaleLong(workload.concurrentUsers(), factor),
                        workload.averageRps().multiply(factor),
                        workload.peakRps().multiply(factor),
                        workload.burstFactor(),
                        workload.readPercentage(),
                        workload.writePercentage(),
                        workload.averagePayloadBytes(),
                        workload.maximumPayloadBytes(),
                        workload.batchJobsPerDay(),
                        workload.asynchronousWorkPercentage()),
                new DataProfile(
                        data.currentStorageGb().multiply(factor),
                        data.monthlyGrowthPercentage(),
                        data.retentionMonths(),
                        data.objectStorageGb().multiply(factor),
                        data.hotDataPercentage(),
                        data.eventVolumePerDay().multiply(factor)),
                source.quality(),
                source.organization(),
                source.deployment());
    }

    private long scaleLong(long value, BigDecimal factor) {
        return factor.multiply(BigDecimal.valueOf(value)).longValueExact();
    }
}
