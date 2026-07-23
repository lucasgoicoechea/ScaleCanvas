package com.scalecanvas.scenario.infrastructure;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioVersionJpaRepository extends JpaRepository<ScenarioVersionEntity, UUID> {
    java.util.List<ScenarioVersionEntity> findByScenarioIdOrderByCreatedAtDesc(UUID scenarioId);
}
