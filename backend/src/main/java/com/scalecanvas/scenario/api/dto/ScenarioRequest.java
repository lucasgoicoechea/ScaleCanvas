package com.scalecanvas.scenario.api.dto;

import com.scalecanvas.scenario.domain.BudgetBand;
import com.scalecanvas.scenario.domain.CloudProvider;
import com.scalecanvas.scenario.domain.ConsistencyLevel;
import com.scalecanvas.scenario.domain.DeploymentService;
import com.scalecanvas.scenario.domain.GatewayType;
import com.scalecanvas.scenario.domain.GeographicScope;
import com.scalecanvas.scenario.domain.LoadBalancerType;
import com.scalecanvas.scenario.domain.MaturityLevel;
import com.scalecanvas.scenario.domain.ProductType;
import com.scalecanvas.scenario.domain.ServerType;
import com.scalecanvas.scenario.domain.ServiceBinding;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ScenarioRequest(
        @NotBlank @Size(max = 160) String name,
        @Size(max = 2000) String description,
        @NotNull ProductType productType,
        @NotNull @Valid WorkloadRequest workload,
        @NotNull @Valid DataRequest data,
        @NotNull @Valid QualityRequest quality,
        @NotNull @Valid OrganizationRequest organization,
        @NotNull @Valid DeploymentRequest deployment) {

    public record WorkloadRequest(
            @Min(0) long registeredUsers,
            @Min(0) long dailyActiveUsers,
            @Min(0) long concurrentUsers,
            @NotNull @DecimalMin("0.0") BigDecimal averageRps,
            @NotNull @DecimalMin("0.0") BigDecimal peakRps,
            @NotNull @DecimalMin("1.0") BigDecimal burstFactor,
            @NotNull @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal readPercentage,
            @NotNull @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal writePercentage,
            @Min(0) long averagePayloadBytes,
            @Min(0) long maximumPayloadBytes,
            @Min(0) int batchJobsPerDay,
            @NotNull @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal asynchronousWorkPercentage) {
    }

    public record DataRequest(
            @NotNull @DecimalMin("0.0") BigDecimal currentStorageGb,
            @NotNull @DecimalMin("-99.99") BigDecimal monthlyGrowthPercentage,
            @Min(1) @Max(1200) int retentionMonths,
            @NotNull @DecimalMin("0.0") BigDecimal objectStorageGb,
            @NotNull @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal hotDataPercentage,
            @NotNull @DecimalMin("0.0") BigDecimal eventVolumePerDay) {
    }

    public record QualityRequest(
            @Min(1) int targetP50Ms,
            @Min(1) int targetP95Ms,
            @Min(1) int targetP99Ms,
            @NotNull @DecimalMin("90.0") @DecimalMax("100.0") BigDecimal availabilitySloPercent,
            @Min(0) int rtoMinutes,
            @Min(0) int rpoMinutes,
            @NotNull ConsistencyLevel consistencyLevel,
            @NotNull GeographicScope geographicScope) {
    }

    public record OrganizationRequest(
            @Min(1) @Max(10000) int teamSize,
            @NotNull MaturityLevel operationsMaturity,
            @Min(0) int deploymentFrequencyPerWeek,
            boolean onCallAvailable,
            @NotNull MaturityLevel observabilityMaturity,
            @NotNull MaturityLevel cloudExperience,
            @NotNull BudgetBand budgetBand) {
    }

    public record DeploymentRequest(
            @NotNull ServerType serverType,
            @NotNull CloudProvider cloudProvider,
            @NotNull DeploymentService deploymentService,
            @NotNull GatewayType gatewayType,
            @NotNull LoadBalancerType loadBalancerType,
            @Min(0) int minimumUnitMemoryMb,
            @Min(0) int minimumUnitCpuCount,
            @NotNull @Valid ServiceTopologyRequest serviceTopology) {
    }

    public record ServiceTopologyRequest(
            @Min(0) int totalServices,
            @Min(0) int microservicesCount,
            @Min(0) int scalableServicesCount,
            @NotNull @Valid java.util.List<ServiceCapacityRequest> services) {
    }

    public record ServiceCapacityRequest(
            @NotBlank String serviceName,
            @Min(0) long requestsPerMinute,
            @Min(0) int memoryMb,
            @Min(0) int cpuCount,
            @Min(1) int replicas,
            @NotNull ServiceBinding serverBinding) {
    }
}
