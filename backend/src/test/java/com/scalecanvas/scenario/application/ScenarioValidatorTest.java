package com.scalecanvas.scenario.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.scalecanvas.TestFixtures;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ScenarioValidatorTest {
    private final ScenarioValidator validator = new ScenarioValidator();

    @Test
    void rejectsReadWritePercentagesDifferentFromOneHundred() {
        var source = TestFixtures.scenarioRequest();
        var invalidWorkload = new ScenarioRequest.WorkloadRequest(
                source.workload().registeredUsers(), source.workload().dailyActiveUsers(),
                source.workload().concurrentUsers(), source.workload().averageRps(), source.workload().peakRps(),
                source.workload().burstFactor(), new BigDecimal("90"), new BigDecimal("20"),
                source.workload().averagePayloadBytes(), source.workload().maximumPayloadBytes(),
                source.workload().batchJobsPerDay(), source.workload().asynchronousWorkPercentage());
        var invalid = new ScenarioRequest(source.name(), source.description(), source.productType(), invalidWorkload,
                source.data(), source.quality(), source.organization(), source.deployment());

        assertThatThrownBy(() -> validator.validate(invalid))
                .isInstanceOf(InvalidScenarioException.class)
                .hasMessageContaining("must equal 100");
    }

    @Test
    void rejectsMicroservicesCountGreaterThanTotalServices() {
        var source = TestFixtures.scenarioRequest();
        var invalidDeployment = new ScenarioRequest.DeploymentRequest(
                source.deployment().serverType(),
                source.deployment().cloudProvider(),
                source.deployment().deploymentService(),
                source.deployment().gatewayType(),
                source.deployment().loadBalancerType(),
                source.deployment().minimumUnitMemoryMb(),
                source.deployment().minimumUnitCpuCount(),
                new ScenarioRequest.ServiceTopologyRequest(
                        2,
                        5,
                        source.deployment().serviceTopology().scalableServicesCount(),
                        source.deployment().serviceTopology().services()));
        var invalid = new ScenarioRequest(source.name(), source.description(), source.productType(), source.workload(),
                source.data(), source.quality(), source.organization(), invalidDeployment);

        assertThatThrownBy(() -> validator.validate(invalid))
                .isInstanceOf(InvalidScenarioException.class)
                .hasMessageContaining("microservicesCount must be less than or equal to totalServices");
    }
}
