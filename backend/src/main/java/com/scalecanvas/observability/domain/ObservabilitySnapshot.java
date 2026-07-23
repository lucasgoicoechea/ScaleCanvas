package com.scalecanvas.observability.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ObservabilitySnapshot(
        String snapshotId,
        String connectionId,
        Instant generatedAt,
        String sourceWindow,
        List<ObservedResource> resources,
        List<ResourceRelation> relations,
        List<AlertInstance> alerts,
        DataQualitySummary dataQualitySummary,
        Boolean partial,
        List<String> warnings) {

    public record DataQualitySummary(
            int totalResources,
            int measuredSamples,
            int derivedSamples,
            int estimatedSamples,
            int missingSamples,
            int staleSamples,
            int unknownResources,
            Map<String, Integer> qualityBySource) {
    }
}
