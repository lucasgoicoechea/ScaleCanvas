package com.scalecanvas.rules;

import static org.assertj.core.api.Assertions.assertThat;

import com.scalecanvas.TestFixtures;
import com.scalecanvas.capacity.CapacityCalculator;
import com.scalecanvas.scenario.application.ScenarioMapper;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ContainerMinUnitRuleTest {
    @Test
    void triggersForSmallContainerUnit() {
        var base = TestFixtures.scenarioRequest();
        var deployment = new ScenarioRequest.DeploymentRequest(
                com.scalecanvas.scenario.domain.ServerType.CONTAINER,
                base.deployment().cloudProvider(),
                base.deployment().deploymentService(),
                base.deployment().gatewayType(),
                base.deployment().loadBalancerType(),
                1024,
                1,
                base.deployment().serviceTopology());
        var request = new ScenarioRequest(base.name(), base.description(), base.productType(), base.workload(),
                base.data(), base.quality(), base.organization(), deployment);
        var scenario = new ScenarioMapper().toDomain(UUID.randomUUID(), request);
        var context = new EvaluationContext(scenario, new CapacityCalculator().calculate(scenario));

        RuleOutcome outcome = new ContainerMinUnitRule().evaluate(context);

        assertThat(outcome.status()).isEqualTo(RuleStatus.TRIGGERED);
        assertThat(outcome.urgency()).isEqualTo(Urgency.WATCH);
        assertThat(outcome.action()).isEqualTo(RecommendationAction.CONSIDER);
    }
}
