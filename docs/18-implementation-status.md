# Implementation Status

Última reconciliación con el código fuente: 2026-07-23.

Este documento describe el estado observado en el repositorio. Una funcionalidad presente
en código pero que no compila o que sólo usa datos simulados se marca explícitamente.

## Implementado

- Monorepo con backend, frontend y Docker Compose.
- Escenarios manuales.
- Validaciones de porcentajes, RPS y percentiles.
- Cálculos de capacidad.
- Variantes BASELINE, GROWTH_X2 y GROWTH_X10.
- Catálogo determinista de reglas.
- Recomendaciones Do now, Watch y Not yet.
- Persistencia básica de escenarios.
- API OpenAPI/Swagger.
- Dashboard React.
- React Flow y ECharts.
- Exportación Markdown.
- Tests unitarios y HTTP básicos.
- Proveedor Ollama opcional para explicaciones.
- DeploymentProfile: servidor, cloud, gateway, balanceador, unidad mínima.
- ServiceTopology: servicios, microservicios, capacidad por servicio.
- Scaling equalizer (equalizador visual de capacidad por servicio).
- Scaling matrix en ECharts: servicios × servidores × utilización. El endpoint backend ya
  calcula celdas a partir del perfil de despliegue; esta matriz continúa siendo 2D.
- Persistencia de evaluaciones y resultados por variante.
- Versionado de escenarios con snapshots.
- Historial de evaluaciones por escenario.
- Frontend: panel de historial de evaluaciones.
- Frontend: panel de catálogo de reglas.
- Lombok en backend para reducir boilerplate.
- OpenAPI/Swagger mejorado con seguridad, tags y operaciones de actuator.
- Logging estructurado configurado.
- Observabilidad: Actuator, health indicators, métricas y Prometheus.
- Contratos OpenAI/Ollama con SPI para proveedores de IA.
- UI de persistencia: guardar, cargar y borrar escenarios.
- Importación, exportación y duplicación de escenarios.
- Estimación de costo cloud basada en catálogo interno.
- Matriz costo/complejidad.
- Generación de ADR a partir de una evaluación.
- Catálogo de reglas con metadata de versiones y activación.
- Modelo provider-neutral de observabilidad.
- Normalización de métricas y estados de calidad.
- Provider simulado determinista.
- Snapshot API de observabilidad.
- Escena 3D experimental con Three.js/React Three Fiber.
- Fallback 2D de observabilidad con ECharts.
- Modelo y evaluación local de alertas.
- Sincronización consistente del provider simulado: actualiza conexión y devuelve snapshot.
- Protección opcional de mutaciones mediante `X-ScaleCanvas-Key`.
- Wizard dividido por secciones funcionales.
- Ejecución local sin Docker mediante `scripts/run-local.ps1`.
- Pipeline de CI para backend y frontend mediante GitHub Actions.
- Licencia MIT y guion de demostración para portfolio.

## Reglas incluidas

- Modular monolith baseline.
- Horizontal replicas.
- Async queue.
- Distributed cache.
- Read replica.
- Object storage.
- CDN/global delivery.
- Multi-AZ availability.
- Serverless bursty workloads.
- Kubernetes operational trigger.
- Kafka not yet.
- Sharding not yet.
- Container minimum unit sizing.
- Serverless trigger for low-traffic fleets.
- Gateway sizing for multi-service ingress.
- Load balancer recommendation for replicated services.

## Pendiente para iteraciones futuras

- Autenticación.
- Catálogo externo YAML versionado.
- Golden master de diez escenarios.
- Integraciones funcionales GitHub, SonarQube, Prometheus y OpenTelemetry.
- Cálculo de costos con catálogos cloud externos y versionados.
- Exportación PNG/SVG del diagrama.
- Persistencia de conexiones y snapshots de observabilidad.
- Providers AWS y GCP read-only.
- Autenticación, autorización y auditoría.
- Catálogo de reglas funcionalmente versionado; la activación actual administra metadata,
  pero las reglas ejecutables continúan compiladas en Java.

<!-- OBSERVABILITY-REAL-STATUS:START -->
## Estado real de observabilidad cloud

Implementado:

- modelo provider-neutral;
- provider simulado determinista;
- snapshots con recursos, relaciones, métricas, alertas y calidad de datos;
- CPU, GPU, memoria y storage como dimensiones independientes;
- máximos y ratios cuando existe capacidad válida;
- estados `STALE`, `UNKNOWN` y `NO_CAPACITY`;
- evaluación local de alertas;
- escena Three.js experimental y fallback 2D;
- endpoints para listar providers, administrar conexiones en memoria y consultar snapshots.

Parcial o experimental:

- la UI 3D siempre carga un snapshot simulado y no está asociada al escenario evaluado;
- las conexiones se almacenan en memoria y se pierden al reiniciar;
- `PROMETHEUS`, `OPENTELEMETRY`, AWS, Azure, GCP, Kubernetes y MCP aparecen como contratos,
  no como conectores productivos;
- el fallback inline recibe actualmente un snapshot vacío.

No implementado:

- SDKs y conectores AWS/GCP;
- ingestión Prometheus;
- topología OpenTelemetry real;
- persistencia e histórico de snapshots;
- MCP de consulta;
- manejo productivo de credenciales por referencia.

## Salud de build verificada

- Backend: 53 tests ejecutados correctamente el 2026-07-23.
- Frontend: `tsc -b` ejecutado correctamente el 2026-07-23.
- Frontend: Vitest (1 test) y build de producción Vite ejecutados correctamente el
  2026-07-23 mediante `configLoader=runner`.
- Tests presentes: 21 archivos Java y 1 archivo de tests frontend.
- Smoke test local sin Docker: backend `UP`, frontend HTTP 200 y 8 providers declarados.

## Riesgos funcionales conocidos

- La versión activa del catálogo queda estampada en evaluaciones nuevas. Las reglas
  ejecutables todavía pertenecen al catálogo Java incorporado.

## Correcciones de estabilización 2026-07-23

- La entidad, el payload y la respuesta de una evaluación reutilizan el mismo
  `evaluationId` y `generatedAt`.
- `EvaluationRequest` acepta un `scenarioId` opcional.
- El frontend conserva el identificador del escenario cargado, creado, duplicado o importado
  y lo envía al evaluar, de modo que el timeline queda asociado al escenario persistido.
- Se agregó cobertura de identidad y asociación en `EvaluationServiceTest`.
- Se eliminó la dependencia duplicada de validation en Maven.
- Se corrigieron los errores TypeScript de templates y observabilidad.
- Vite build y Vitest ejecutados correctamente mediante `configLoader=runner`.
- El bundle inicial se separó en chunks de aplicación, React, React Flow, Three.js y ECharts.
- Se corrigió el registro de `IntegrationProperties`.
- Se eliminó la creación duplicada de `CloudPricingCatalog`.
- Smoke test del JAR: health `UP`, catálogo con 16 reglas y 8 providers declarados.
- Docker Compose no pudo ejecutarse porque Docker no está instalado en el entorno de
  reconciliación.
- El provider `SIMULATED` es el único marcado `IMPLEMENTED`; los otros siete se exponen como
  `CONTRACT_ONLY` y no anuncian capacidades disponibles.
- Se agregaron `scripts/run-local.ps1`, `scripts/test-all.ps1`, CI, licencia MIT y
  `docs/PORTFOLIO_DEMO.md` para una presentación reproducible en GitHub y LinkedIn.
<!-- OBSERVABILITY-REAL-STATUS:END -->
