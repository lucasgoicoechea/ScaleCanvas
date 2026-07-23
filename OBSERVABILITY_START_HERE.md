# ScaleCanvas Observability — punto de entrada obligatorio

## Propósito

Este documento convierte el pedido de observabilidad multi-cloud en un plan implementable
sin alterar todavía el código. El agente del IDE debe leerlo antes de trabajar sobre la
matriz cúbica, conectores cloud, métricas o alertas.

## Auditoría del estado actual

La revisión del repositorio recibido muestra:

1. `frontend/src/components/ScalingMatrix3D.tsx` no renderiza una escena tridimensional.
   Actualmente crea un **heatmap 2D de ECharts** con servicio en X, índice de réplica en Y
   y utilización expresada mediante color.
2. Esa utilización se calcula con datos declarados en el escenario, no con telemetría:
   `memoryMb / minimumUnitMemoryMb` y `cpuCount / minimumUnitCpuCount`.
3. `frontend/src/components/ScalingEqualizer.tsx` calcula capacidad agregada multiplicando
   recursos por réplicas. Eso puede servir como aproximación de planificación, pero no debe
   reutilizarse como utilización observada por instancia.
4. `backend/src/main/java/com/scalecanvas/scenario/api/ScalingController.java` contiene un
   endpoint placeholder que devuelve un mapa vacío.
5. Existen `MatrixCell` y `ScalingMatrixResponse`, pero el contrato actual solo contempla
   una utilización combinada y no distingue CPU, GPU, memoria, almacenamiento, I/O,
   frescura, procedencia ni alertas.
6. No existe todavía un SPI de proveedores de observabilidad.
7. No existen conectores AWS, GCP, Prometheus u OpenTelemetry.
8. No existe motor de alertas de observabilidad.
9. La documentación principal todavía presenta la lectura automática de métricas como
   fuera del MVP original. OBS-001 debe tratarse como una evolución del producto.
10. `backend/pom.xml` configura Java 25, mientras que varios documentos y reglas del agente
    todavía indican Java 21. No cambiar Java ni Spring Boot dentro de OBS-001. Primero se
    debe resolver o aceptar esa diferencia mediante una tarea explícita.

## Conclusión de factibilidad

### Factible

- Marco 3D por servidor/nodo/instancia.
- Columnas independientes para CPU, GPU, memoria y almacenamiento.
- Máximo, valor actual, porcentaje, unidad y timestamp.
- Capacidad libre translúcida y volumen usado proporcional a la presión.
- Estados azul/verde, amarillo y rojo.
- Servicios visualizados dentro de un host cuando la relación esté demostrada.
- Alertas locales de ScaleCanvas.
- Lectura de métricas y alarmas desde AWS y Google Cloud.
- Configuración multi-cuenta y multi-proyecto con credenciales por referencia.
- MCP para que una IA consulte snapshots normalizados.

### Factible con instrumentación adicional

- Memoria, filesystem, procesos y GPU dentro de una VM.
- Asociación precisa entre procesos/servicios y servidor.
- Límites reales de IOPS, throughput y cuotas dinámicas.
- Historial de alta resolución.

### No válido como promesa

- Predecir que un servicio “va a colapsar” solo por superar un porcentaje.
- Sumar CPU y GPU como si fueran la misma unidad.
- Multiplicar CPU × memoria × disco para obtener salud.
- Considerar ausencia de datos como 0 %.
- Usar MCP como canal principal de ingestión continua de telemetría.
- Inferir servicios internos de una VM sin metadatos, agente o mapping manual.

## Modelo visual decidido

Cada recurso agrupador tiene:

- marco exterior: capacidad disponible o límite conocido;
- columna CPU;
- columna GPU, solamente si existe capacidad GPU;
- columna memoria;
- columna almacenamiento;
- indicador separado de presión de I/O;
- etiquetas de capacidad, uso y porcentaje;
- nombre del recurso debajo;
- servicios hijos dentro o alrededor del agrupador;
- borde general según el peor estado relevante.

Semántica de colores:

- capacidad libre: azul/cian translúcido;
- carga saludable: verde;
- warning: amarillo;
- critical: rojo;
- stale: gris azulado;
- unknown: gris;
- máximo desconocido: violeta tenue y sin porcentaje inventado.

## Regla de salud

La geometría representa presión normalizada. El estado global usa el peor indicador relevante:

```text
computePressure = max(cpuRatio, gpuRatio)
storagePressure = max(capacityRatio, iopsPressure, throughputPressure, latencyPressure)
resourcePressure = max(computePressure, memoryRatio, storagePressure)
servicePressure = max(resourcePressure, errorPressure, latencyPressure, queuePressure, availabilityPressure)
```

No se debe presentar `servicePressure` como probabilidad científica de colapso.

## Orden de implementación obligatorio

1. `OBS-000`: inventario, tests base y reconciliación documental.
2. `OBS-001`: modelo provider-neutral.
3. `OBS-002`: proveedor simulado.
4. `OBS-003`: API de snapshots.
5. `OBS-004`: escena 3D con datos simulados.
6. `OBS-005`: estados UNKNOWN, STALE, NO_CAPACITY e histéresis.
7. `OBS-006`: alertas locales.
8. `OBS-007`: persistencia mínima y retención.
9. `OBS-008`: conector AWS read-only.
10. `OBS-009`: conector GCP read-only.
11. `OBS-010`: alarmas cloud read-only.
12. `OBS-011`: mapping de topología.
13. `OBS-012`: Prometheus/OpenTelemetry.
14. `OBS-013`: MCP de consulta sobre datos ya normalizados.
15. `OBS-014`: rendimiento, seguridad y revisión final.

## Primera instrucción para el agente

Usar una sesión nueva y pegar:

```text
Leé, en este orden:

1. AGENTS.md
2. OBSERVABILITY_START_HERE.md
3. docs/specs/OBS-001-cloud-observability/spec.md
4. docs/specs/OBS-001-cloud-observability/feasibility.md
5. docs/specs/OBS-001-cloud-observability/architecture.md
6. docs/specs/OBS-001-cloud-observability/metric-normalization.md
7. docs/specs/OBS-001-cloud-observability/tasks.md
8. docs/specs/OBS-001-cloud-observability/progress.md

Ejecutá solamente OBS-000.

No modifiques código todavía.
No agregues dependencias.
No conectes cuentas cloud.
No cambies Java ni Spring Boot.
Verificá el estado real del repositorio, ejecutá los tests existentes y actualizá
únicamente progress.md con hechos comprobados, comandos ejecutados, resultados,
inconsistencias y propuesta de cambio mínimo.

Detenete antes de implementar OBS-001.
```
