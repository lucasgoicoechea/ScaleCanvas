import type { ScenarioRequest } from '../types'
import ReactECharts from 'echarts-for-react'
import { useMemo } from 'react'

interface ScalingMatrix3DProps {
  scenario: ScenarioRequest
}

export function ScalingMatrix3D({ scenario }: ScalingMatrix3DProps) {
  const services = scenario.deployment.serviceTopology.services
  const unitMemory = scenario.deployment.minimumUnitMemoryMb
  const unitCpu = scenario.deployment.minimumUnitCpuCount
  const maxReplicas = useMemo(
    () => services.reduce((max, s) => Math.max(max, s.replicas), 0),
    [services],
  )

  const bar3DData = useMemo(() => {
    const data: Array<{ serviceName: string; serverIndex: number; util: number }> = []
    services.forEach((service) => {
      for (let serverIndex = 0; serverIndex < service.replicas; serverIndex++) {
        const memoryUtil = unitMemory > 0 ? (service.memoryMb / unitMemory) * 100 : 0
        const cpuUtil = unitCpu > 0 ? (service.cpuCount / unitCpu) * 100 : 0
        const util = Math.min(Math.max(memoryUtil, cpuUtil), 100)
        data.push({ serviceName: service.serviceName, serverIndex, util })
      }
    })
    return data
  }, [services, unitMemory, unitCpu])

  const serviceNames = useMemo(() => Array.from(new Set(services.map((s) => s.serviceName))), [services])
  const serverIndices = useMemo(() => Array.from({ length: maxReplicas }, (_, i) => i), [maxReplicas])

  const option = useMemo(() => ({
    tooltip: {
      formatter: (params: { data: { serviceName: string; serverIndex: number; util: number } }) => {
        const { serviceName, serverIndex, util } = params.data
        return `<strong>${serviceName}</strong><br/>Server #${serverIndex + 1}<br/>Utilization: ${util.toFixed(1)}%`
      },
    },
    grid3D: {
      boxWidth: 120,
      boxDepth: 60,
      viewControl: {
        autoRotate: true,
        autoRotateSpeed: 8,
        distance: 220,
      },
      light: {
        main: { intensity: 1.2, shadow: true },
        ambient: { intensity: 0.3 },
      },
    },
    xAxis3D: { type: 'category', data: serviceNames, name: 'Service' },
    yAxis3D: { type: 'category', data: serverIndices.map((i) => `Server ${i + 1}`), name: 'Server' },
    zAxis3D: { type: 'value', name: 'Utilization %', min: 0, max: 100 },
    visualMap: {
      min: 0,
      max: 100,
      calculable: true,
      orient: 'horizontal',
      left: 'center',
      top: 'top',
      inRange: {
        color: ['#1e3a5f', '#1e5f3a', '#3a8a5c', '#7cb342', '#fdd835', '#f57f17', '#d84315'],
      },
    },
    series: [
      {
        type: 'bar3D',
        data: bar3DData.map((item) => [item.serviceName, `Server ${item.serverIndex + 1}`, item.util]),
        shading: 'lambert',
        label: { show: true, fontSize: 10, color: '#fff' },
        itemStyle: { opacity: 0.85 },
        emphasis: {
          label: { fontSize: 12, color: '#fff' },
          itemStyle: { color: '#fff' },
        },
      },
    ],
  }), [bar3DData, serviceNames, serverIndices])

  return (
    <div className="visual-card">
      <div className="section-title">
        <span>Scaling matrix 3D</span>
      </div>
      {services.length === 0 && <p className="column-note">Add services in the Infrastructure section to see the matrix.</p>}
      {services.length > 0 && maxReplicas === 0 && <p className="column-note">Set replicas to at least 1 to visualize capacity.</p>}
      {services.length > 0 && maxReplicas > 0 && (
        <ReactECharts option={option} style={{ height: 420 }} />
      )}
      <div className="equalizer-insight">
        <strong>Interpretation:</strong> rotate the 3D matrix to inspect per-service utilization. Add servers or split services when utilization is above 80%.
      </div>
    </div>
  )
}
