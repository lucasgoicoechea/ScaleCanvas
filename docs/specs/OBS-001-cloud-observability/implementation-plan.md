# Plan de implementación

## Fase 0 — Base confiable

- crear branch;
- ejecutar tests backend/frontend;
- verificar Java/Spring/Maven/Node;
- registrar baseline;
- confirmar que scaling endpoint es placeholder;
- confirmar que la vista actual es heatmap 2D;
- resolver decisión Java 25 vs documentación Java 21;
- no tocar observabilidad todavía.

## Fase 1 — Modelo canónico

- enums;
- value objects;
- snapshots;
- unidades;
- estados;
- frescura;
- provider SPI;
- tests puros.

## Fase 2 — Simulado

- perfiles deterministas;
- jerarquía host → services;
- missing/stale;
- overflow;
- alarmas simuladas;
- endpoint snapshot.

## Fase 3 — Escena 3D

- instalar dependencias aprobadas;
- `Snapshot -> SceneModel`;
- capacity shell;
- resource columns;
- labels;
- selección;
- legend;
- fallback 2D;
- pruebas.

## Fase 4 — Alertas locales

- reglas;
- ventanas;
- histéresis;
- no-data;
- persistencia;
- UI.

## Fase 5 — AWS read-only

- test de conexión;
- identidad;
- discovery;
- GetMetricData;
- alarmas;
- partial results;
- throttling;
- agente requerido para métricas in-guest.

## Fase 6 — GCP read-only

- ADC;
- discovery;
- timeSeries;
- policies/incidents;
- partial results;
- Ops Agent requirements.

## Fase 7 — Topología

- tags;
- OTel;
- ECS;
- Kubernetes posterior;
- manual mapping;
- confidence.

## Fase 8 — MCP

- tools/resources read-only;
- authorization;
- snapshots normalizados;
- sin polling;
- sin mutation.

## Fase 9 — Hardening

- performance;
- security;
- quotas;
- cache;
- retention;
- accessibility;
- e2e;
- docs.
