import ReactECharts from 'echarts-for-react'
import { useMemo } from 'react'

interface ScatterPoint {
  serviceName: string
  estimatedMonthlyCost: number
  complexityScore: number
  driver: string
}

interface CostComplexityMatrixProps {
  items: ScatterPoint[]
}

export function CostComplexityMatrix({ items }: CostComplexityMatrixProps) {
  const chartData = useMemo(() => {
    return items.map((item) => [item.estimatedMonthlyCost, item.complexityScore, item.serviceName, item.driver])
  }, [items])

  const option = useMemo(() => {
    return {
      tooltip: {
        trigger: 'item',
        formatter: (params: { data: [number, number, string, string]; marker: string }) => {
          const point = params.data
          return `${params.marker} <strong>${point[2]}</strong><br/>Cost: $${point[0]}<br/>Complexity: ${point[1]}<br/><em>${point[3]}</em>`
        },
      },
      xAxis: {
        type: 'value',
        name: 'Estimated Monthly Cost ($)',
        axisLine: { lineStyle: { color: '#7f1d3d' } },
        axisLabel: { color: '#f87171' },
        splitLine: { lineStyle: { color: '#3b1525' } },
      },
      yAxis: {
        type: 'value',
        name: 'Complexity Score',
        min: 0,
        max: 100,
        axisLine: { lineStyle: { color: '#7f1d3d' } },
        axisLabel: { color: '#f87171' },
        splitLine: { lineStyle: { color: '#3b1525' } },
      },
      series: [
        {
          type: 'scatter',
          symbolSize: (data: [number, number, string, string]) => Math.max(12, Math.min(40, data[1] * 0.5)),
          data: chartData,
          itemStyle: { color: '#dc2626' },
          label: {
            show: true,
            formatter: (params: { data: [number, number, string, string] }) => params.data[2],
            color: '#f87171',
          },
        },
      ],
    }
  }, [chartData])

  return (
    <div className="visual-card">
      <div className="section-title">
        <span>Cost/complexity matrix</span>
      </div>
      {items.length === 0 && <p className="column-note">Load or create a scenario to visualize cost versus complexity.</p>}
      {items.length > 0 && <ReactECharts option={option} style={{ height: 320 }} />}
      <div className="equalizer-insight">
        <strong>Interpretation:</strong> each point represents one service. Top-right means high cost and high complexity; prefer services toward the bottom-left when trade-offs allow.
      </div>
    </div>
  )
}
