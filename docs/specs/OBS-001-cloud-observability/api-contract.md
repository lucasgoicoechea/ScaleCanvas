# Contrato API propuesto

Base:

```text
/api/v1/observability
```

## Providers

```http
GET /providers
```

Devuelve capabilities, no secretos.

## Connections

```http
POST /connections
GET /connections
GET /connections/{id}
PUT /connections/{id}
DELETE /connections/{id}
POST /connections/{id}/test
POST /connections/{id}/sync
```

Ejemplo:

```json
{
  "name": "aws-demo-readonly",
  "providerType": "AWS_CLOUDWATCH",
  "enabled": false,
  "readOnly": true,
  "credentialStrategy": "PROFILE",
  "secretReference": null,
  "accountOrProject": "123456789012",
  "regions": ["us-east-1"],
  "pollIntervalSeconds": 60,
  "options": {
    "profileName": "scalecanvas-readonly",
    "roleArn": null
  }
}
```

La API nunca retorna material secreto.

## Snapshot

```http
GET /snapshot?connectionId={id}&rootResourceId={id}&depth=3
GET /snapshot/simulated?profile=WARNING&seed=42
```

Respuesta resumida:

```json
{
  "snapshotId": "uuid",
  "generatedAt": "2026-07-19T15:00:00Z",
  "partial": false,
  "resources": [
    {
      "id": "host-1",
      "externalId": "i-123",
      "name": "payments-host",
      "type": "VM",
      "state": "WARNING",
      "dimensions": {
        "cpu": {
          "current": 3.1,
          "maximum": 4.0,
          "canonicalUnit": "CORE",
          "ratio": 0.775,
          "state": "WARNING",
          "timestamp": "2026-07-19T14:59:30Z"
        },
        "gpu": null,
        "memory": {
          "current": 12884901888,
          "maximum": 17179869184,
          "canonicalUnit": "BYTE",
          "ratio": 0.75,
          "state": "WARNING",
          "timestamp": "2026-07-19T14:59:30Z"
        },
        "storage": {
          "current": 64424509440,
          "maximum": 107374182400,
          "canonicalUnit": "BYTE",
          "ratio": 0.60,
          "state": "HEALTHY",
          "timestamp": "2026-07-19T14:59:30Z"
        }
      },
      "reasons": ["CPU warning for 5m"]
    }
  ],
  "relations": [
    {
      "parentId": "host-1",
      "childId": "service-payments",
      "relationType": "RUNS_ON",
      "source": "OTEL_RESOURCE",
      "confidence": "HIGH"
    }
  ],
  "alerts": []
}
```

## History

```http
GET /resources/{resourceId}/metrics/{metricKey}?from=&to=&step=
```

Aplicar límites de rango y cantidad de puntos.

## Local alerts

```http
POST /alert-rules
GET /alert-rules
PUT /alert-rules/{id}
DELETE /alert-rules/{id}
GET /alerts?state=FIRING
POST /alerts/{id}/acknowledge
```

## Imported cloud alarms

```http
GET /connections/{id}/external-alarms
```

Read-only en primera fase.

## Error model

RFC 9457 con códigos canónicos:

- OBS_CONNECTION_INVALID
- OBS_AUTHENTICATION_FAILED
- OBS_PERMISSION_DENIED
- OBS_PROVIDER_THROTTLED
- OBS_TIMEOUT
- OBS_PARTIAL_RESULT
- OBS_METRIC_UNAVAILABLE
- OBS_CAPACITY_UNKNOWN
- OBS_RESOURCE_NOT_FOUND
