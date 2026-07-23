package com.scalecanvas.evaluation.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evaluation")
public class EvaluationEntity {
    @Id
    private UUID id;

    @Column(name = "scenario_id", nullable = false)
    private UUID scenarioId;

    @Column(name = "scenario_name", nullable = false, length = 160)
    private String scenarioName;

    @Column(name = "catalog_version", nullable = false, length = 32)
    private String catalogVersion;

    @Column(name = "generated_at", nullable = false)
    private Instant generatedAt;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    protected EvaluationEntity() {}

    public EvaluationEntity(UUID id, UUID scenarioId, String scenarioName, String catalogVersion, Instant generatedAt, String payloadJson) {
        this.id = id;
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
        this.catalogVersion = catalogVersion;
        this.generatedAt = generatedAt;
        this.payloadJson = payloadJson;
    }

    public UUID getId() { return id; }
    public UUID getScenarioId() { return scenarioId; }
    public String getScenarioName() { return scenarioName; }
    public String getCatalogVersion() { return catalogVersion; }
    public Instant getGeneratedAt() { return generatedAt; }
    public String getPayloadJson() { return payloadJson; }
}
