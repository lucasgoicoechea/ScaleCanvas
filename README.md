# ScaleCanvas

[![CI](https://github.com/lucasgoicoechea/ScaleCanvas/actions/workflows/ci.yml/badge.svg)](https://github.com/lucasgoicoechea/ScaleCanvas/actions/workflows/ci.yml)
![Java 25](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot 3.5](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-TypeScript-61DAFB?logo=react&logoColor=black)
![License MIT](https://img.shields.io/badge/license-MIT-blue)

ScaleCanvas es un laboratorio de arquitectura de software que transforma un escenario
cuantitativo en decisiones justificadas y reproducibles.

El usuario describe demanda, crecimiento, datos, objetivos de disponibilidad y latencia,
madurez operativa, presupuesto y topología de despliegue. ScaleCanvas calcula capacidad,
compara el escenario actual con crecimientos x2 y x10, ejecuta reglas deterministas y
explica qué conviene hacer ahora, qué observar y qué complejidad todavía no está
justificada.

La IA es opcional y sólo explica resultados existentes. No modifica cálculos ni decide la
arquitectura.

## Por qué este proyecto

Este proyecto demuestra diseño de dominio, modelado cuantitativo, arquitectura modular,
APIs REST, persistencia, visualización avanzada y documentación de decisiones técnicas.
El foco no está en recomendar tecnología por moda, sino en convertir restricciones medibles
en decisiones auditables.

Puntos destacados:

- motor determinista con 16 reglas arquitectónicas versionadas;
- simulación comparable para escenario actual, crecimiento x2 y crecimiento x10;
- trazabilidad desde cada recomendación hasta su evidencia y threshold;
- frontend interactivo con diagramas, matrices, gráficos y una escena 3D experimental;
- backend modular con persistencia, Flyway, OpenAPI, métricas y pruebas automatizadas;
- ejecución local completa sin Docker y alternativa Docker/PostgreSQL conservada;
- contratos provider-neutral preparados para extender observabilidad sin fingir integraciones.

## Qué problema resuelve

Decisiones como “usar microservicios”, “agregar Kafka” o “migrar a Kubernetes” suelen
presentarse como recetas sin métricas. ScaleCanvas obliga a vincular cada recomendación con:

- una regla y versión identificables;
- un threshold explícito;
- evidencia obtenida del escenario;
- beneficios y trade-offs;
- una alternativa más simple;
- una clasificación `Do now`, `Watch` o `Not yet`.

Está orientado a backend engineers, arquitectos, tech leads, SREs y estudiantes avanzados.

## Funcionalidades

### Escenarios

- Wizard dividido en identidad, demanda, datos/calidad, infraestructura y acciones.
- Perfiles de workload, datos, calidad, organización y despliegue.
- Topología y capacidad por servicio.
- Golden master scenarios.
- Crear, cargar, editar, duplicar y eliminar escenarios.
- Importar y exportar JSON.
- Versiones y snapshots de escenarios.

### Evaluación determinista

- Comparación `BASELINE`, `GROWTH_X2` y `GROWTH_X10`.
- Cálculo de requests, transferencia, lecturas/escrituras, almacenamiento y downtime budget.
- 16 reglas arquitectónicas incorporadas.
- Recomendaciones con evidencia, threshold, beneficios, trade-offs y alternativa simple.
- Riesgos de disponibilidad, soporte operativo, RPO y crecimiento de datos.
- Identidad consistente entre evaluación persistida, payload y respuesta.
- Asociación de evaluaciones con escenarios guardados.
- Historial y timeline.

### Visualización y entregables

- Diagrama de arquitectura con React Flow.
- Métricas y gráficos con ECharts.
- Radar de atributos de calidad.
- Capacity chart y comparador de variantes.
- Scaling equalizer y scaling matrix.
- Matrices de costo/complejidad y costo cloud aproximado.
- Informe Markdown.
- ADR generado desde una evaluación.

### Observabilidad experimental

- Modelo provider-neutral.
- CPU, GPU, memoria y storage como dimensiones independientes.
- Calidad de métricas y estados `STALE`, `UNKNOWN` y `NO_CAPACITY`.
- Provider simulado determinista.
- Snapshot API.
- Alertas locales con histéresis.
- Escena 3D con Three.js/React Three Fiber.
- Fallback 2D con ECharts.
- Administración de conexiones en memoria.

La observabilidad utiliza datos simulados. AWS, GCP, Prometheus y OpenTelemetry reales
continúan en el roadmap.

| Provider | Estado | Capacidades activas |
|---|---|---|
| Simulated | Implementado | Discovery, métricas y alertas |
| AWS CloudWatch | Contrato solamente | Ninguna |
| GCP Cloud Monitoring | Contrato solamente | Ninguna |
| Prometheus | Contrato solamente | Ninguna |
| OpenTelemetry | Contrato solamente | Ninguna |
| Kubernetes, Azure y MCP | Contrato solamente | Ninguna |

Los conectores incompletos se conservan como interfaces y documentación de diseño. La API
no anuncia capacidades que todavía no existen.

## Arquitectura

ScaleCanvas es un monolito modular:

```text
React + TypeScript
        |
        | /api/v1
        v
Spring Boot
  ├─ scenarios
  ├─ capacity
  ├─ rules
  ├─ evaluations
  ├─ reports
  ├─ observability
  └─ optional AI explanations
        |
        v
PostgreSQL / H2 + Flyway
```

Stack:

- Java 25 y Spring Boot 3.5.
- React 18, TypeScript y Vite 6.
- PostgreSQL 16; H2 para desarrollo y pruebas.
- Spring Data JPA y Flyway.
- React Flow, ECharts, Three.js y React Three Fiber.
- Actuator, Micrometer, Prometheus y OpenTelemetry.
- Ollama opcional.

## Probar la demo

### Demo local recomendada

No requiere Docker ni PostgreSQL. Usa H2 en memoria y levanta backend y frontend con un
solo comando:

```powershell
.\scripts\run-local.ps1
```

El script verifica Java, Maven, Node y npm; instala dependencias si faltan, construye el
backend, espera el health check y abre la aplicación. `Ctrl+C` detiene ambos procesos.

Opciones:

```powershell
.\scripts\run-local.ps1 -NoBrowser
.\scripts\run-local.ps1 -SkipInstall
```

### Docker Compose

Docker se mantiene como alternativa de despliegue; no es necesario para la demo.

```powershell
Copy-Item .env.example .env
docker compose up --build
```

Abrir:

- Aplicación: http://localhost:3000
- Swagger: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health
- Prometheus: http://localhost:8080/actuator/prometheus

### Desarrollo local

Backend con H2:

```powershell
mvn -f backend/pom.xml spring-boot:run
```

Frontend:

```powershell
npm --prefix frontend install
npm --prefix frontend run dev
```

El servidor Vite publica el frontend en http://localhost:5173 y redirige `/api` al backend.

## Verificación

```powershell
mvn -f backend/pom.xml test
npm --prefix frontend test -- --run
npm --prefix frontend run build
```

Si esbuild no puede cargar `vite.config.ts` por restricciones del host:

```powershell
frontend\node_modules\.bin\vitest.cmd run --configLoader runner
frontend\node_modules\.bin\vite.cmd build --configLoader runner
```

Estado verificado el 2026-07-23:

- 53 tests backend aprobados.
- Test frontend aprobado.
- TypeScript aprobado.
- Build frontend de producción aprobado.
- JAR iniciado con H2 y Flyway.
- Health `UP`.
- Docker Compose no ejecutado en el entorno de verificación porque Docker no estaba
  instalado.

## Seguridad

Por defecto el modo local no requiere credenciales. Para proteger todas las mutaciones:

```dotenv
MUTATION_API_KEY=una-clave-larga-y-aleatoria
```

Los requests `POST`, `PUT` y `DELETE` bajo `/api/**` deberán incluir:

```http
X-ScaleCanvas-Key: una-clave-larga-y-aleatoria
```

Esta protección es apropiada para una demo controlada. Autenticación por usuario, roles y
auditoría permanecen pendientes antes de una exposición pública.

Nunca se deben persistir secretos cloud. Las conexiones de observabilidad trabajan con
referencias y están diseñadas como read-only.

## API principal

- `POST /api/v1/evaluations`
- `GET /api/v1/evaluations`
- `GET /api/v1/evaluations/{id}`
- `GET /api/v1/evaluations/scenario/{scenarioId}`
- CRUD `/api/v1/scenarios`
- `GET /api/v1/rule-catalog`
- `GET /api/v1/rule-catalog/versions`
- `POST /api/v1/reports/markdown`
- `POST /api/v1/explanations`
- `GET /api/v1/observability/snapshot/simulated`
- `GET/POST /api/v1/observability/connections`

El contrato completo está en `docs/08-api-contract.md`.

## Límites actuales

- Los costos cloud son aproximaciones internas, no cotizaciones.
- Las reglas ejecutables están compiladas en Java.
- Activar una versión cambia la versión estampada en evaluaciones nuevas, pero todavía no
  carga otro catálogo externo de reglas.
- Las conexiones y snapshots de observabilidad no se persisten.
- La escena 3D consume telemetría simulada.
- No se aprovisiona infraestructura ni se genera IaC de producción.

## Roadmap

1. Persistencia e histórico de observabilidad asociados a escenarios.
2. Catálogo externo de reglas y thresholds versionados.
3. Prometheus y topología OpenTelemetry read-only.
4. Conectores AWS/GCP read-only con credenciales por referencia.
5. Autenticación, roles y auditoría.
6. Más cobertura frontend y pruebas E2E.

## Portfolio

El recorrido sugerido para video, capturas y publicación está en
`docs/PORTFOLIO_DEMO.md`.

El repositorio incluye GitHub Actions para backend tests, typecheck, frontend tests y build.

### Qué observar en una demo

1. Completar o cargar un escenario.
2. Comparar `BASELINE`, `GROWTH_X2` y `GROWTH_X10`.
3. Revisar cómo cada recomendación expone regla, evidencia y alternativa simple.
4. Explorar el diagrama, las matrices de capacidad y costo, y el timeline.
5. Abrir la vista de observabilidad simulada y distinguirla de los conectores contractuales.

## Documentación

- `docs/01-product-spec.md`: visión del producto.
- `docs/05-capacity-model.md`: fórmulas.
- `docs/06-rule-engine.md`: motor determinista.
- `docs/08-api-contract.md`: endpoints.
- `docs/11-security.md`: postura de seguridad.
- `docs/13-observability.md`: observabilidad propia y de sistemas.
- `docs/18-implementation-status.md`: estado real y limitaciones.
- `docs/adr/`: decisiones arquitectónicas.
