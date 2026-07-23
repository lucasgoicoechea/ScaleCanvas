# ADR 0009 — Separar capacidad planificada de telemetría observada

## Estado

Propuesto.

## Contexto

ScaleCanvas ya calcula capacidad con inputs declarados. Esos valores no prueban consumo real.

## Decisión

Mantener dos modelos explícitos:

- PlannedCapacity;
- ObservedTelemetry.

Toda comparación indica procedencia y timestamp.

## Consecuencias

- evita falsos datos;
- permite comparar diseño vs realidad;
- requiere mapping de recursos;
- los cálculos frontend existentes no deben reutilizarse como métricas.
