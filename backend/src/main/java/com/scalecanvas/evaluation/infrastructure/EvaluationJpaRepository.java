package com.scalecanvas.evaluation.infrastructure;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationJpaRepository extends JpaRepository<EvaluationEntity, UUID> {
    List<EvaluationEntity> findByScenarioIdOrderByGeneratedAtDesc(UUID scenarioId);
}
