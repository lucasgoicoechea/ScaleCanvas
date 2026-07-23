package com.scalecanvas.scenario.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "scenario_version")
public class ScenarioVersionEntity {
    @Id
    private UUID id;

    @Column(name = "scenario_id", nullable = false)
    private UUID scenarioId;

    @Column(name = "version_label", nullable = false, length = 64)
    private String versionLabel;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ScenarioVersionEntity() {}

    public ScenarioVersionEntity(UUID id, UUID scenarioId, String versionLabel, String payloadJson, Instant createdAt) {
        this.id = id;
        this.scenarioId = scenarioId;
        this.versionLabel = versionLabel;
        this.payloadJson = payloadJson;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getScenarioId() { return scenarioId; }
    public String getVersionLabel() { return versionLabel; }
    public String getPayloadJson() { return payloadJson; }
    public Instant getCreatedAt() { return createdAt; }
}
