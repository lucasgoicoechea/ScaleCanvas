# Modelo de dominio de observabilidad

## ProviderConnection

```text
id
name
providerType
enabled
readOnly
credentialStrategy
secretReference
accountId/projectId
regions/zones
pollInterval
timeout
labels
lastSuccessfulSyncAt
lastFailureAt
connectionState
```

`secretReference` identifica una credencial externa; nunca contiene el secreto.

## ObservedResource

```text
id
connectionId
externalId
resourceType
name
provider
accountOrProject
region
zone
labels
capacityMetadata
discoveredAt
lastSeenAt
lifecycleState
```

Tipos iniciales:

- ACCOUNT
- PROJECT
- REGION
- ZONE
- HOST
- VM
- NODE
- CLUSTER
- CONTAINER
- POD
- SERVICE
- PROCESS
- SERVERLESS_SERVICE
- DATABASE
- STORAGE_VOLUME
- GPU_DEVICE

## ResourceRelation

```text
parentId
childId
relationType
source
confidence
effectiveFrom
effectiveTo
```

Relaciones:

- CONTAINS
- RUNS_ON
- MEMBER_OF
- ATTACHED_TO
- DEPENDS_ON
- EXPOSES
- MANUAL_MAPPING

## MetricSample

```text
resourceId
canonicalKey
timestamp
valueOriginal
unitOriginal
valueCanonical
unitCanonical
source
quality
dimensions
```

Calidad:

- MEASURED
- DERIVED
- DECLARED
- ESTIMATED
- MISSING
- STALE

## ResourceCapacity

```text
cpuCapacityCores
gpuCapacityCount
gpuMemoryBytes
memoryCapacityBytes
storageCapacityBytes
iopsLimit
throughputBytesPerSecondLimit
source
timestamp
```

## DimensionSnapshot

```text
key
current
maximum
ratio
visualRatio
originalUnit
canonicalUnit
state
timestamp
staleAfter
reasons[]
subDimensions[]
```

## ObservabilitySnapshot

```text
snapshotId
connectionId
generatedAt
sourceWindow
resources[]
relations[]
alerts[]
dataQualitySummary
partial
warnings[]
```

## AlertRule

```text
id
name
scopeSelector
metricKey
operator
warningThreshold
criticalThreshold
aggregation
window
minimumSamples
enterDuration
exitDuration
cooldown
noDataPolicy
enabled
notificationTargets
```

## AlertInstance

```text
id
ruleId
resourceId
state
openedAt
updatedAt
resolvedAt
lastValue
threshold
reason
source
externalAlarmId
```
