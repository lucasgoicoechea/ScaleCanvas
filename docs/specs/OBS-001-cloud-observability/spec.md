# OBS-001 — Observabilidad 3D multi-cloud

## Problema

ScaleCanvas puede modelar capacidad declarada, pero no puede contrastarla con uso real.
La visualización actual tampoco es tridimensional: es un heatmap 2D.

## Objetivo

Incorporar una vista de observabilidad provider-neutral que represente capacidad y presión
de recursos de infraestructura y servicios mediante una escena 3D, con métricas reales o
simuladas, jerarquía, alertas y conectores configurables.

## Usuarios

- backend engineers;
- arquitectos;
- tech leads;
- SRE/DevOps;
- estudiantes avanzados;
- responsables de capacidad.

## Casos principales

1. Explorar un escenario simulado sin credenciales.
2. Conectar una cuenta AWS en modo lectura.
3. Conectar un proyecto GCP en modo lectura.
4. Ver hosts, nodos, workloads y servicios.
5. Comparar actual, máximo y porcentaje.
6. Identificar warning, critical, stale y unknown.
7. Definir alertas locales.
8. Importar alarmas del proveedor.
9. Consultar datos normalizados desde una IA mediante MCP opcional.
10. Mantener separadas capacidad planificada y telemetría observada.

## Dimensiones

### Compute

Canales separados:

- CPU: vCPU como capacidad; utilización como porcentaje o cores efectivos.
- GPU: cantidad/tipo como capacidad; utilización y memoria GPU como submétricas.

No se suman CPU y GPU. `computePressure` toma el máximo o una política explícita.

### Memory

- capacidad utilizable en bytes;
- bytes usados;
- bytes disponibles;
- porcentaje;
- swap como métrica secundaria.

### Storage

Separar:

- capacidad lógica/física;
- filesystem ocupado;
- IOPS;
- throughput;
- latencia;
- queue depth.

Un volumen provisionado no prueba cuánto filesystem está ocupado.

### Service health

Recursos no bastan. Cuando existan datos, incluir:

- error rate;
- latencia p95/p99;
- disponibilidad;
- queue lag/depth;
- throttling;
- saturation;
- request rate.

## Jerarquía

```text
ProviderConnection
└── Account/Project
    └── Region/Zone
        └── Cluster/Host/Node/ServerlessPlatform
            └── Workload/Service
                └── Instance/Pod/Container/Process
```

La relación padre-hijo debe incluir `source` y `confidence`.

## Estados

- `HEALTHY`
- `WARNING`
- `CRITICAL`
- `UNKNOWN`
- `STALE`
- `NO_CAPACITY`
- `DISABLED`

## Alertas

### Locales

Reglas configurables evaluadas por ScaleCanvas.

### Importadas

Alarmas/policies/incidents leídos desde el proveedor.

### Escritura cloud

Fuera del primer alcance. Requiere ADR, autorización y auditoría.

## Conectores

MVP de observabilidad:

- `SIMULATED`
- `AWS_CLOUDWATCH`
- `GCP_CLOUD_MONITORING`

Siguientes:

- `PROMETHEUS`
- `OPENTELEMETRY`
- `KUBERNETES`
- `AZURE_MONITOR`
- `MCP_QUERY_ADAPTER`

## No objetivos iniciales

- reemplazar Grafana, CloudWatch o Cloud Monitoring;
- almacenar todos los logs y trazas;
- remediación automática;
- autoscaling;
- crear infraestructura;
- escribir alarmas cloud;
- prometer predicción de fallos;
- hacer streaming mediante MCP;
- soportar miles de objetos 3D desde la primera iteración.

## Criterios de aceptación resumidos

- snapshot provider-neutral con unidades, máximos, timestamps y procedencia;
- escena 3D real con capacidad exterior y carga interior;
- CPU/GPU/memory/storage independientes;
- labels y detalle seleccionable;
- jerarquía demostrable;
- warning/critical con histéresis;
- unknown/stale visibles;
- proveedor simulado;
- conectores AWS y GCP read-only;
- secretos nunca persistidos en texto plano;
- 250 unidades visuales con degradación controlada;
- aplicación sigue funcionando sin cloud ni MCP.
