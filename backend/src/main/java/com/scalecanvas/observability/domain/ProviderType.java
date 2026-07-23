package com.scalecanvas.observability.domain;

public enum ProviderType {
    SIMULATED,
    AWS_CLOUDWATCH,
    GCP_CLOUD_MONITORING,
    PROMETHEUS,
    OPENTELEMETRY,
    KUBERNETES,
    AZURE_MONITOR,
    MCP_QUERY_ADAPTER
}
