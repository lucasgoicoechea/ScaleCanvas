# API Contract

Base: `/api/v1`

## Scenarios

- POST `/scenarios`
- GET `/scenarios`
- GET `/scenarios/{id}`
- PUT `/scenarios/{id}`
- DELETE `/scenarios/{id}`
- POST `/scenarios/{id}/duplicate`
- GET `/scenarios/{id}/export`
- POST `/scenarios/import`
- GET `/scenarios/{id}/cost-complexity`
- GET `/scenarios/{id}/cloud-cost`

## Evaluations

- POST `/evaluations`
- GET `/evaluations`
- GET `/evaluations/{id}`
- GET `/evaluations/scenario/{scenarioId}`
- GET `/evaluations/{id}/adr`

Request ejemplo:

```json
{
  "scenarioId": "uuid opcional",
  "scenario": {
    "name": "Portfolio SaaS",
    "productType": "SAAS_B2B",
    "...": "escenario cuantitativo completo"
  },
  "variants": ["BASELINE","GROWTH_X2","GROWTH_X10"]
}
```

`scenarioId` se envía cuando el escenario ya fue persistido. Si se omite, la evaluación es
ad hoc y el backend asigna un identificador independiente.

Response ejemplo:

```json
{
  "evaluationId": "uuid",
  "scenarioName": "Portfolio SaaS",
  "catalogVersion": "1.0.0",
  "generatedAt": "2026-07-18T19:00:00Z",
  "results": [...]
}
```

## Scenario versions

- POST `/scenario-versions/{scenarioId}`
- GET `/scenario-versions/{scenarioId}`

Request ejemplo:

```json
{
  "name": "Portfolio SaaS",
  "productType": "SAAS_B2B",
  ...
}
```

## Catalog

- GET `/rule-catalog`
- GET `/rule-catalog/versions`
- GET `/rule-catalog/versions/active`
- POST `/rule-catalog/versions/{id}/activate`

## Reports

- POST `/reports/markdown`

## AI explanation

- POST `/explanations`, opcional y sin autoridad para cambiar resultados.

## Infrastructure

- POST `/scenarios/{id}/deployment`
- GET `/scenarios/{id}/deployment`
- PUT `/scenarios/{id}/deployment`

Request ejemplo:

```json
{
  "serverType": "CONTAINER",
  "cloudProvider": "AWS",
  "deploymentService": "EKS",
  "gatewayType": "ALB",
  "loadBalancerType": "NLB",
  "minimumUnitMemoryMb": 4096,
  "minimumUnitCpuCount": 2,
  "serviceTopology": {
    "totalServices": 8,
    "microservicesCount": 4,
    "scalableServicesCount": 3,
    "services": [
      {
        "serviceName": "api-gateway",
        "requestsPerMinute": 1200,
        "memoryMb": 2048,
        "cpuCount": 1,
        "replicas": 3,
        "serverBinding": "SHARED"
      }
    ]
  }
}
```

## Scaling matrix

- GET `/scenarios/{id}/scaling-matrix`

Response ejemplo:

```json
{
  "serverType": "CONTAINER",
  "cloudProvider": "AWS",
  "minimumUnitMemoryMb": 4096,
  "minimumUnitCpuCount": 2,
  "services": ["api", "worker"],
  "maxReplicas": 3,
  "matrix": [
    { "serviceName": "api", "serverIndex": 0, "utilizationPercent": 50.0, "requestsPerMinuteShare": 400, "replicas": 3 },
    { "serviceName": "api", "serverIndex": 1, "utilizationPercent": 50.0, "requestsPerMinuteShare": 400, "replicas": 3 },
    { "serviceName": "worker", "serverIndex": 0, "utilizationPercent": 25.0, "requestsPerMinuteShare": 200, "replicas": 2 }
  ]
}
```

## Errores

Problem Details RFC 9457 con violations.

<!-- OBSERVABILITY-API:START -->
## Observability API

Base: `/api/v1/observability`

- GET `/providers`
- GET/POST `/connections`
- GET/DELETE `/connections/{id}`
- POST `/connections/{id}/test`
- POST `/connections/{id}/sync`
- GET `/snapshot`
- GET `/snapshot/simulated`

Los endpoints anteriores están implementados con almacenamiento de conexiones en memoria y
provider simulado. Métricas por recurso, CRUD de alertas y alarmas externas permanecen
propuestos en el contrato detallado.

Contrato completo en `docs/specs/OBS-001-cloud-observability/api-contract.md`.

El endpoint actual `/scenarios/{id}/scaling-matrix` es planificación y no debe convertirse
en endpoint de telemetría.
<!-- OBSERVABILITY-API:END -->
