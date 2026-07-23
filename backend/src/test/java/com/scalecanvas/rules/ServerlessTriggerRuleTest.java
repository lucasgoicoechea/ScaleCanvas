package com.scalecanvas.rules;

import static org.assertj.core.api.Assertions.assertThat;

import com.scalecanvas.TestFixtures;
import com.scalecanvas.capacity.CapacityCalculator;
import com.scalecanvas.scenario.application.ScenarioMapper;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ServerlessTriggerRuleTest {
    @Test
    void triggersForLowTrafficServerlessTopology() {
        var base = TestFixtures.scenarioRequest();
        var services = java.util.List.of(
                new ScenarioRequest.ServiceCapacityRequest("svc-a", 10, 256, 1, 1, com.scalecanvas.scenario.domain.ServiceBinding.SHARED),
                new ScenarioRequest.ServiceCapacityRequest("svc-b", 20, 256, 1, 1, com.scalecanvas.scenario.domain.ServiceBinding.SHARED));
        var topology = new ScenarioRequest.ServiceTopologyRequest(2, 2, 2, services);
        var deployment = new ScenarioRequest.DeploymentRequest(
                com.scalecanvas.scenario.domain.ServerType.SERVERLESS,
                base.deployment().cloudProvider(),
                com.scalecanvas.scenario.domain.DeploymentService.LAMBDA,
                base.deployment().gatewayType(),
                base.deployment().loadBalancerType(),
                base.deployment().minimumUnitMemoryMb(),
                base.deployment().minimumUnitCpuCount(),
                topology);
        var request = new ScenarioRequest(base.name(), base.description(), base.productType(), base.workload(),
                base.data(), base.quality(), base.organization(), deployment);
        var scenario = new ScenarioMapper().toDomain(UUID.randomUUID(), request);
        var context = new EvaluationContext(scenario, new CapacityCalculator().calculate(scenario));

        RuleOutcome outcome = new ServerlessTriggerRule().evaluate(context);

        assertThat(outcome.status()).isEqualTo(RuleStatus.TRIGGERED);
        assertThat(outcome.urgency()).isEqualTo(Urgency.WATCH);
        assertThat(outcome.action()).isEqualTo(RecommendationAction.CONSIDER);
    }
}
