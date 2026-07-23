package com.scalecanvas.observability.api;

import com.scalecanvas.observability.domain.ConnectionState;
import com.scalecanvas.observability.domain.ObservabilitySnapshot;
import com.scalecanvas.observability.domain.ProviderConnection;
import com.scalecanvas.observability.domain.ProviderType;
import com.scalecanvas.observability.provider.simulated.SimulatedProfile;
import com.scalecanvas.observability.provider.simulated.SimulatedProvider;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/observability")
public class ObservabilityController {

    private final SimulatedProvider simulatedProvider = new SimulatedProvider();
    private final Map<String, ProviderConnection> connections = new ConcurrentHashMap<>();

    @GetMapping("/providers")
    public List<Map<String, Object>> providers() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (ProviderType type : ProviderType.values()) {
            boolean implemented = type == ProviderType.SIMULATED;
            result.add(Map.of(
                    "providerType", type.name(),
                    "status", implemented ? "IMPLEMENTED" : "CONTRACT_ONLY",
                    "readOnly", true,
                    "capabilities", Map.of(
                            "discoverResources", implemented,
                            "queryMetrics", implemented,
                            "queryAlarms", implemented)));
        }
        return result;
    }

    @GetMapping("/connections")
    public List<ProviderConnection> connections() {
        return new ArrayList<>(connections.values());
    }

    @PostMapping("/connections")
    public ProviderConnection createConnection(@RequestBody ProviderConnection connection) {
        String id = connection.id() != null ? connection.id() : "conn-" + UUID.randomUUID();
        ProviderConnection stored = new ProviderConnection(
                id,
                connection.name(),
                connection.providerType(),
                connection.enabled(),
                connection.readOnly(),
                connection.credentialStrategy(),
                connection.secretReference(),
                connection.accountOrProject(),
                connection.regions(),
                connection.zones(),
                connection.pollInterval(),
                connection.timeout(),
                connection.labels(),
                connection.lastSuccessfulSyncAt(),
                connection.lastFailureAt(),
                connection.connectionState() == null ? ConnectionState.DISCONNECTED : connection.connectionState());
        connections.put(id, stored);
        return stored;
    }

    @GetMapping("/connections/{id}")
    public ProviderConnection getConnection(@PathVariable String id) {
        return requireConnection(id);
    }

    @DeleteMapping("/connections/{id}")
    public Map<String, Object> deleteConnection(@PathVariable String id) {
        requireConnection(id);
        connections.remove(id);
        return Map.of("id", id, "removed", true);
    }

    @PostMapping("/connections/{id}/test")
    public Map<String, Object> testConnection(@PathVariable String id) {
        ProviderConnection conn = requireConnection(id);
        requireSimulatedProvider(conn);
        var result = simulatedProvider.testConnection(conn);
        return Map.of("success", result.success(), "state", result.state().name(), "message", result.message());
    }

    @PostMapping("/connections/{id}/sync")
    public ObservabilitySnapshot syncConnection(@PathVariable String id) {
        ProviderConnection conn = requireConnection(id);
        requireSimulatedProvider(conn);
        Instant syncedAt = Instant.now();
        connections.put(id, new ProviderConnection(
                conn.id(), conn.name(), conn.providerType(), conn.enabled(), conn.readOnly(),
                conn.credentialStrategy(), conn.secretReference(), conn.accountOrProject(),
                conn.regions(), conn.zones(), conn.pollInterval(), conn.timeout(), conn.labels(),
                syncedAt, conn.lastFailureAt(), ConnectionState.CONNECTED));
        return simulatedProvider.generate(id, Math.max(1L, id.hashCode()), SimulatedProfile.NORMAL);
    }

    @GetMapping("/snapshot/simulated")
    public ObservabilitySnapshot simulatedSnapshot(
            @RequestParam(defaultValue = "NORMAL") SimulatedProfile profile,
            @RequestParam(defaultValue = "1") long seed) {
        return simulatedProvider.generate("sim-conn", seed, profile);
    }

    @GetMapping("/snapshot")
    public ObservabilitySnapshot snapshot(
            @RequestParam(required = false) String connectionId,
            @RequestParam(required = false) String rootResourceId,
            @RequestParam(defaultValue = "3") int depth) {
        return simulatedProvider.generate(
                connectionId == null ? "default" : connectionId,
                Math.max(1L, (long) (rootResourceId == null ? 1 : rootResourceId.hashCode())),
                SimulatedProfile.NORMAL);
    }

    private ProviderConnection requireConnection(String id) {
        ProviderConnection connection = connections.get(id);
        if (connection == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Observability connection not found: " + id);
        }
        return connection;
    }

    private void requireSimulatedProvider(ProviderConnection connection) {
        if (connection.providerType() != ProviderType.SIMULATED) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_IMPLEMENTED,
                    "Provider connector is not implemented: " + connection.providerType());
        }
    }
}
