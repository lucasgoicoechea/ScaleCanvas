# Contratos de proveedores

## Estado de implementación

| Provider | Estado |
|---|---|
| `SIMULATED` | Implementado y testeado |
| `AWS_CLOUDWATCH` | Contrato, sin SDK ni consultas |
| `GCP_CLOUD_MONITORING` | Contrato, sin SDK ni consultas |
| `PROMETHEUS` | Contrato |
| `OPENTELEMETRY` | Contrato |
| `KUBERNETES` | Contrato |
| `AZURE_MONITOR` | Contrato |
| `MCP_QUERY_ADAPTER` | Contrato |

`GET /api/v1/observability/providers` devuelve `IMPLEMENTED` únicamente para el provider
simulado. Los restantes devuelven `CONTRACT_ONLY` y capacidades en `false`.

Conservar un tipo o una interfaz no significa que el conector esté disponible. Ningún
conector contract-only debe aceptar sincronización ni anunciar discovery.

## SPI conceptual

```java
interface ObservabilityProvider {
    ProviderType type();
    ConnectionTestResult test(ConnectionConfig config);
    ResourceInventory discoverResources(ConnectionConfig config, DiscoveryQuery query);
    MetricBatch queryMetrics(ConnectionConfig config, MetricQuery query);
    AlarmBatch queryAlarms(ConnectionConfig config, AlarmQuery query);
}
```

La interfaz final puede variar, pero debe:

- evitar DTOs AWS/GCP en application/domain;
- aceptar cancelación/timeout;
- reportar datos parciales;
- mapear throttling;
- preservar provider metadata;
- no exponer credenciales.

## SIMULATED

Perfiles:

- NORMAL
- WARNING
- CRITICAL
- STALE
- UNKNOWN
- OVER_CAPACITY
- MIXED_HIERARCHY

Debe ser determinista mediante seed.

## AWS

### Descubrimiento inicial

- EC2: instancias, tipo, región/zona, tags, EBS.
- ECS: clusters, services, tasks y containers.
- EKS queda para conector Kubernetes posterior.
- STS para validar identidad y AssumeRole.

### Métricas

- CloudWatch `GetMetricData` por lotes.
- `ListMetrics` solo para descubrimiento controlado.
- `DescribeAlarms` para alarmas existentes.
- CPU de EC2 disponible de base.
- Memoria/filesystem/procesos requieren CloudWatch Agent o métricas custom.
- GPU requiere agente/telemetría compatible.

### Credenciales

Estrategias:

- DEFAULT_CHAIN
- PROFILE
- SSO_PROFILE
- ASSUME_ROLE
- WEB_IDENTITY
- INSTANCE_OR_TASK_ROLE

No agregar campos `accessKey` o `secretKey` a DTOs públicos.

## Google Cloud

### Descubrimiento inicial

- Compute Engine instances/disks/machine types.
- Cloud Run para recursos serverless.
- GKE queda para conector Kubernetes posterior.

### Métricas

- Cloud Monitoring `timeSeries.list`.
- Alert policies mediante `AlertPolicyServiceClient`.
- Incidents/alerts cuando la API y permisos lo permitan.
- CPU básica de VM.
- Memoria/procesos/filesystem requieren Ops Agent.
- GPU requiere Ops Agent compatible y drivers.

### Credenciales

- Application Default Credentials.
- Workload Identity.
- Service account impersonation.
- Credencial referenciada externamente.

No persistir JSON de service account.

## Prometheus

Conector recomendado después del simulado y antes de ampliar clouds:

- consulta HTTP API desde backend;
- mapeo por labels;
- permite probar el modelo canónico sin permisos cloud;
- no convertir ScaleCanvas en un Prometheus.

## OpenTelemetry

Utilizar Resource attributes para topología:

- `service.name`
- `service.namespace`
- `service.instance.id`
- `host.id`
- `host.name`
- `cloud.provider`
- `cloud.region`
- `cloud.availability_zone`
- `container.id`
- `k8s.pod.uid`
- `k8s.node.name`

## MCP

MCP no implementa `ObservabilityProvider` para polling continuo en la primera versión.

Uso aceptado:

- exponer snapshots como resources;
- exponer tools read-only;
- diagnóstico asistido;
- consultas ad hoc.

Tools candidatas:

- `get_observability_snapshot`
- `list_active_alerts`
- `get_resource_details`
- `get_metric_history`
- `explain_pressure_reasons`

Ninguna tool modifica infraestructura o alarmas.
