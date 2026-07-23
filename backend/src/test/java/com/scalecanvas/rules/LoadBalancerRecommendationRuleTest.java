package com.scalecanvas.rules;

import static org.assertj.core.api.Assertions.assertThat;

import com.scalecanvas.TestFixtures;
import com.scalecanvas.capacity.CapacityCalculator;
import com.scalecanvas.scenario.application.ScenarioMapper;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LoadBalancerRecommendationRuleTest {
    @Test
    void triggersWhenReplicasExistWithoutLoadBalancer() {
        var base = TestFixtures.scenarioRequest();
        var services = java.util.List.of(
                new ScenarioRequest.ServiceCapacityRequest("api", 100, 512, 1, 3, com.scalecanvas.scenario.domain.ServiceBinding.SHARED));
        var topology = new ScenarioRequest.ServiceTopologyRequest(1, 0, 1, services);
        var deployment = new ScenarioRequest.DeploymentRequest(
                base.deployment().serverType(),
                base.deployment().cloudProvider(),
                base.deployment().deploymentService(),
                base.deployment().gatewayType(),
                com.scalecanvas.scenario.domain.LoadBalancerType.NONE,
                base.deployment().minimumUnitMemoryMb(),
                base.deployment().minimumUnitCpuCount(),
                topology);
        var request = new ScenarioRequest(base.name(), base.description(), base.productType(), base.workload(),
                base.data(), base.quality(), base.organization(), deployment);
        var scenario = new ScenarioMapper().toDomain(UUID.randomUUID(), request);
        var context = new EvaluationContext(scenario, new CapacityCalculator().calculate(scenario));

        RuleOutcome outcome = new LoadBalancerRecommendationRule().evaluate(context);

        assertThat(outcome.status()).isEqualTo(RuleStatus.TRIGGERED);
        assertThat(outcome.urgency()).isEqualTo(Urgency.DO_NOW);
        assertThat(outcome.action()).isEqualTo(RecommendationAction.USE);
    }
}
