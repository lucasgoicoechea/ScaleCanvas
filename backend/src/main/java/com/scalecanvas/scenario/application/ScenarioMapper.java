package com.scalecanvas.scenario.application;

import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import com.scalecanvas.scenario.domain.ArchitectureScenario;
import com.scalecanvas.scenario.domain.DataProfile;
import com.scalecanvas.scenario.domain.DeploymentProfile;
import com.scalecanvas.scenario.domain.OrganizationProfile;
import com.scalecanvas.scenario.domain.QualityProfile;
import com.scalecanvas.scenario.domain.ServiceCapacity;
import com.scalecanvas.scenario.domain.ServiceTopology;
import com.scalecanvas.scenario.domain.WorkloadProfile;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ScenarioMapper {
    public ArchitectureScenario toDomain(UUID id, ScenarioRequest request) {
        return new ArchitectureScenario(
                id,
                request.name(),
                request.description(),
                request.productType(),
                new WorkloadProfile(
                        request.workload().registeredUsers(),
                        request.workload().dailyActiveUsers(),
                        request.workload().concurrentUsers(),
                        request.workload().averageRps(),
                        request.workload().peakRps(),
                        request.workload().burstFactor(),
                        request.workload().readPercentage(),
                        request.workload().writePercentage(),
                        request.workload().averagePayloadBytes(),
                        request.workload().maximumPayloadBytes(),
                        request.workload().batchJobsPerDay(),
                        request.workload().asynchronousWorkPercentage()),
                new DataProfile(
                        request.data().currentStorageGb(),
                        request.data().monthlyGrowthPercentage(),
                        request.data().retentionMonths(),
                        request.data().objectStorageGb(),
                        request.data().hotDataPercentage(),
                        request.data().eventVolumePerDay()),
                new QualityProfile(
                        request.quality().targetP50Ms(),
                        request.quality().targetP95Ms(),
                        request.quality().targetP99Ms(),
                        request.quality().availabilitySloPercent(),
                        request.quality().rtoMinutes(),
                        request.quality().rpoMinutes(),
                        request.quality().consistencyLevel(),
                        request.quality().geographicScope()),
                new OrganizationProfile(
                        request.organization().teamSize(),
                        request.organization().operationsMaturity(),
                        request.organization().deploymentFrequencyPerWeek(),
                        request.organization().onCallAvailable(),
                        request.organization().observabilityMaturity(),
                        request.organization().cloudExperience(),
                        request.organization().budgetBand()),
                new DeploymentProfile(
                        request.deployment().serverType(),
                        request.deployment().cloudProvider(),
                        request.deployment().deploymentService(),
                        request.deployment().gatewayType(),
                        request.deployment().loadBalancerType(),
                        request.deployment().minimumUnitMemoryMb(),
                        request.deployment().minimumUnitCpuCount(),
                        new ServiceTopology(
                                request.deployment().serviceTopology().totalServices(),
                                request.deployment().serviceTopology().microservicesCount(),
                                request.deployment().serviceTopology().scalableServicesCount(),
                                request.deployment().serviceTopology().services().stream()
                                        .map(s -> new ServiceCapacity(
                                                s.serviceName(),
                                                s.requestsPerMinute(),
                                                s.memoryMb(),
                                                s.cpuCount(),
                                                s.replicas(),
                                                s.serverBinding()))
                                        .toList())));
    }
}
