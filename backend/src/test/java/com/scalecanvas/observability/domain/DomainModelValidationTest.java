package com.scalecanvas.observability.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DomainModelValidationTest {

    @Test
    void resourceCapacityStoresAllFields() {
        Instant ts = Instant.parse("2026-07-19T15:00:00Z");
        var cap = new ResourceCapacity(4.0, 1, 8L * 1024 * 1024 * 1024, 16L * 1024 * 1024 * 1024,
                100L * 1024 * 1024 * 1024, 3000, 200L * 1024 * 1024, "SIMULATED", ts);
        assertEquals(4.0, cap.cpuCapacityCores());
        assertEquals(1, cap.gpuCapacityCount());
        assertEquals(8L * 1024 * 1024 * 1024, cap.gpuMemoryBytes());
        assertEquals(16L * 1024 * 1024 * 1024, cap.memoryCapacityBytes());
        assertEquals(100L * 1024 * 1024 * 1024, cap.storageCapacityBytes());
        assertEquals(3000, cap.iopsLimit());
        assertEquals(200L * 1024 * 1024, cap.throughputBytesPerSecondLimit());
        assertEquals("SIMULATED", cap.source());
        assertEquals(ts, cap.timestamp());
    }

    @Test
    void metricSampleCarriesCanonicalUnits() {
        Instant ts = Instant.parse("2026-07-19T15:00:00Z");
        var sample = new MetricSample("host-1", DimensionKey.CPU, ts, 3.1, Unit.CORE, 3.1,
                Unit.CORE, "SIMULATED", MetricQuality.MEASURED, Map.of("agg", "avg"));
        assertEquals(Unit.CORE, sample.unitCanonical());
        assertEquals(MetricQuality.MEASURED, sample.quality());
        assertEquals(3.1, sample.valueCanonical());
    }

    @Test
    void relationAndObservedResourceAreImmutable() {
        Instant ts = Instant.parse("2026-07-19T15:00:00Z");
        var rel = new ResourceRelation("a", "b", RelationType.RUNS_ON, "SIMULATED", 0.9, ts, null);
        assertEquals(RelationType.RUNS_ON, rel.relationType());
        assertEquals(0.9, rel.confidence());

        var resource = new ObservedResource("host-1", "conn-1", "i-1", ResourceType.HOST, "h",
                ProviderType.SIMULATED, "acct", "us-east-1", "us-east-1a", Map.of(),
                null, ts, ts, ResourceLifecycleState.HEALTHY);
        assertEquals(ResourceType.HOST, resource.resourceType());
        assertEquals(ProviderType.SIMULATED, resource.provider());
        assertTrue(resource.id().equals("host-1"));
    }

    @Test
    void snapshotAggregatesResourcesAndAlerts() {
        var resource = new ObservedResource("host-1", "conn-1", "i-1", ResourceType.HOST, "h",
                ProviderType.SIMULATED, "acct", "r", "z", Map.of(), null,
                Instant.now(), Instant.now(), ResourceLifecycleState.HEALTHY);
        var summary = new ObservabilitySnapshot.DataQualitySummary(
                1, 1, 0, 0, 0, 0, 0, Map.of("SIMULATED", 1));
        var snapshot = new ObservabilitySnapshot("snap-1", "conn-1", Instant.now(), "PT5M",
                List.of(resource), List.of(), List.of(), summary, false, List.of());
        assertEquals(1, snapshot.resources().size());
        assertTrue(snapshot.warnings().isEmpty());
    }
}
