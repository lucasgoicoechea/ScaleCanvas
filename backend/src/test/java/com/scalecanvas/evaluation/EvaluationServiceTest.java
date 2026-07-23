package com.scalecanvas.evaluation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scalecanvas.TestFixtures;
import com.scalecanvas.capacity.CapacityCalculator;
import com.scalecanvas.evaluation.api.EvaluationRequest;
import com.scalecanvas.evaluation.infrastructure.EvaluationEntity;
import com.scalecanvas.evaluation.infrastructure.EvaluationJpaRepository;
import com.scalecanvas.evaluation.infrastructure.EvaluationResultEntity;
import com.scalecanvas.evaluation.infrastructure.EvaluationResultJpaRepository;
import com.scalecanvas.rules.ArchitectureRule;
import com.scalecanvas.rules.AsyncQueueRule;
import com.scalecanvas.rules.CatalogVersionService;
import com.scalecanvas.rules.KubernetesRule;
import com.scalecanvas.rules.ModularMonolithDefaultRule;
import com.scalecanvas.rules.RuleCatalog;
import com.scalecanvas.observation.EvaluationMetrics;
import com.scalecanvas.scenario.application.ScenarioMapper;
import com.scalecanvas.scenario.application.ScenarioValidator;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class EvaluationServiceTest {
    @Test
    void evaluatesEveryRequestedVariant() {
        List<ArchitectureRule> rules = List.of(
                new ModularMonolithDefaultRule(), new AsyncQueueRule(), new KubernetesRule());
        EvaluationService service = new EvaluationService(
                new ScenarioMapper(), new ScenarioValidator(), new ScenarioScaler(),
                new CapacityCalculator(), new RuleCatalog(rules),
                org.mockito.Mockito.mock(EvaluationJpaRepository.class),
                org.mockito.Mockito.mock(EvaluationResultJpaRepository.class),
                jsonMapper(),
                org.mockito.Mockito.mock(EvaluationMetrics.class),
                catalogVersions("test"));

        var response = service.evaluate(new EvaluationRequest(
                null,
                TestFixtures.scenarioRequest(),
                List.of(ScenarioVariant.BASELINE, ScenarioVariant.GROWTH_X2, ScenarioVariant.GROWTH_X10)));

        assertThat(response.results()).hasSize(3);
        assertThat(response.results().get(2).derivedMetrics().dailyRequests())
                .isGreaterThan(response.results().getFirst().derivedMetrics().dailyRequests());
        assertThat(response.results().getFirst().recommendations())
                .extracting(outcome -> outcome.ruleId())
                .contains("MODULAR_MONOLITH_DEFAULT", "ASYNC_LONG_RUNNING_WORK", "KUBERNETES_OPERATIONAL_TRIGGER");
    }

    @Test
    void persistsEvaluationAndResults() {
        List<ArchitectureRule> rules = List.of(new ModularMonolithDefaultRule());
        var evaluationRepository = org.mockito.Mockito.mock(EvaluationJpaRepository.class);
        var resultRepository = org.mockito.Mockito.mock(EvaluationResultJpaRepository.class);
        when(evaluationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(resultRepository.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));
        EvaluationService service = new EvaluationService(
                new ScenarioMapper(), new ScenarioValidator(), new ScenarioScaler(),
                new CapacityCalculator(), new RuleCatalog(rules),
                evaluationRepository, resultRepository, jsonMapper(),
                org.mockito.Mockito.mock(EvaluationMetrics.class),
                catalogVersions("test"));

        UUID scenarioId = UUID.randomUUID();
        var response = service.evaluate(new EvaluationRequest(
                scenarioId,
                TestFixtures.scenarioRequest(),
                List.of(ScenarioVariant.BASELINE)));

        assertThat(response.evaluationId()).isNotNull();
        assertThat(response.catalogVersion()).isEqualTo("test");
        ArgumentCaptor<EvaluationEntity> evaluationCaptor = ArgumentCaptor.forClass(EvaluationEntity.class);
        org.mockito.Mockito.verify(evaluationRepository).save(evaluationCaptor.capture());
        assertThat(evaluationCaptor.getValue().getScenarioName()).isEqualTo("Portfolio SaaS");
        assertThat(evaluationCaptor.getValue().getScenarioId()).isEqualTo(scenarioId);
        assertThat(evaluationCaptor.getValue().getId()).isEqualTo(response.evaluationId());
        assertThat(evaluationCaptor.getValue().getPayloadJson())
                .contains(response.evaluationId().toString());
        ArgumentCaptor<EvaluationResultEntity> resultCaptor = ArgumentCaptor.forClass(EvaluationResultEntity.class);
        org.mockito.Mockito.verify(resultRepository).saveAll(org.mockito.ArgumentMatchers.anyList());
    }

    private static ObjectMapper jsonMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    private static CatalogVersionService catalogVersions(String version) {
        CatalogVersionService versions = new CatalogVersionService();
        versions.register("test", version, "Test catalog", "test", true);
        return versions;
    }
}
