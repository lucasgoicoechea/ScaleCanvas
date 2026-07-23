export type ProviderType =
  | 'SIMULATED'
  | 'AWS_CLOUDWATCH'
  | 'GCP_CLOUD_MONITORING'
  | 'PROMETHEUS'
  | 'OPENTELEMETRY'
  | 'KUBERNETES'
  | 'AZURE_MONITOR'
  | 'MCP_QUERY_ADAPTER'

export type ResourceType =
  | 'ACCOUNT'
  | 'PROJECT'
  | 'REGION'
  | 'ZONE'
  | 'HOST'
  | 'VM'
  | 'NODE'
  | 'CLUSTER'
  | 'CONTAINER'
  | 'POD'
  | 'SERVICE'
  | 'PROCESS'
  | 'SERVERLESS_SERVICE'
  | 'DATABASE'
  | 'STORAGE_VOLUME'
  | 'GPU_DEVICE'

export type MetricQuality = 'MEASURED' | 'DERIVED' | 'DECLARED' | 'ESTIMATED' | 'MISSING' | 'STALE'

export type ResourceLifecycleState =
  | 'HEALTHY'
  | 'WARNING'
  | 'CRITICAL'
  | 'UNKNOWN'
  | 'STALE'
  | 'NO_CAPACITY'
  | 'DISABLED'

export type AlertInstanceState =
  | 'PENDING'
  | 'FIRING_WARNING'
  | 'FIRING_CRITICAL'
  | 'ACKNOWLEDGED'
  | 'RESOLVED'
  | 'SUPPRESSED'
  | 'UNKNOWN'

export type RelationType = 'CONTAINS' | 'RUNS_ON' | 'MEMBER_OF' | 'ATTACHED_TO' | 'DEPENDS_ON' | 'EXPOSES' | 'MANUAL_MAPPING'

export type DimensionKey = 'CPU' | 'GPU' | 'MEMORY' | 'STORAGE' | 'IOPS' | 'THROUGHPUT' | 'LATENCY' | 'QUEUE' | 'ERROR_RATE' | 'AVAILABILITY' | 'REQUEST_RATE' | 'SATURATION'

export type Unit = 'CORE' | 'MILLI_CORE' | 'BYTE' | 'KIBIBYTE' | 'MEBIBYTE' | 'GIBIBYTE' | 'TEBIBYTE' | 'IOPS' | 'BYTES_PER_SECOND' | 'MILLISECOND' | 'PERCENT' | 'COUNT' | 'UNKNOWN'

export type ConnectionState = 'DISCONNECTED' | 'CONNECTING' | 'CONNECTED' | 'FAILED' | 'PARTIAL'

export type SimulatedProfile = 'NORMAL' | 'WARNING' | 'CRITICAL' | 'STALE' | 'UNKNOWN' | 'OVER_CAPACITY' | 'MIXED_HIERARCHY'

export interface ResourceCapacity {
  cpuCapacityCores: number
  gpuCapacityCount: number
  gpuMemoryBytes: number
  memoryCapacityBytes: number
  storageCapacityBytes: number
  iopsLimit: number
  throughputBytesPerSecondLimit: number
  source: string
  timestamp: string
}

export interface MetricSample {
  resourceId: string
  canonicalKey: DimensionKey
  timestamp: string
  valueOriginal: number
  unitOriginal: Unit
  valueCanonical: number
  unitCanonical: Unit
  source: string
  quality: MetricQuality
  dimensions: Record<string, string>
}

export interface ResourceRelation {
  parentId: string
  childId: string
  relationType: RelationType
  source: string
  confidence: 'HIGH' | 'MEDIUM' | 'LOW'
  effectiveFrom: string
  effectiveTo?: string
}

export interface DimensionSnapshot {
  key: DimensionKey
  current: number | null
  maximum: number | null
  ratio: number | null
  visualRatio: number
  originalUnit: Unit
  canonicalUnit: Unit
  state: ResourceLifecycleState
  timestamp: string
  staleAfter: string
  reasons: string[]
  subDimensions: DimensionSnapshot[]
}

export interface ObservedResource {
  id: string
  connectionId: string
  externalId: string
  resourceType: ResourceType
  name: string
  provider: ProviderType
  accountOrProject: string
  region: string
  zone: string
  labels: Record<string, string>
  capacityMetadata: ResourceCapacity
  discoveredAt: string
  lastSeenAt: string
  lifecycleState: ResourceLifecycleState
}

export interface AlertRule {
  id: string
  name: string
  scopeSelector: string
  metricKey: DimensionKey
  operator: 'GT' | 'GTE' | 'LT' | 'LTE' | 'EQ' | 'NEQ'
  warningThreshold: number
  criticalThreshold: number
  aggregation: 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'P95' | 'P99' | 'COUNT'
  window: string
  minimumSamples: number
  enterDuration: string
  exitDuration: string
  cooldown: string
  noDataPolicy: 'IGNORE' | 'UNKNOWN' | 'WARNING' | 'CRITICAL'
  enabled: boolean
  notificationTargets: string[]
}

export interface AlertInstance {
  id: string
  ruleId: string
  resourceId: string
  state: AlertInstanceState
  openedAt: string
  updatedAt: string
  resolvedAt?: string
  lastValue: number
  threshold: number
  reason: string
  source: string
  externalAlarmId?: string
}

export interface ProviderConnection {
  id: string
  name: string
  providerType: ProviderType
  enabled: boolean
  readOnly: boolean
  credentialStrategy: string
  secretReference?: string
  accountOrProject: string
  regions: string[]
  zones?: string[]
  pollIntervalSeconds: number
  timeoutSeconds: number
  labels: Record<string, string>
  lastSuccessfulSyncAt?: string
  lastFailureAt?: string
  connectionState: ConnectionState
  options?: Record<string, string>
}

export interface ObservabilitySnapshot {
  snapshotId: string
  connectionId: string
  generatedAt: string
  sourceWindow: string
  resources: ObservedResource[]
  relations: ResourceRelation[]
  alerts: AlertInstance[]
  dataQualitySummary: Record<string, number>
  partial: boolean
  warnings: string[]
}

export interface SceneModelResource {
  id: string
  name: string
  type: ResourceType
  state: ResourceLifecycleState
  position: [number, number, number]
  dimensions: Record<DimensionKey, DimensionSnapshot>
  capacity: ResourceCapacity
  children: SceneModelResource[]
  labels: string[]
}

export interface SceneModel {
  snapshotId: string
  generatedAt: string
  resources: SceneModelResource[]
  relations: ResourceRelation[]
  alerts: AlertInstance[]
  camera: {
    position: [number, number, number]
    target: [number, number, number]
  }
}
