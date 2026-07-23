package com.scalecanvas.scenario.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalecanvas.scenario.api.dto.CostComplexityItem;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import com.scalecanvas.scenario.api.dto.ScenarioSummary;
import com.scalecanvas.scenario.infrastructure.ScenarioEntity;
import com.scalecanvas.scenario.infrastructure.ScenarioJpaRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScenarioService {
    private final ScenarioJpaRepository repository;
    private final ObjectMapper objectMapper;
    private final ScenarioValidator validator;
    private final ScenarioVersionService versionService;
    private final String catalogVersion;

    public ScenarioService(
            ScenarioJpaRepository repository,
            ObjectMapper objectMapper,
            ScenarioValidator validator,
            ScenarioVersionService versionService,
            @Value("${app.rule-catalog-version}") String catalogVersion) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.versionService = versionService;
        this.catalogVersion = catalogVersion;
    }

    @Transactional
    public ScenarioSummary create(ScenarioRequest request) {
        validator.validate(request);
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        ScenarioEntity entity = new ScenarioEntity(
                id,
                request.name(),
                request.description(),
                writeJson(request),
                catalogVersion,
                now,
                now);
        ScenarioSummary summary = toSummary(repository.save(entity), request.productType().name());
        versionService.createVersion(id, "v1", request);
        return summary;
    }

    @Transactional(readOnly = true)
    public ScenarioRequest get(UUID id) {
        ScenarioEntity entity = repository.findById(id).orElseThrow(() -> new ScenarioNotFoundException(id));
        try {
            return objectMapper.readValue(entity.getPayloadJson(), ScenarioRequest.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Stored scenario cannot be read", exception);
        }
    }

    @Transactional(readOnly = true)
    public List<ScenarioSummary> list() {
        return repository.findAllByOrderByUpdatedAtDesc().stream()
                .map(entity -> {
                    ScenarioRequest request = readJson(entity.getPayloadJson());
                    return toSummary(entity, request.productType().name());
                })
                .toList();
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ScenarioNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ScenarioRequest.DeploymentRequest getDeployment(UUID id) {
        ScenarioRequest request = get(id);
        return request.deployment();
    }

    @Transactional
    public ScenarioRequest.DeploymentRequest updateDeployment(UUID id, ScenarioRequest.DeploymentRequest deployment) {
        ScenarioRequest existing = get(id);
        ScenarioRequest updated = new ScenarioRequest(
                existing.name(),
                existing.description(),
                existing.productType(),
                existing.workload(),
                existing.data(),
                existing.quality(),
                existing.organization(),
                deployment);
        ScenarioEntity entity = repository.findById(id).orElseThrow(() -> new ScenarioNotFoundException(id));
        entity.setPayloadJson(writeJson(updated));
        entity.setUpdatedAt(Instant.now());
        repository.save(entity);
        return deployment;
    }

    @Transactional
    public ScenarioSummary update(UUID id, ScenarioRequest request) {
        validator.validate(request);
        ScenarioEntity entity = repository.findById(id).orElseThrow(() -> new ScenarioNotFoundException(id));
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPayloadJson(writeJson(request));
        entity.setUpdatedAt(Instant.now());
        return toSummary(repository.save(entity), request.productType().name());
    }

    @Transactional
    public ScenarioSummary duplicate(UUID id) {
        ScenarioEntity source = repository.findById(id).orElseThrow(() -> new ScenarioNotFoundException(id));
        ScenarioRequest request = readJson(source.getPayloadJson());
        UUID newId = UUID.randomUUID();
        Instant now = Instant.now();
        ScenarioEntity copy = new ScenarioEntity(
                newId,
                request.name() + " (copy)",
                request.description(),
                writeJson(request),
                catalogVersion,
                now,
                now);
        ScenarioSummary summary = toSummary(repository.save(copy), request.productType().name());
        versionService.createVersion(newId, "v1", request);
        return summary;
    }

    @Transactional
    public ScenarioSummary importScenario(ScenarioRequest request) {
        validator.validate(request);
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        ScenarioEntity entity = new ScenarioEntity(
                id,
                request.name(),
                request.description(),
                writeJson(request),
                catalogVersion,
                now,
                now);
        ScenarioSummary summary = toSummary(repository.save(entity), request.productType().name());
        versionService.createVersion(id, "v1", request);
        return summary;
    }

    @Transactional(readOnly = true)
    public List<CostComplexityItem> estimateCostComplexity(UUID id) {
        ScenarioRequest request = get(id);
        return estimateCostComplexity(request);
    }

    @Transactional(readOnly = true)
    public List<CostComplexityItem> estimateCostComplexity(ScenarioRequest request) {
        return request.deployment().serviceTopology().services().stream()
                .map(service -> {
                    BigDecimal computeUnits = BigDecimal.valueOf(service.memoryMb())
                            .add(BigDecimal.valueOf(service.cpuCount() * 1024))
                            .multiply(BigDecimal.valueOf(service.replicas()));
                    BigDecimal estimatedCost = computeUnits.multiply(BigDecimal.valueOf(0.12))
                            .add(BigDecimal.valueOf(18))
                            .setScale(2, RoundingMode.HALF_UP);
                    int complexityScore = Math.min(100, Math.max(0,
                            service.cpuCount() * 5 + service.memoryMb() / 128 + service.replicas() * 8));
                    String driver = switch (request.deployment().serverType()) {
                        case SERVERLESS -> "Serverless access cost and cold-start tuning";
                        case CONTAINER -> "Container scheduling and replica management";
                        case VM -> "VM provisioning and guest OS maintenance";
                        case BARE_METAL -> "Hardware lifecycle and partition density";
                    };
                    return new CostComplexityItem(
                            service.serviceName(),
                            estimatedCost,
                            complexityScore,
                            driver);
                })
                .toList();
    }

    private String writeJson(ScenarioRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Scenario cannot be serialized", exception);
        }
    }

    private ScenarioRequest readJson(String json) {
        try {
            return objectMapper.readValue(json, ScenarioRequest.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Stored scenario cannot be read", exception);
        }
    }

    private ScenarioSummary toSummary(ScenarioEntity entity, String productType) {
        return new ScenarioSummary(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                productType,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
