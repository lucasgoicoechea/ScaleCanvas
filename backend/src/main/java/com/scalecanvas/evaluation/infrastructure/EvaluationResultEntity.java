package com.scalecanvas.evaluation.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evaluation_result")
public class EvaluationResultEntity {
    @Id
    private UUID id;

    @Column(name = "evaluation_id", nullable = false)
    private UUID evaluationId;

    @Column(name = "variant", nullable = false, length = 32)
    private String variant;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected EvaluationResultEntity() {}

    public EvaluationResultEntity(UUID id, UUID evaluationId, String variant, String payloadJson, Instant createdAt) {
        this.id = id;
        this.evaluationId = evaluationId;
        this.variant = variant;
        this.payloadJson = payloadJson;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getEvaluationId() { return evaluationId; }
    public String getVariant() { return variant; }
    public String getPayloadJson() { return payloadJson; }
    public Instant getCreatedAt() { return createdAt; }
}
