package com.scalecanvas.observability.provider.spi;

import com.scalecanvas.observability.domain.ConnectionState;
import com.scalecanvas.observability.domain.ObservedResource;
import com.scalecanvas.observability.domain.ProviderConnection;
import com.scalecanvas.observability.domain.ProviderType;
import java.util.List;

public interface ObservabilityProvider {

    ProviderType type();

    ConnectionTestResult testConnection(ProviderConnection connection);

    List<ObservedResource> discoverResources(ProviderConnection connection);

    List<com.scalecanvas.observability.domain.MetricSample> queryMetrics(ProviderConnection connection);

    List<com.scalecanvas.observability.domain.AlertInstance> queryAlarms(ProviderConnection connection);

    record ConnectionTestResult(boolean success, ConnectionState state, String message) {
    }
}
