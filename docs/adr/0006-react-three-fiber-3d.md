# ADR 0006 — React Three Fiber para la escena 3D

## Estado

Propuesto.

## Contexto

La vista actual es un heatmap ECharts 2D. La nueva vista necesita composición de objetos,
jerarquía, selección, drill-down, labels y optimización.

## Decisión

Usar Three.js mediante React Three Fiber, con major compatible con React 18, y Drei como
utilidad. Mantener ECharts para históricos, heatmaps y fallback.

## Consecuencias

- verdadera escena 3D;
- integración declarativa con React;
- mayor costo de bundle y GPU;
- necesidad de instancing, LOD, labels controlados y fallback.
