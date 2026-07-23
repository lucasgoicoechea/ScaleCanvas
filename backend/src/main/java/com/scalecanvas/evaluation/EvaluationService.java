package com.scalecanvas.evaluation;

import com.scalecanvas.capacity.CapacityCalculator;
import com.scalecanvas.evaluation.api.EvaluationRequest;
import com.scalecanvas.evaluation.api.EvaluationResponse;
import com.scalecanvas.evaluation.api.EvaluationSummary;
import com.scalecanvas.evaluation.api.RiskFinding;
import com.scalecanvas.evaluation.api.VariantResult;
import com.scalecanvas.evaluation.api.dto.EvaluationTimelineItem;
import com.scalecanvas.evaluation.infrastructure.EvaluationEntity;
import com.scalecanvas.evaluation.infrastructure.EvaluationJpaRepository;
import com.scalecanvas.evaluation.infrastructure.EvaluationResultEntity;
import com.scalecanvas.evaluation.infrastructure.EvaluationResultJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalecanvas.rules.EvaluationContext;
import com.scalecanvas.rules.CatalogVersionService;
import com.scalecanvas.rules.RuleCatalog;
import com.scalecanvas.rules.RuleOutcome;
import com.scalecanvas.rules.Urgency;
import com.scalecanvas.observation.EvaluationMetrics;
import com.scalecanvas.scenario.application.ScenarioMapper;
import com.scalecanvas.scenario.application.ScenarioValidator;
import com.scalecanvas.scenario.domain.ArchitectureScenario;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvaluationService {
    private final ScenarioMapper mapper;
    private final ScenarioValidator validator;
    private final ScenarioScaler scaler;
    private final CapacityCalculator calculator;
    private final RuleCatalog catalog;
    private final EvaluationJpaRepository evaluationRepository;
    private final EvaluationResultJpaRepository resultRepository;
    private final ObjectMapper objectMapper;
    private final EvaluationMetrics metrics;
    private final CatalogVersionService catalogVersionService;

    public EvaluationService(
            ScenarioMapper mapper,
            ScenarioValidator validator,
            ScenarioScaler scaler,
            CapacityCalculator calculator,
            RuleCatalog catalog,
            EvaluationJpaRepository evaluationRepository,
            EvaluationResultJpaRepository resultRepository,
            ObjectMapper objectMapper,
            EvaluationMetrics metrics,
            CatalogVersionService catalogVersionService) {
        this.mapper = mapper;
        this.validator = validator;
        this.scaler = scaler;
        this.calculator = calculator;
        this.catalog = catalog;
        this.evaluationRepository = evaluationRepository;
        this.resultRepository = resultRepository;
        this.objectMapper = objectMapper;
        this.metrics = metrics;
        this.catalogVersionService = catalogVersionService;
    }

    @Transactional
    public EvaluationResponse evaluate(EvaluationRequest request) {
        validator.validate(request.scenario());
        ArchitectureScenario base = mapper.toDomain(UUID.randomUUID(), request.scenario());
        List<VariantResult> results = request.variants().stream()
                .distinct()
                .map(variant -> evaluateVariant(scaler.scale(base, variant), variant))
                .toList();

        metrics.incrementEvaluation();
        for (VariantResult result : results) {
            metrics.incrementRecommendation(result.recommendations().size());
        }

        UUID evaluationId = UUID.randomUUID();
        UUID scenarioId = request.scenarioId() == null ? UUID.randomUUID() : request.scenarioId();
        Instant now = Instant.now();
        String catalogVersion = catalogVersionService.activeVersion().version();
        EvaluationResponse response = new EvaluationResponse(
                evaluationId,
                request.scenario().name(),
                catalogVersion,
                now,
                results);
        String payload = writeEvaluationPayload(response);
        EvaluationEntity evaluationEntity = new EvaluationEntity(
                evaluationId,
                scenarioId,
                request.scenario().name(),
                catalogVersion,
                now,
                payload);
        evaluationRepository.save(evaluationEntity);

        List<EvaluationResultEntity> resultEntities = new ArrayList<>();
        for (VariantResult result : results) {
            resultEntities.add(new EvaluationResultEntity(
                    UUID.randomUUID(),
                    evaluationId,
                    result.variant().name(),
                    writeVariantPayload(result),
                    now
            ));
        }
        resultRepository.saveAll(resultEntities);

        return response;
    }

    @Transactional(readOnly = true)
    public EvaluationResponse getEvaluation(UUID id) {
        EvaluationEntity entity = evaluationRepository.findById(id)
                .orElseThrow(() -> new EvaluationNotFoundException(id));
        try {
            return objectMapper.readValue(entity.getPayloadJson(), EvaluationResponse.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Stored evaluation cannot be read", exception);
        }
    }

    @Transactional(readOnly = true)
    public List<EvaluationSummary> listEvaluations() {
        return evaluationRepository.findAll().stream()
                .map(entity -> new EvaluationSummary(
                        entity.getId(),
                        entity.getScenarioId(),
                        entity.getScenarioName(),
                        entity.getCatalogVersion(),
                        entity.getGeneratedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EvaluationTimelineItem> getEvaluationTimeline(UUID scenarioId) {
        return evaluationRepository.findByScenarioIdOrderByGeneratedAtDesc(scenarioId).stream()
                .map(entity -> {
                    try {
                        EvaluationResponse response = objectMapper.readValue(entity.getPayloadJson(), EvaluationResponse.class);
                        return response.results().stream()
                                .map(variantResult -> new EvaluationTimelineItem(
                                        entity.getId(),
                                        entity.getScenarioId(),
                                        entity.getScenarioName(),
                                        entity.getCatalogVersion(),
                                        entity.getGeneratedAt(),
                                        variantResult.variant().name(),
                                        variantResult.derivedMetrics().peakHourRequests(),
                                        variantResult.derivedMetrics().storageAfter12MonthsGb(),
                                        variantResult.derivedMetrics().allowedUnavailabilityMinutesPerMonth(),
                                        variantResult.recommendations().size()))
                                .toList();
                    } catch (Exception exception) {
                        throw new IllegalStateException("Cannot read evaluation payload", exception);
                    }
                })
                .flatMap(List::stream)
                .toList();
    }

    private String writeEvaluationPayload(EvaluationResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Evaluation cannot be serialized", exception);
        }
    }

    private String writeVariantPayload(VariantResult result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Variant result cannot be serialized", exception);
        }
    }

    private VariantResult evaluateVariant(ArchitectureScenario scenario, ScenarioVariant variant) {
        var metrics = calculator.calculate(scenario);
        var context = new EvaluationContext(scenario, metrics);
        List<RuleOutcome> outcomes = catalog.rules().stream()
                .map(rule -> rule.evaluate(context))
                .filter(outcome -> outcome.status() != com.scalecanvas.rules.RuleStatus.NOT_TRIGGERED)
                .sorted(Comparator.comparing(RuleOutcome::urgency).thenComparing(RuleOutcome::ruleId))
                .toList();
        return new VariantResult(variant, metrics, outcomes, deriveRisks(scenario, metrics, outcomes));
    }

    private List<RiskFinding> deriveRisks(
            ArchitectureScenario scenario,
            com.scalecanvas.capacity.DerivedMetrics metrics,
            List<RuleOutcome> outcomes) {
        List<RiskFinding> risks = new ArrayList<>();
        if (scenario.quality().availabilitySloPercent().compareTo(new java.math.BigDecimal("99.95")) >= 0
                && !scenario.organization().onCallAvailable()) {
            risks.add(new RiskFinding("HIGH", "No on-call coverage",
                    "The availability target is incompatible with the current support model."));
        }
        if (scenario.quality().rpoMinutes() == 0
                && scenario.organization().operationsMaturity() != com.scalecanvas.scenario.domain.MaturityLevel.HIGH) {
            risks.add(new RiskFinding("HIGH", "Zero data-loss objective",
                    "RPO 0 requires synchronous durability, tested failover and high operational maturity."));
        }
        if (metrics.storageAfter12MonthsGb().compareTo(new java.math.BigDecimal("500")) >= 0) {
            risks.add(new RiskFinding("MEDIUM", "Rapid data growth",
                    "Projected storage exceeds 500 GB within twelve months; retention and partitioning need review."));
        }
        long notYetCount = outcomes.stream().filter(outcome -> outcome.urgency() == Urgency.NOT_YET).count();
        if (notYetCount >= 3) {
            risks.add(new RiskFinding("LOW", "Over-engineering pressure",
                    "Several technologies are explicitly unjustified by the current scenario."));
        }
        return risks;
    }
}
