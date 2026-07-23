# Alertas

## Motor local

El motor de alertas de observabilidad es diferente del motor de recomendaciones
arquitectónicas, aunque puede reutilizar patrones de diseño deterministas.

## Regla

```text
scope
metric
operator
warning threshold
critical threshold
window
aggregation
minimum samples
enter duration
exit duration
cooldown
no-data policy
```

## No-data policy

- IGNORE
- UNKNOWN
- WARNING
- CRITICAL

Default: `UNKNOWN`.

## Histéresis

Evitar cambios de color por cada fluctuación:

```text
enter warning: >= 70% durante 5m
exit warning: < 65% durante 5m
enter critical: >= 85% durante 5m
exit critical: < 80% durante 5m
```

## Estados de instancia

- PENDING
- FIRING_WARNING
- FIRING_CRITICAL
- ACKNOWLEDGED
- RESOLVED
- SUPPRESSED
- UNKNOWN

## Alarmas del proveedor

Importar:

- ID externo;
- nombre;
- estado;
- condición;
- recurso;
- severidad;
- timestamp;
- enlace lógico;
- provider.

No duplicar como alerta local salvo mapping explícito.

## Notificaciones

Fuera del primer corte visual. Fases posteriores:

- email;
- webhook;
- Slack/Teams;
- event bus.

Toda notificación requiere:

- deduplicación;
- cooldown;
- retry;
- auditoría;
- redacción de secretos.

## Escritura cloud

No habilitar `PutMetricAlarm`, creación de AlertPolicy ni acciones automáticas en el MVP.
Si se incorpora:

- feature flag;
- rol separado;
- confirmación humana;
- diff de política;
- idempotency key;
- auditoría;
- rollback;
- prueba sandbox.
