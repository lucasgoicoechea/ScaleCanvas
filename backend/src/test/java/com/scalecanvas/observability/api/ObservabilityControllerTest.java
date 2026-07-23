package com.scalecanvas.observability.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.scalecanvas.observability.domain.ConnectionState;
import com.scalecanvas.observability.domain.ProviderConnection;
import com.scalecanvas.observability.domain.ProviderType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class ObservabilityControllerTest {

    @Test
    void missingConnectionReturnsNotFound() {
        ObservabilityController controller = new ObservabilityController();

        assertThatThrownBy(() -> controller.getConnection("missing"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(exception -> ((ResponseStatusException) exception).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void simulatedSyncReturnsSnapshotAndUpdatesConnection() {
        ObservabilityController controller = new ObservabilityController();
        ProviderConnection created = controller.createConnection(new ProviderConnection(
                null,
                "Local simulation",
                ProviderType.SIMULATED,
                true,
                true,
                "NONE",
                null,
                "local",
                List.of("local"),
                List.of(),
                60,
                10,
                Map.of(),
                null,
                null,
                ConnectionState.DISCONNECTED));

        var snapshot = controller.syncConnection(created.id());

        assertThat(snapshot.connectionId()).isEqualTo(created.id());
        assertThat(snapshot.resources()).isNotEmpty();
        assertThat(controller.getConnection(created.id()).connectionState())
                .isEqualTo(ConnectionState.CONNECTED);
        assertThat(controller.getConnection(created.id()).lastSuccessfulSyncAt()).isNotNull();
    }

    @Test
    void onlySimulatedProviderAdvertisesImplementedCapabilities() {
        ObservabilityController controller = new ObservabilityController();

        var simulated = controller.providers().stream()
                .filter(item -> item.get("providerType").equals("SIMULATED"))
                .findFirst()
                .orElseThrow();
        var aws = controller.providers().stream()
                .filter(item -> item.get("providerType").equals("AWS_CLOUDWATCH"))
                .findFirst()
                .orElseThrow();

        assertThat(simulated.get("status")).isEqualTo("IMPLEMENTED");
        assertThat(aws.get("status")).isEqualTo("CONTRACT_ONLY");
        Map<?, ?> capabilities = (Map<?, ?>) aws.get("capabilities");
        assertThat(capabilities.get("discoverResources")).isEqualTo(false);
        assertThat(capabilities.get("queryMetrics")).isEqualTo(false);
        assertThat(capabilities.get("queryAlarms")).isEqualTo(false);
    }
}
