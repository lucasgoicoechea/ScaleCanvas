package com.scalecanvas.observability.domain;

import java.time.Instant;
import java.util.List;

public record ProviderConnection(
        String id,
        String name,
        ProviderType providerType,
        Boolean enabled,
        Boolean readOnly,
        String credentialStrategy,
        String secretReference,
        String accountOrProject,
        List<String> regions,
        List<String> zones,
        Integer pollInterval,
        Integer timeout,
        java.util.Map<String, String> labels,
        Instant lastSuccessfulSyncAt,
        Instant lastFailureAt,
        ConnectionState connectionState) {
}
