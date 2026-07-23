# Capacity Model

Las fórmulas son aproximaciones transparentes, no sizing definitivo.

```text
daily_requests = average_rps × 86,400
peak_hour_requests = peak_rps × 3,600
daily_transfer_bytes = daily_requests × average_payload_bytes
write_rps = peak_rps × write_percentage / 100
read_rps = peak_rps × read_percentage / 100
storage_after_n_months = current_storage × (1 + growth/100)^n
allowed_unavailability_minutes = minutes_in_month × (1 - availability_slo)

service_memory_utilization = (service_memory_mb × replicas) / minimum_unit_memory_mb
service_cpu_utilization = (service_cpu_count × replicas) / minimum_unit_cpu_count
service_capacity_percent = max(service_memory_utilization, service_cpu_utilization) × 100

recommendation =
  if service_capacity_percent > 80 then "Increase capacity"
  else if service_capacity_percent > 50 then "Watch"
  else "OK"
```

## Unidades obligatorias

RPS, bytes, porcentaje, duración, moneda, meses y eventos/día.

## Unidades de infraestructura

Servidores, vCPU, MB de memoria, réplicas, requests/minuto por servicio, binding (shared/dedicated/serverless).

## Procedencia y confianza

Cada valor puede ser `USER`, `IMPORTED` o `DEFAULT`, con confianza HIGH/MEDIUM/LOW.

## Validaciones

- read + write = 100.
- averageRps <= peakRps.
- p50 <= p95 <= p99.
- RPO/RTO no negativos.
- porcentajes entre 0 y 100.
- minimumUnitMemoryMb y minimumUnitCpuCount no negativos.
- services no nulos en serviceTopology.

<!-- OBSERVABILITY-CAPACITY:START -->
## Capacidad planificada vs observada

Las fórmulas existentes son estimaciones de diseño. No representan utilización medida.

En particular:

```text
(service_memory_mb × replicas) / minimum_unit_memory_mb
```

expresa demanda agregada relativa a una unidad, pero no utilización de cada réplica ni host.
Para una réplica:

```text
planned_memory_ratio_per_replica = service_memory_mb / minimum_unit_memory_mb
planned_cpu_ratio_per_replica = service_cpu_count / minimum_unit_cpu_count
```

Para telemetría:

```text
observed_ratio = measured_current / measured_or_declared_maximum
```

Toda comparación debe indicar `source`, `quality` y `timestamp`. No reutilizar los cálculos
frontend actuales para pintar observabilidad real.
<!-- OBSERVABILITY-CAPACITY:END -->
