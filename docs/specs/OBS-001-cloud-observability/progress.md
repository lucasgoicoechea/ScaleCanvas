# Estado OBS-001

## Estado

`SIMULATED_PROVIDER_AND_UI_VERIFIED`

## Baseline histórico

- Existe frontend React 18.
- Existe ECharts.
- Originalmente no existían dependencias Three.js/React Three Fiber.
- `ScalingMatrix3D.tsx` es un heatmap 2D (para fallback).
- `ScalingEqualizer.tsx` usa datos declarados.
- Existen records parciales de matrix.
- Originalmente no existía el módulo observability.
- POM configura Java 25.

## Cambios de esta entrega

- Documentación priorizada para escena 3D real.
- `tasks.md` reordenado para OBS-001 → OBS-012 mínimo operable.
- `architecture.md` ajustado a `three` + snapshot API.
- Backend: corregido el error de compilación en `EvaluationService.java`.
- Backend: reparados imports en `AdrGenerator.java` y `EvaluationController.java`.
- Backend: compilación `mvn clean compile` confirmada en verde en este entorno.
- Frontend: agregados `ObservabilityScene3D` y `ObservabilityScene3DFallback`.
- Layout: ajustes en `.workspace`, `.topbar-actions`, `.scenario-panel`, `.results-heading` y media queries para descomprimir el panel izquierdo y evitar superposición de botones.
- Frontend: `ObservabilityScene3D` se abre ahora como modal desde un botón; `ObservabilityScene3DFallback` se mantiene inline.
- Jerarquía host→service, selección básica y labels acotadas en la escena 3D.
- Tipografía y espaciados mejorados para mayor legibilidad y sin botones/inputs encimados.
- Docs: actualizados `progress.md` y `qa-checklist.md` con el avance verificado.
- Backend: agregado modelo provider-neutral, normalización, alertas y provider simulado.
- Backend: agregado Snapshot API y administración de conexiones en memoria.
- Frontend: agregadas dependencias Three.js, React Three Fiber y Drei.
- Backend: `ScalingController` ya genera la matriz a partir del deployment.

## Próxima tarea

1. Asociar snapshots persistidos con escenarios/evaluaciones.
2. Agregar histórico de snapshots.
3. Implementar Prometheus read-only.
4. Implementar conectores cloud sólo después de completar persistencia y seguridad.

## Prohibido asumir

- que el backend compila después de cada cambio;
- que todos los tests pasan;
- que el endpoint scaling funciona;
- que hay credenciales cloud;
- que en el MVP se elimina la vista 2D previa.
