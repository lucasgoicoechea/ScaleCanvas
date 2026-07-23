import type {
  DimensionKey,
  DimensionSnapshot,
  ObservedResource,
  ObservabilitySnapshot,
  ResourceCapacity,
  ResourceRelation,
  ResourceType,
  SceneModel,
  SceneModelResource,
} from './observabilityTypes'

export type { SceneModel, SceneModelResource }

const LAYOUT_RADIUS = 8
const LEVEL_HEIGHT = 4

function dimensionState(resource: ObservedResource, key: DimensionKey): DimensionSnapshot {
  const found = resource.labels && resource.labels['__dimensions'] ? JSON.parse(resource.labels['__dimensions']) : {}
  if (found[key]) return found[key] as DimensionSnapshot
  return {
    key,
    current: null,
    maximum: null,
    ratio: null,
    visualRatio: 0,
    originalUnit: 'UNKNOWN',
    canonicalUnit: 'UNKNOWN',
    state: 'UNKNOWN',
    timestamp: resource.lastSeenAt,
    staleAfter: resource.lastSeenAt,
    reasons: ['Missing telemetry'],
    subDimensions: [],
  }
}

function resourcePosition(type: ResourceType, index: number, total: number): [number, number, number] {
  const angle = (index / Math.max(total, 1)) * Math.PI * 2
  const radius = type === 'HOST' || type === 'VM' || type === 'NODE' ? LAYOUT_RADIUS : LAYOUT_RADIUS * 0.5
  return [Math.cos(angle) * radius, 0, Math.sin(angle) * radius]
}

function resourceLevel(type: ResourceType): number {
  if (type === 'ACCOUNT' || type === 'PROJECT') return 2
  if (type === 'REGION' || type === 'ZONE') return 1
  if (type === 'HOST' || type === 'VM' || type === 'NODE' || type === 'CLUSTER') return 0
  return -1
}

function buildResourceNode(resource: ObservedResource): SceneModelResource {
  const position = resourcePosition(resource.resourceType, 0, 1)
  const level = resourceLevel(resource.resourceType)
  return {
    id: resource.id,
    name: resource.name,
    type: resource.resourceType,
    state: resource.lifecycleState,
    position: [position[0], level * LEVEL_HEIGHT, position[2]],
    dimensions: {
      CPU: dimensionState(resource, 'CPU'),
      GPU: dimensionState(resource, 'GPU'),
      MEMORY: dimensionState(resource, 'MEMORY'),
      STORAGE: dimensionState(resource, 'STORAGE'),
      IOPS: dimensionState(resource, 'IOPS'),
      THROUGHPUT: dimensionState(resource, 'THROUGHPUT'),
      LATENCY: dimensionState(resource, 'LATENCY'),
      QUEUE: dimensionState(resource, 'QUEUE'),
      ERROR_RATE: dimensionState(resource, 'ERROR_RATE'),
      AVAILABILITY: dimensionState(resource, 'AVAILABILITY'),
      REQUEST_RATE: dimensionState(resource, 'REQUEST_RATE'),
      SATURATION: dimensionState(resource, 'SATURATION'),
    },
    capacity: resource.capacityMetadata,
    children: [],
    labels: Object.entries(resource.labels).map(([k, v]) => `${k}=${v}`),
  }
}

function attachChildren(
  nodes: Map<string, SceneModelResource>,
  relations: ResourceRelation[],
): SceneModelResource[] {
  const parentMap = new Map<string, SceneModelResource[]>()
  for (const rel of relations) {
    if (rel.relationType === 'RUNS_ON' || rel.relationType === 'MEMBER_OF' || rel.relationType === 'CONTAINS') {
      const children = parentMap.get(rel.parentId) || []
      children.push(nodes.get(rel.childId) || { id: rel.childId, name: rel.childId, type: 'SERVICE', state: 'UNKNOWN', position: [0, 0, 0], dimensions: {} as Record<DimensionKey, DimensionSnapshot>, capacity: {} as ResourceCapacity, children: [], labels: [] })
      parentMap.set(rel.parentId, children)
    }
  }
  const roots: SceneModelResource[] = []
  for (const [id, node] of nodes) {
    const children = parentMap.get(id) || []
    node.children = children
    if (children.length > 0) {
      roots.push(node)
    }
  }
  return roots
}

export function transformSnapshotToSceneModel(snapshot: ObservabilitySnapshot): SceneModel {
  const nodes = new Map<string, SceneModelResource>()
  for (const resource of snapshot.resources) {
    nodes.set(resource.id, buildResourceNode(resource))
  }
  const roots = attachChildren(nodes, snapshot.relations)
  if (roots.length === 0 && nodes.size > 0) {
    return {
      snapshotId: snapshot.snapshotId,
      generatedAt: snapshot.generatedAt,
      resources: Array.from(nodes.values()),
      relations: snapshot.relations,
      alerts: snapshot.alerts,
      camera: { position: [0, 10, 20], target: [0, 0, 0] },
    }
  }
  return {
    snapshotId: snapshot.snapshotId,
    generatedAt: snapshot.generatedAt,
    resources: roots,
    relations: snapshot.relations,
    alerts: snapshot.alerts,
    camera: { position: [0, 10, 20], target: [0, 0, 0] },
  }
}
