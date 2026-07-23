# Domain Model

## Aggregates

### ArchitectureScenario

- id, name, description, productType.
- workloadProfile, dataProfile, qualityProfile.
- organizationProfile, constraints.
- deploymentProfile, serviceTopology.
- ruleCatalogVersion, timestamps.

### WorkloadProfile

- registeredUsers, dailyActiveUsers, concurrentUsers.
- averageRps, peakRps, burstFactor.
- readPercentage, writePercentage.
- averagePayloadBytes, maximumPayloadBytes.
- batchJobsPerDay, asynchronousWorkPercentage.

### DataProfile

- currentStorageBytes, monthlyGrowthPercentage.
- retentionMonths, objectStorageBytes.
- hotDataPercentage, relationalDataPercentage.
- eventVolumePerDay.

### QualityProfile

- targetP50/P95/P99.
- availabilitySlo, rto, rpo.
- consistencyLevel, durabilityLevel, geographicScope.

### OrganizationProfile

- teamSize, operationsMaturity, deploymentFrequency.
- onCallAvailable, observabilityMaturity.
- cloudExperience, budgetBand.

### DeploymentProfile

- serverType (VM, CONTAINER, SERVERLESS, BARE_METAL).
- cloudProvider (AWS, GCP, AZURE, ON_PREM, HYBRID).
- deploymentService (ECS, EKS, LAMBDA, APP_SERVICE, VM_MANUAL, CLOUD_RUN, OTHER).
- gatewayType (API_GATEWAY, ALB, NLB, CLOUDFLARE, KONG, NGINX, NONE).
- loadBalancerType (ALB, NLB, CLB, CLOUDFLARE, KONG, NGINX, NONE).
- minimumUnitMemoryMb, minimumUnitCpuCount.

### ServiceTopology

- totalServices, microservicesCount.
- scalableServicesCount.
- services: lista de ServiceCapacity.

### ServiceCapacity

- serviceName.
- requestsPerMinute.
- memoryMb, cpuCount.
- replicas.
- serverBinding (SHARED, DEDICATED, SERVERLESS).

### EvaluationResult

- scenarioId, catalogVersion, derivedMetrics.
- findings, recommendations, rejectedRecommendations, risks.
- capacityGaps, scalingMatrix.

### Recommendation

- id, componentType, action, urgency, confidence.
- ruleId, evidence, benefits, tradeoffs.
- prerequisites, alternatives.

## Value Objects

RequestsPerSecond, Percentage, Money, DataVolume, LatencyTarget, AvailabilityTarget, RecoveryObjective, Evidence, Threshold, Confidence, ServerType, CloudProvider, DeploymentService, GatewayType, LoadBalancerType, ServiceBinding.

<!-- OBSERVABILITY-DOMAIN:START -->
## Observability bounded module

Agregar sin contaminar `ArchitectureScenario`:

- ProviderConnection.
- ObservedResource.
- ResourceRelation.
- MetricDefinition.
- MetricSample.
- ResourceCapacity.
- DimensionSnapshot.
- ObservabilitySnapshot.
- AlertRule.
- AlertInstance.

Los detalles están en `docs/specs/OBS-001-cloud-observability/domain-model.md`.
<!-- OBSERVABILITY-DOMAIN:END -->
