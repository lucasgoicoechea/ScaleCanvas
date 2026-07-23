package com.scalecanvas.evaluation.infrastructure;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationResultJpaRepository extends JpaRepository<EvaluationResultEntity, UUID> {
    List<EvaluationResultEntity> findByEvaluationIdOrderByCreatedAtAsc(UUID evaluationId);
}
