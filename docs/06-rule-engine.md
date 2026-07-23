# Rule Engine

## Interfaz conceptual

```java
interface ArchitectureRule {
    RuleId id();
    RuleVersion version();
    boolean appliesTo(EvaluationContext context);
    RuleOutcome evaluate(EvaluationContext context);
}
```

## Outcome

`TRIGGERED`, `NOT_TRIGGERED` o `INSUFFICIENT_DATA`, con severidad, recomendación, evidencia, threshold, alternativas y tradeoffs.

## Catálogo inicial

### Topología

- MODULAR_MONOLITH_DEFAULT
- HORIZONTAL_REPLICAS
- API_GATEWAY_NOT_YET
- MICROSERVICES_ORGANIZATIONAL_TRIGGER

### Datos

- POSTGRES_PRIMARY
- READ_REPLICA_READ_HEAVY
- PARTITION_LARGE_TIME_SERIES
- OBJECT_STORAGE_FOR_BINARY
- SHARDING_NOT_YET

### Rendimiento

- LOCAL_CACHE_REFERENCE_DATA
- DISTRIBUTED_CACHE_READ_HEAVY
- CDN_STATIC_OR_GLOBAL
- ASYNC_LONG_RUNNING_WORK

### Confiabilidad

- BACKUP_AND_RESTORE_BASELINE
- MULTI_AZ_FOR_SLO
- ACTIVE_PASSIVE_DR
- MULTI_REGION_NOT_YET

### Mensajería y plataforma

- SIMPLE_QUEUE_FOR_ASYNC
- EVENT_STREAM_FOR_HIGH_VOLUME_REPLAY
- KAFKA_NOT_YET
- CONTAINERS_FOR_PORTABILITY
- MANAGED_PLATFORM_FOR_SMALL_TEAM
- KUBERNETES_OPERATIONAL_TRIGGER
- SERVERLESS_BURSTY_WORKLOAD
- SERVERLESS_TRIGGER
- CONTAINER_MIN_UNIT
- GATEWAY_SIZING
- LOAD_BALANCER_RECOMMENDATION

## Observabilidad

- Logs estructurados con patrones JSON/ECES.
- Health indicators para AI, base de datos y Flyway.
- Métricas de evaluaciones, reglas y proveedores.
- Actuator expuesto en `/actuator` con health, info, metrics y prometheus.
- Trazabilidad por `evaluationId` y `scenarioId`.

## Versionado

El resultado guarda versión de catálogo. Cambiar thresholds exige nueva versión y tests de regresión. Conflictos se muestran explícitamente; el LLM no los resuelve.
