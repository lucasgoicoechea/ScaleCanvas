package com.scalecanvas.observability.provider.simulated;

import com.scalecanvas.observability.domain.AlertInstance;
import com.scalecanvas.observability.domain.AlertInstanceState;
import com.scalecanvas.observability.domain.ConnectionState;
import com.scalecanvas.observability.domain.DimensionKey;
import com.scalecanvas.observability.domain.MetricQuality;
import com.scalecanvas.observability.domain.MetricSample;
import com.scalecanvas.observability.domain.ObservabilitySnapshot;
import com.scalecanvas.observability.domain.ObservedResource;
import com.scalecanvas.observability.domain.ProviderConnection;
import com.scalecanvas.observability.domain.ProviderType;
import com.scalecanvas.observability.domain.ResourceCapacity;
import com.scalecanvas.observability.domain.ResourceLifecycleState;
import com.scalecanvas.observability.domain.RelationType;
import com.scalecanvas.observability.domain.ResourceRelation;
import com.scalecanvas.observability.domain.ResourceType;
import com.scalecanvas.observability.domain.Unit;
import com.scalecanvas.observability.provider.spi.ObservabilityProvider;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SimulatedProvider implements ObservabilityProvider {

    private static final Instant NOW = Instant.parse("2026-07-19T15:00:00Z");
    private static final Instant STALE_BOUNDARY = Instant.parse("2026-07-19T13:00:00Z");

    @Override
    public ProviderType type() {
        return ProviderType.SIMULATED;
    }

    @Override
    public ConnectionTestResult testConnection(ProviderConnection connection) {
        return new ConnectionTestResult(true, ConnectionState.CONNECTED, "Simulated provider always available");
    }

    @Override
    public List<ObservedResource> discoverResources(ProviderConnection connection) {
        return generate(connection.id(), 1L, SimulatedProfile.NORMAL).resources();
    }

    @Override
    public List<MetricSample> queryMetrics(ProviderConnection connection) {
        return List.of();
    }

    @Override
    public List<AlertInstance> queryAlarms(ProviderConnection connection) {
        return List.of();
    }

    public ObservabilitySnapshot generate(String connectionId, long seed, SimulatedProfile profile) {
        Random rng = new Random(seed);
        List<ObservedResource> resources = new ArrayList<>();
        List<ResourceRelation> relations = new ArrayList<>();
        List<AlertInstance> alerts = new ArrayList<>();

        int hostCount = 3;
        for (int h = 0; h < hostCount; h++) {
            String hostId = "host-" + (h + 1);
            double baseLoad = profileLoad(profile, h, rng);

            ResourceCapacity capacity = new ResourceCapacity(
                    4.0,
                    profile == SimulatedProfile.OVER_CAPACITY ? 1 : 0,
                    profile == SimulatedProfile.OVER_CAPACITY ? 8L * 1024 * 1024 * 1024 : null,
                    16L * 1024 * 1024 * 1024,
                    100L * 1024 * 1024 * 1024,
                    3000,
                    200L * 1024 * 1024,
                    "SIMULATED",
                    NOW);

            double cpuUsed = clamp(capacity.cpuCapacityCores() * baseLoad, 0, capacity.cpuCapacityCores());
            double memUsed = capacity.memoryCapacityBytes() * baseLoad;

            ResourceLifecycleState hostState = stateForProfile(profile, h, baseLoad);

            ObservedResource host = new ObservedResource(
                    hostId,
                    connectionId,
                    "i-sim-" + (h + 1),
                    h == 0 && profile == SimulatedProfile.MIXED_HIERARCHY ? ResourceType.NODE : ResourceType.HOST,
                    "sim-host-" + (h + 1),
                    ProviderType.SIMULATED,
                    "sim-account",
                    "us-east-1",
                    "us-east-1a",
                    Map.of("tier", "compute", "profile", profile.name()),
                    capacity,
                    NOW,
                    NOW,
                    hostState);
            resources.add(host);

            for (int s = 0; s < 2; s++) {
                String svcId = hostId + "-svc-" + (s + 1);
                ObservedResource service = new ObservedResource(
                        svcId,
                        connectionId,
                        "svc-sim-" + hostId + "-" + s,
                        ResourceType.SERVICE,
                        "sim-service-" + (h + 1) + "-" + (s + 1),
                        ProviderType.SIMULATED,
                        "sim-account",
                        "us-east-1",
                        "us-east-1a",
                        Map.of("app", "payments"),
                        null,
                        NOW,
                        NOW,
                        hostState);
                resources.add(service);
                relations.add(new ResourceRelation(hostId, svcId, RelationType.RUNS_ON, "SIMULATED", 0.9, NOW, null));
            }

            if (profile == SimulatedProfile.MIXED_HIERARCHY) {
                String clusterId = "cluster-1";
                if (!relations.stream().anyMatch(r -> r.parentId().equals(clusterId))) {
                    relations.add(0, new ResourceRelation(clusterId, hostId, RelationType.MEMBER_OF, "SIMULATED", 0.7, NOW, null));
                }
                if (resources.stream().noneMatch(r -> r.id().equals(clusterId))) {
                    resources.add(new ObservedResource(
                            clusterId, connectionId, "c-sim-1", ResourceType.CLUSTER, "sim-cluster-1",
                            ProviderType.SIMULATED, "sim-account", "us-east-1", null,
                            Map.of(), null, NOW, NOW, ResourceLifecycleState.HEALTHY));
                }
            }

            if (hostState == ResourceLifecycleState.WARNING || hostState == ResourceLifecycleState.CRITICAL) {
                alerts.add(new AlertInstance(
                        "alert-" + hostId,
                        "rule-cpu",
                        hostId,
                        hostState == ResourceLifecycleState.CRITICAL ? AlertInstanceState.FIRING_CRITICAL : AlertInstanceState.FIRING_WARNING,
                        NOW, NOW, null,
                        cpuUsed, capacity.cpuCapacityCores() * 0.85,
                        "CPU " + hostState.name().toLowerCase() + " for 5m",
                        "SIMULATED", null));
            }
        }

        ObservabilitySnapshot.DataQualitySummary summary = new ObservabilitySnapshot.DataQualitySummary(
                resources.size(),
                resources.size() * 3,
                0, 0, 0,
                profile == SimulatedProfile.STALE ? resources.size() : 0,
                profile == SimulatedProfile.UNKNOWN ? resources.size() : 0,
                Map.of("SIMULATED", resources.size()));

        return new ObservabilitySnapshot(
                "snap-" + seed + "-" + profile.name(),
                connectionId,
                NOW,
                "PT5M",
                List.copyOf(resources),
                List.copyOf(relations),
                List.copyOf(alerts),
                summary,
                false,
                List.of());
    }

    private double profileLoad(SimulatedProfile profile, int hostIndex, Random rng) {
        return switch (profile) {
            case NORMAL -> 0.35 + rng.nextDouble() * 0.20;
            case WARNING -> 0.70 + rng.nextDouble() * 0.05;
            case CRITICAL -> 0.88 + rng.nextDouble() * 0.05;
            case STALE -> 0.40 + rng.nextDouble() * 0.20;
            case UNKNOWN -> Double.NaN;
            case OVER_CAPACITY -> 1.10 + rng.nextDouble() * 0.10;
            case MIXED_HIERARCHY -> hostIndex == 0 ? 0.30 : (hostIndex == 1 ? 0.75 : 0.90);
        };
    }

    private ResourceLifecycleState stateForProfile(SimulatedProfile profile, int hostIndex, double load) {
        return switch (profile) {
            case NORMAL -> ResourceLifecycleState.HEALTHY;
            case WARNING -> hostIndex == 0 ? ResourceLifecycleState.HEALTHY : ResourceLifecycleState.WARNING;
            case CRITICAL -> ResourceLifecycleState.CRITICAL;
            case STALE -> ResourceLifecycleState.STALE;
            case UNKNOWN -> ResourceLifecycleState.UNKNOWN;
            case OVER_CAPACITY -> ResourceLifecycleState.CRITICAL;
            case MIXED_HIERARCHY -> load >= 0.85 ? ResourceLifecycleState.CRITICAL
                    : (load >= 0.70 ? ResourceLifecycleState.WARNING : ResourceLifecycleState.HEALTHY);
        };
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
