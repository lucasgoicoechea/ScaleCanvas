# Normalización de métricas

## Separación obligatoria

ScaleCanvas maneja dos dominios:

1. `PlannedCapacity`: valores declarados o calculados para escenarios.
2. `ObservedTelemetry`: valores medidos en un período.

No mezclar ambos sin etiquetar procedencia.

## Ratios

```text
ratio = current / maximum
visualRatio = clamp(ratio, 0, 1)
overflowRatio = max(ratio - 1, 0)
```

Si `maximum` es nulo, cero, negativo o incompatible:

```text
ratio = null
state = NO_CAPACITY
```

Si no hay sample:

```text
state = UNKNOWN
```

Si el sample existe pero está vencido:

```text
state = STALE
```

## Compute

```text
cpuRatio = cpuUsedCores / cpuCapacityCores
gpuUtilizationRatio = provider utilization percent / 100
gpuMemoryRatio = gpuMemoryUsedBytes / gpuMemoryCapacityBytes
gpuRatio = max(gpuUtilizationRatio, gpuMemoryRatio)
computeRatio = max(cpuRatio, gpuRatio)
```

CPU y GPU permanecen visibles por separado.

## Memory

Preferencia:

```text
memoryUsed = total - available
memoryRatio = memoryUsed / usableMemory
```

No usar `free` como equivalente a `available` sin documentar la fuente.

## Storage

```text
capacityRatio = filesystemUsedBytes / filesystemCapacityBytes
iopsPressure = observedIops / provisionedIopsLimit
throughputPressure = observedThroughput / throughputLimit
latencyPressure = observedLatency / configuredLatencyThreshold
storagePressure = max(capacityRatio, iopsPressure, throughputPressure, latencyPressure)
```

Un EBS/GCE disk puede informar tamaño provisionado sin filesystem usage. En ese caso:

- mostrar capacidad provisionada;
- filesystem usage = UNKNOWN;
- no inventar porcentaje ocupado.

## Service pressure

```text
resourcePressure = max(computeRatio, memoryRatio, storagePressure)
errorPressure = errorRate / configuredErrorBudgetThreshold
latencyPressure = observedP95 / targetP95
queuePressure = observedQueueDepth / configuredQueueLimit
availabilityPressure = targetAvailabilityGapPolicy(...)
servicePressure = max(resourcePressure, errorPressure, latencyPressure, queuePressure, availabilityPressure)
```

Solo incluir indicadores disponibles.

## Estados por threshold

Defaults de demo, no universales:

```text
HEALTHY  ratio < 0.70
WARNING  ratio >= 0.70 durante 5 minutos
CRITICAL ratio >= 0.85 durante 5 minutos
```

Recuperación con histéresis:

```text
WARNING -> HEALTHY si ratio < 0.65 durante 5 minutos
CRITICAL -> WARNING si ratio < 0.80 durante 5 minutos
```

Los thresholds deben ser configurables por recurso, métrica y perfil.

## Agregación temporal

- CPU/GPU: average y max.
- memoria: latest y max.
- filesystem: latest.
- IOPS/throughput: average, p95 o max según regla.
- latencia: p95/p99.
- errores: rate sobre ventana.
- colas: latest y max.

Guardar siempre:

- ventana;
- período/alignment;
- agregación;
- timezone UTC;
- timestamp del sample.
