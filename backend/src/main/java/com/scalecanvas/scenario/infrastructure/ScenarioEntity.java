package com.scalecanvas.scenario.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "architecture_scenario")
public class ScenarioEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 160)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "rule_catalog_version", nullable = false, length = 32)
    private String ruleCatalogVersion;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ScenarioEntity() {
    }

    public ScenarioEntity(UUID id, String name, String description, String payloadJson,
                          String ruleCatalogVersion, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.payloadJson = payloadJson;
        this.ruleCatalogVersion = ruleCatalogVersion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    public String getRuleCatalogVersion() { return ruleCatalogVersion; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
