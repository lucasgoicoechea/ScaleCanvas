# Testing Strategy

## Backend

- Unit tests para value objects, fórmulas, reglas, conflictos y priorización.
- Property tests para porcentajes, crecimiento y determinismo.
- Testcontainers para PostgreSQL.
- Tests HTTP y migraciones.
- Golden scenarios.

## Frontend

- Vitest y Testing Library.
- Playwright para crear, evaluar, comparar y exportar.

## Gate

Ninguna regla sin test triggered, not-triggered e insufficient-data.

<!-- OBSERVABILITY-TESTING:START -->
## Tests OBS

Backend:

- normalización de unidades;
- ratio/max;
- unknown/stale/no-capacity;
- histéresis;
- provider simulated determinista;
- contract tests AWS/GCP con fixtures;
- partial result;
- throttling;
- serialización sin secretos;
- topología y confidence.

Frontend:

- Snapshot -> SceneModel;
- color y estado independiente;
- selección;
- hierarchy;
- labels;
- fallback;
- accessibility;
- performance 250 unidades.

No ejecutar tests cloud contra cuentas productivas.
<!-- OBSERVABILITY-TESTING:END -->
