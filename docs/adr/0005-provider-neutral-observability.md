# ADR 0005 — Modelo provider-neutral de observabilidad

## Estado

Propuesto.

## Contexto

AWS, GCP, Prometheus y OpenTelemetry tienen modelos distintos. Exponerlos directamente al
frontend acoplaría UI, reglas y persistencia al primer proveedor.

## Decisión

Crear un modelo canónico y un SPI interno. Los adapters traducen recursos, métricas y
alarmas. El frontend solo consume snapshots normalizados.

## Consecuencias

Positivas:

- multi-cloud;
- proveedor simulado;
- tests contractuales;
- frontend estable.

Negativas:

- trabajo de normalización;
- algunas métricas específicas se conservan como metadata;
- riesgo de perder semántica si el modelo se hace demasiado genérico.
