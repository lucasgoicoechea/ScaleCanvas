package com.scalecanvas.observability.provider.simulated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.scalecanvas.observability.domain.ObservabilitySnapshot;
import java.util.List;
import org.junit.jupiter.api.Test;

class SimulatedProviderDeterminismTest {

    private final SimulatedProvider provider = new SimulatedProvider();

    @Test
    void sameSeedAndProfileProducesIdenticalSnapshot() {
        ObservabilitySnapshot a = provider.generate("conn", 42L, SimulatedProfile.WARNING);
        ObservabilitySnapshot b = provider.generate("conn", 42L, SimulatedProfile.WARNING);
        assertEquals(a.snapshotId(), b.snapshotId());
        assertEquals(a.resources().size(), b.resources().size());
        assertEquals(a.relations().size(), b.relations().size());
        assertEquals(a.resources(), b.resources());
        assertEquals(a.relations(), b.relations());
        assertEquals(a.alerts(), b.alerts());
    }

    @Test
    void differentSeedsProduceDifferentSnapshots() {
        ObservabilitySnapshot a = provider.generate("conn", 1L, SimulatedProfile.NORMAL);
        ObservabilitySnapshot b = provider.generate("conn", 2L, SimulatedProfile.NORMAL);
        assertEquals(a.resources().size(), b.resources().size());
        assertTrue(!a.snapshotId().equals(b.snapshotId()));
    }

    @Test
    void criticalProfileFiresCriticalAlerts() {
        ObservabilitySnapshot snap = provider.generate("conn", 7L, SimulatedProfile.CRITICAL);
        assertNotNull(snap.alerts());
        assertTrue(snap.alerts().stream()
                .anyMatch(al -> al.state() == com.scalecanvas.observability.domain.AlertInstanceState.FIRING_CRITICAL));
    }

    @Test
    void mixedHierarchyIncludesClusterAndNode() {
        ObservabilitySnapshot snap = provider.generate("conn", 3L, SimulatedProfile.MIXED_HIERARCHY);
        List<String> types = snap.resources().stream()
                .map(r -> r.resourceType().name()).toList();
        assertTrue(types.contains("CLUSTER"));
        assertTrue(types.contains("NODE"));
    }
}
