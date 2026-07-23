package com.scalecanvas.scenario.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import com.scalecanvas.scenario.infrastructure.ScenarioVersionEntity;
import com.scalecanvas.scenario.infrastructure.ScenarioVersionJpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScenarioVersionService {
    private final ScenarioVersionJpaRepository repository;
    private final ObjectMapper objectMapper;

    public ScenarioVersionService(ScenarioVersionJpaRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ScenarioVersionEntity createVersion(UUID scenarioId, String versionLabel, ScenarioRequest request) {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        String payload = writeJson(request);
        ScenarioVersionEntity entity = new ScenarioVersionEntity(id, scenarioId, versionLabel, payload, now);
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<ScenarioVersionEntity> listVersions(UUID scenarioId) {
        return repository.findByScenarioIdOrderByCreatedAtDesc(scenarioId);
    }

    private String writeJson(ScenarioRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Scenario cannot be serialized", exception);
        }
    }
}
