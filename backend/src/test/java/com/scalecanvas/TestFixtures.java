package com.scalecanvas;

import com.scalecanvas.scenario.api.dto.ScenarioRequest;
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
import java.math.BigDecimal;

public final class TestFixtures {
    private TestFixtures() {}

    public static ScenarioRequest scenarioRequest() {
        return new ScenarioRequest(
                "Portfolio SaaS",
                "Synthetic test scenario",
                ProductType.SAAS_B2B,
                new ScenarioRequest.WorkloadRequest(
                        100_000, 20_000, 1_000,
                        new BigDecimal("120"), new BigDecimal("800"), new BigDecimal("6.67"),
                        new BigDecimal("85"), new BigDecimal("15"),
                        4096, 2_000_000, 4, new BigDecimal("20")),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("120"), new BigDecimal("8"), 36,
                        new BigDecimal("60"), new BigDecimal("30"), new BigDecimal("500000")),
                new ScenarioRequest.QualityRequest(
                        80, 250, 700, new BigDecimal("99.9"), 60, 15,
                        ConsistencyLevel.READ_YOUR_WRITES, GeographicScope.MULTI_COUNTRY),
                new ScenarioRequest.OrganizationRequest(
                        8, MaturityLevel.MEDIUM, 5, true,
                        MaturityLevel.MEDIUM, MaturityLevel.MEDIUM, BudgetBand.MODERATE),
                new ScenarioRequest.DeploymentRequest(
                        ServerType.CONTAINER,
                        CloudProvider.AWS,
                        DeploymentService.EKS,
                        GatewayType.ALB,
                        LoadBalancerType.NLB,
                        4096,
                        2,
                        new ScenarioRequest.ServiceTopologyRequest(
                                6,
                                3,
                                3,
                                java.util.List.of(
                                        new ScenarioRequest.ServiceCapacityRequest(
                                                "api", 1200, 2048, 1, 3, ServiceBinding.SHARED),
                                        new ScenarioRequest.ServiceCapacityRequest(
                                                "worker", 400, 1024, 1, 2, ServiceBinding.SHARED)))));
    }
}
