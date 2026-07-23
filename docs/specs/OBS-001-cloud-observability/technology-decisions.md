# Tecnologías y librerías propuestas

No modificar dependencias hasta completar OBS-000 y aprobar los ADR.

## Frontend

El proyecto usa React 18.

Propuesta:

```text
three
@types/three
@react-three/fiber (major compatible con React 18)
@react-three/drei
```

ECharts continúa para:

- histórico;
- series temporales;
- heatmaps;
- comparación 2D;
- fallback.

No adoptar `echarts-gl` como implementación principal porque la escena requiere jerarquía,
selección, composición de objetos, labels y optimización específica.

## Backend AWS

AWS SDK for Java 2.x mediante BOM:

```text
software.amazon.awssdk:cloudwatch
software.amazon.awssdk:ec2
software.amazon.awssdk:sts
software.amazon.awssdk:ecs
```

Agregar módulos solo cuando la tarea los necesite.

## Backend GCP

Google Cloud Libraries BOM:

```text
com.google.cloud:google-cloud-monitoring
com.google.cloud:google-cloud-compute
```

Cloud Run/GKE requieren módulos y tareas separados.

## Prometheus

Primera opción: usar el HTTP API desde backend mediante cliente HTTP de Spring.
No agregar una dependencia pesada sin necesidad.

## OpenTelemetry

Usar semantic conventions para identidad y topología. Para ingestión:

- consultar un backend compatible;
- Prometheus;
- OTLP/collector en fase posterior.

ScaleCanvas no debe convertirse inicialmente en un collector OTLP general.

## MCP

Opciones futuras:

- MCP Java SDK oficial;
- Spring AI MCP Server Boot Starter.

La integración MCP debe depender del módulo normalizado y no de SDKs AWS/GCP.

## Persistencia

Usar PostgreSQL existente.

Primera etapa:

- último snapshot;
- inventario;
- relaciones;
- alertas;
- samples seleccionados.

TimescaleDB es una optimización futura, no requisito inicial.

## Cache y resiliencia

Preferir capacidades existentes de Spring. Evaluar librerías adicionales solo con evidencia:

- cache corta;
- timeout;
- retry con backoff;
- circuit breaker;
- bulkhead por conexión.

No aplicar retries a errores de autenticación o permisos.

## Versiones

- No elegir “latest” automáticamente.
- Usar BOM.
- documentar compatibilidad con Java/Spring reales del repositorio.
- ejecutar dependency tree y tests.
- no mezclar el upgrade Java/Spring con OBS-001.
