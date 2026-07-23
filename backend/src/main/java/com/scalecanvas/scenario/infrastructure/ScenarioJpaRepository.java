package com.scalecanvas.scenario.infrastructure;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioJpaRepository extends JpaRepository<ScenarioEntity, UUID> {
    List<ScenarioEntity> findAllByOrderByUpdatedAtDesc();
}
