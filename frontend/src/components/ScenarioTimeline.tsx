import ReactECharts from 'echarts-for-react'
import { useMemo } from 'react'

interface ScenarioTimelineProps {
  versions: Array<{ id: string; scenarioId: string; versionLabel: string; createdAt: string }>
}

export function ScenarioTimeline({ versions }: ScenarioTimelineProps) {
  const chartData = useMemo(() => {
    return versions
      .sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime())
      .map((version, index) => ({
        name: version.versionLabel,
        value: [index, version.createdAt],
      }))
  }, [versions])

  const option = useMemo(() => {
    return {
      tooltip: {
        formatter: (params: { data: { name: string; value: number[] } }) => {
          const item = params.data
          return `<strong>${item.name}</strong><br/>Created: ${new Date(item.value[1]).toLocaleString()}`
        },
      },
      xAxis: {
        type: 'category',
        data: chartData.map((item) => item.name),
        name: 'Version',
        axisLine: { lineStyle: { color: '#7f1d3d' } },
        axisLabel: { color: '#f87171' },
        splitLine: { lineStyle: { color: '#3b1525' } },
      },
      yAxis: {
        type: 'value',
        show: false,
        max: 1,
      },
      series: [
        {
          type: 'line',
          data: chartData.map((item) => [item.name, 1]),
          step: 'start',
          lineStyle: { color: '#dc2626', width: 2 },
          itemStyle: { color: '#f87171' },
          symbol: 'circle',
          symbolSize: 10,
        },
      ],
    }
  }, [chartData])

  return (
    <div className="visual-card">
      <div className="section-title">
        <span>Evolution timeline</span>
      </div>
      {versions.length === 0 && <p className="column-note">Save the scenario to start tracking versions.</p>}
      {versions.length > 0 && <ReactECharts option={option} style={{ height: 240 }} />}
      <div className="equalizer-insight">
        <strong>Interpretation:</strong> each point represents a saved version. Use it to review how the architecture evolved.
      </div>
    </div>
  )
}
