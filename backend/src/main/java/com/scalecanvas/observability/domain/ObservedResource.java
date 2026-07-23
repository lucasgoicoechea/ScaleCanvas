package com.scalecanvas.observability.domain;

import java.time.Instant;
import java.util.Map;

public record ObservedResource(
        String id,
        String connectionId,
        String externalId,
        ResourceType resourceType,
        String name,
        ProviderType provider,
        String accountOrProject,
        String region,
        String zone,
        Map<String, String> labels,
        ResourceCapacity capacityMetadata,
        Instant discoveredAt,
        Instant lastSeenAt,
        ResourceLifecycleState lifecycleState) {
}
