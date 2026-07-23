import { useEffect, useMemo, useState } from 'react'
import { Canvas } from '@react-three/fiber'
import { OrbitControls, PerspectiveCamera, Grid, Text, Line } from '@react-three/drei'
import * as THREE from 'three'
import { getSimulatedSnapshot } from '../api'
import type { ObservabilitySnapshot, ResourceCapacity, SceneModel, SceneModelResource } from '../observabilityTypes'
import { transformSnapshotToSceneModel } from '../sceneModelTransformer'

function getResourceColor(state: string) {
  switch (state) {
    case 'HEALTHY':
      return '#22c55e'
    case 'WARNING':
      return '#eab308'
    case 'CRITICAL':
      return '#ef4444'
    case 'STALE':
      return '#94a3b8'
    case 'UNKNOWN':
      return '#7c3aed'
    default:
      return '#475569'
  }
}

function getRatioColor(ratio: number | null) {
  if (ratio === null || Number.isNaN(ratio)) return '#7c3aed'
  if (ratio > 1) return '#ef4444'
  if (ratio > 0.85) return '#ef4444'
  if (ratio > 0.7) return '#f59e0b'
  if (ratio > 0) return '#22c55e'
  return '#475569'
}

const ROOT_OFFSET_X = -4.5
const CHILD_OFFSET_X = 4.5
const LEVEL_HEIGHT = 2.8
const MAX_VISIBLE_LABELS = 20

function BoundingBox({ resource, capacity, onSelect, isSelected }: { resource: SceneModelResource; capacity: ResourceCapacity; onSelect?: (id: string) => void; isSelected?: boolean }) {
  const width = 1.2
  const depth = 1.2
  const height = Math.max(0.4, Math.min(7, (capacity.memoryCapacityBytes / (1024 * 1024 * 1024)) / 14 + 0.6))

  const boxGeometry = useMemo(() => new THREE.BoxGeometry(width, height, depth), [width, height, depth])
  const edgesGeometry = useMemo(() => new THREE.EdgesGeometry(boxGeometry), [boxGeometry])

  const cpuUsed = Math.min(Math.max((resource.dimensions?.CPU?.current ?? 0) / Math.max(1, capacity.cpuCapacityCores), 0), 1)
  const gpuUsed = Math.min(Math.max((resource.dimensions?.GPU?.current ?? 0) / Math.max(1, capacity.gpuCapacityCount || 1), 0), 1)
  const memoryUsed = Math.min(Math.max((resource.dimensions?.MEMORY?.current ?? 0) / Math.max(1, capacity.memoryCapacityBytes), 0), 1)
  const storageUsed = Math.min(Math.max((resource.dimensions?.STORAGE?.current ?? 0) / Math.max(1, capacity.storageCapacityBytes), 0), 1)

  const baseX = isSelected ? 0 : (resource.children.length > 0 ? ROOT_OFFSET_X : CHILD_OFFSET_X)
  const baseY = resource.children.length > 0 ? 0 : -LEVEL_HEIGHT

  return (
    <group position={[baseX, baseY, 0]}>
      <mesh geometry={boxGeometry} castShadow receiveShadow onClick={() => onSelect?.(resource.id)}>
        <meshPhysicalMaterial
          color={getResourceColor(resource.state)}
          roughness={0.18}
          metalness={0.05}
          transparent
          opacity={isSelected ? 0.8 : 0.35}
          depthWrite={false}
          transmission={0.35}
          thickness={0.15}
        />
      </mesh>
      <lineSegments geometry={edgesGeometry}>
        <lineBasicMaterial color={getRatioColor(1)} linewidth={1} transparent opacity={0.9} />
      </lineSegments>

      <CpuColumn cpuUsed={cpuUsed} x={-width / 2 - 0.35} />
      {capacity.gpuCapacityCount > 0 && (
        <GpuColumn gpuUsed={gpuUsed} x={width / 2 + 0.35} />
      )}
      <MemoryBar usedRatio={memoryUsed} z={depth / 2 + 0.18} />
      <StorageBar usedRatio={storageUsed} z={-depth / 2 - 0.18} />

      <Text position={[0, height / 2, 0]} fontSize={0.22} color="#e5e7eb" anchorX="center" anchorY="middle">
        {resource.name}
      </Text>
    </group>
  )
}

function CpuColumn({ cpuUsed, x }: { cpuUsed: number; x: number }) {
  const height = 0.4 + Math.max(0, Math.min(2.2, cpuUsed * 2.2))
  const geometry = useMemo(() => new THREE.BoxGeometry(0.16, height, 0.16), [height])
  const ratio = Math.min(Math.max(cpuUsed, 0), 1)
  return (
    <group position={[x, 0, 0]}>
      <mesh geometry={geometry} position={[0, height / 2, 0]}>
        <meshPhysicalMaterial color={getRatioColor(ratio)} transparent opacity={0.8} roughness={0.25} metalness={0.05} />
      </mesh>
      <Text position={[0, height + 0.22, 0]} fontSize={0.2} color="#e5e7eb" anchorX="center" anchorY="middle">
        CPU {(ratio * 100).toFixed(0)}%
      </Text>
    </group>
  )
}

function GpuColumn({ gpuUsed, x }: { gpuUsed: number; x: number }) {
  const height = 0.4 + Math.max(0, Math.min(2.2, gpuUsed * 2.2))
  const geometry = useMemo(() => new THREE.BoxGeometry(0.16, height, 0.16), [height])
  const ratio = Math.min(Math.max(gpuUsed, 0), 1)
  return (
    <group position={[x, 0, 0]}>
      <mesh geometry={geometry} position={[0, height / 2, 0]}>
        <meshPhysicalMaterial color={getRatioColor(ratio)} transparent opacity={0.8} roughness={0.25} metalness={0.05} />
      </mesh>
      <Text position={[0, height + 0.22, 0]} fontSize={0.2} color="#e5e7eb" anchorX="center" anchorY="middle">
        GPU {(ratio * 100).toFixed(0)}%
      </Text>
    </group>
  )
}

function MemoryBar({ usedRatio, z }: { usedRatio: number; z: number }) {
  const width = 1.2
  const height = 0.14
  const length = Math.max(0.2, Math.min(3, usedRatio * 3))
  const ratio = Math.min(Math.max(usedRatio, 0), 1)
  return (
    <group position={[0, 0.35 + height / 2, z]}>
      <mesh rotation={[Math.PI / 2, 0, 0]}>
        <boxGeometry args={[width, length, height]} />
        <meshPhysicalMaterial color={getRatioColor(ratio)} transparent opacity={0.8} roughness={0.25} metalness={0.05} />
      </mesh>
      <Text position={[0, length + 0.26, 0]} fontSize={0.2} color="#e5e7eb" anchorX="center" anchorY="middle" rotation={[Math.PI / 2, 0, 0]}>
        MEM {(ratio * 100).toFixed(0)}%
      </Text>
    </group>
  )
}

function StorageBar({ usedRatio, z }: { usedRatio: number; z: number }) {
  const width = 1.2
  const height = 0.14
  const length = Math.max(0.2, Math.min(3, usedRatio * 3))
  const ratio = Math.min(Math.max(usedRatio, 0), 1)
  return (
    <group position={[0, 0.35 + height / 2, z]}>
      <mesh rotation={[Math.PI / 2, 0, 0]}>
        <boxGeometry args={[width, length, height]} />
        <meshPhysicalMaterial color={getRatioColor(ratio)} transparent opacity={0.8} roughness={0.25} metalness={0.05} />
      </mesh>
      <Text position={[0, length + 0.26, 0]} fontSize={0.2} color="#e5e7eb" anchorX="center" anchorY="middle" rotation={[Math.PI / 2, 0, 0]}>
        STORAGE {(ratio * 100).toFixed(0)}%
      </Text>
    </group>
  )
}

function HierarchyLine({ from, to }: { from: [number, number, number]; to: [number, number, number] }) {
  const points = useMemo(() => [new THREE.Vector3(...from), new THREE.Vector3(...to)], [from, to])
  return (
    <Line points={points} color="#f87171" linewidth={1} transparent opacity={0.6} dashed dashScale={1} gapSize={0.2} />
  )
}

export function ObservabilityScene3D() {
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [snapshot, setSnapshot] = useState<ObservabilitySnapshot | null>(null)
  const [selectedId, setSelectedId] = useState<string | null>(null)

  useEffect(() => {
    let cancelled = false
    setLoading(true)
    setError(null)
    getSimulatedSnapshot('NORMAL', 1)
      .then((data) => {
        if (!cancelled) setSnapshot(data)
      })
      .catch((exception) => {
        if (!cancelled) setError(exception instanceof Error ? exception.message : 'Failed to load observability snapshot')
      })
      .finally(() => {
        if (!cancelled) setLoading(false)
      })

    return () => {
      cancelled = true
    }
  }, [])

  const sceneModel: SceneModel | null = useMemo(() => {
    if (!snapshot) return null
    return transformSnapshotToSceneModel(snapshot)
  }, [snapshot])

  if (loading) {
    return (
      <div className="visual-card">
        <div className="section-title"><span>Observability 3D</span></div>
        <p className="column-note">Loading simulated snapshot...</p>
      </div>
    )
  }

  if (error || !sceneModel) {
    return (
      <div className="visual-card">
        <div className="section-title"><span>Observability 3D</span></div>
        <p className="column-note">WebGL not available or no snapshot. Using 2D fallback is recommended.</p>
      </div>
    )
  }

  const selectedResource = useMemo(() => sceneModel.resources.find((item) => item.id === selectedId) ?? null, [sceneModel.resources, selectedId])
  const visibleLabels = useMemo(() => {
    if (!selectedResource) return []
    const base = `${selectedResource.name} | ${selectedResource.type} | ${selectedResource.state}`
    const cpu = selectedResource.dimensions?.CPU
    const memory = selectedResource.dimensions?.MEMORY
    const extras = [cpu, memory]
      .filter((dimension): dimension is NonNullable<typeof dimension> => dimension != null)
      .map((dimension) => `${dimension.key}: ${dimension.ratio == null ? 'unknown' : `${(dimension.ratio * 100).toFixed(0)}%`}`)
    return [base, ...extras].slice(0, MAX_VISIBLE_LABELS)
  }, [selectedResource])

  return (
    <div className="visual-card" style={{ height: '520px' }}>
      <div className="section-title"><span>Observability 3D</span></div>
      <Canvas dpr={[1, 1.5]} gl={{ preserveDrawingBuffer: true, antialias: true }}>
        <PerspectiveCamera makeDefault position={[14, 10, 14]} />
        <OrbitControls enableDamping dampingFactor={0.08} />
        <Grid infiniteGrid sectionColor="#334155" cellColor="#1e293b" />
        <ambientLight intensity={0.6} />
        <directionalLight position={[8, 12, 10]} intensity={1.2} />
        <pointLight position={[-6, 6, -6]} intensity={0.7} />

        <group rotation={[0, Math.PI / 6, 0]}>
          {sceneModel.resources.map((resource) => (
            <group key={resource.id}>
              <BoundingBox resource={resource} capacity={resource.capacity} onSelect={setSelectedId} isSelected={resource.id === selectedId} />
              {resource.children.map((child) => {
                const parentPos = resource.position
                const childPos = [CHILD_OFFSET_X, -LEVEL_HEIGHT, 0] as [number, number, number]
                return (
                  <group key={child.id}>
                    <BoundingBox resource={child} capacity={child.capacity} onSelect={setSelectedId} isSelected={child.id === selectedId} />
                    <HierarchyLine from={[parentPos[0] + 0.6, parentPos[1], parentPos[2]]} to={childPos} />
                  </group>
                )
              })}
            </group>
          ))}
        </group>
      </Canvas>

      {visibleLabels.length > 0 && (
        <div style={{ marginTop: 10, color: '#e5e7eb', fontSize: 12 }}>
          {visibleLabels.map((label) => (
            <div key={label}>{label}</div>
          ))}
        </div>
      )}
    </div>
  )
}
