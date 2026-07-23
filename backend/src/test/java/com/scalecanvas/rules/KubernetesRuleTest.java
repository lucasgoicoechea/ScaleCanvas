package com.scalecanvas.rules;

import static org.assertj.core.api.Assertions.assertThat;

import com.scalecanvas.TestFixtures;
import com.scalecanvas.capacity.CapacityCalculator;
import com.scalecanvas.scenario.application.ScenarioMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class KubernetesRuleTest {
    @Test
    void advisesAgainstKubernetesForSmallMediumMaturityTeam() {
        var scenario = new ScenarioMapper().toDomain(UUID.randomUUID(), TestFixtures.scenarioRequest());
        var context = new EvaluationContext(scenario, new CapacityCalculator().calculate(scenario));

        RuleOutcome outcome = new KubernetesRule().evaluate(context);

        assertThat(outcome.status()).isEqualTo(RuleStatus.TRIGGERED);
        assertThat(outcome.urgency()).isEqualTo(Urgency.NOT_YET);
        assertThat(outcome.action()).isEqualTo(RecommendationAction.AVOID_FOR_NOW);
    }
}
