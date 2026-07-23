import type { ObservabilitySnapshot } from '../observabilityTypes'
import ReactECharts from 'echarts-for-react'

interface ObservabilityScene3DFallbackProps {
  snapshot: ObservabilitySnapshot
}

export function ObservabilityScene3DFallback({ snapshot }: ObservabilityScene3DFallbackProps) {
  const resources = snapshot.resources ?? []
  const rows = resources.map((resource) => {
    const cpu = resource.labels?.['__dimensions'] ? JSON.parse(resource.labels['__dimensions']).CPU : null
    const memory = resource.labels?.['__dimensions'] ? JSON.parse(resource.labels['__dimensions']).MEMORY : null
    const storage = resource.labels?.['__dimensions'] ? JSON.parse(resource.labels['__dimensions']).STORAGE : null
    const cpuRatio = cpu?.ratio ?? null
    const memoryRatio = memory?.ratio ?? null
    const storageRatio = storage?.ratio ?? null
    const pressure = Math.max(cpuRatio ?? 0, memoryRatio ?? 0, storageRatio ?? 0)
    return {
      resource: resource.name || resource.id,
      state: resource.lifecycleState,
      cpuRatio,
      memoryRatio,
      storageRatio,
      pressure,
    }
  })

  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
    },
    legend: {
      data: ['CPU', 'Memory', 'Storage', 'Pressure'],
      textStyle: { color: '#e5e7eb' },
      bottom: 0,
    },
    grid: { left: '3%', right: '4%', bottom: '10%', top: '12%', containLabel: true },
    xAxis: {
      type: 'category',
      data: rows.map((item) => item.resource),
      axisLabel: { color: '#e5e7eb' },
    },
    yAxis: {
      type: 'value',
      max: 1,
      axisLabel: { color: '#e5e7eb', formatter: '{value}' },
    },
    series: [
      {
        name: 'CPU',
        type: 'bar',
        stack: 'dimensions',
        data: rows.map((item) => item.cpuRatio ?? '-'),
        itemStyle: { color: '#38bdf8' },
      },
      {
        name: 'Memory',
        type: 'bar',
        stack: 'dimensions',
        data: rows.map((item) => item.memoryRatio ?? '-'),
        itemStyle: { color: '#22c55e' },
      },
      {
        name: 'Storage',
        type: 'bar',
        stack: 'dimensions',
        data: rows.map((item) => item.storageRatio ?? '-'),
        itemStyle: { color: '#f59e0b' },
      },
      {
        name: 'Pressure',
        type: 'line',
        data: rows.map((item) => item.pressure),
        itemStyle: { color: '#ef4444' },
        lineStyle: { width: 2 },
      },
    ],
  }

  return (
    <div className="visual-card">
      <div className="section-title"><span>Observability 2D fallback</span></div>
      <ReactECharts option={option} style={{ height: 360 }} />
    </div>
  )
}
