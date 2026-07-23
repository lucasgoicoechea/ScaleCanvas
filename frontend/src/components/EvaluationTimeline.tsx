import ReactECharts from 'echarts-for-react'
import { useMemo } from 'react'

interface EvaluationTimelineProps {
  items: Array<{
    evaluationId: string
    scenarioId: string
    scenarioName: string
    catalogVersion: string
    generatedAt: string
    variant: string
    peakRps: string
    storage12MonthsGb: string
    allowedDowntimeMinutesPerMonth: string
    recommendationCount: number
  }>
}

export function EvaluationTimeline({ items }: EvaluationTimelineProps) {
  const chartData = useMemo(() => {
    return items
      .slice()
      .sort((a, b) => new Date(a.generatedAt).getTime() - new Date(b.generatedAt).getTime())
      .map((item, index) => ({
        label: `${item.variant}\n${new Date(item.generatedAt).toLocaleDateString()} ${new Date(item.generatedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`,
        peakRps: Number(item.peakRps),
        storage12MonthsGb: Number(item.storage12MonthsGb),
        allowedDowntimeMinutesPerMonth: Number(item.allowedDowntimeMinutesPerMonth),
        recommendationCount: item.recommendationCount,
        index,
      }))
  }, [items])

  const option = useMemo(() => {
    return {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'cross' },
        formatter: (params: any[]) => {
          const point = chartData[params[0].dataIndex as number]
          const lines = [point.label, '', ...params.map((p: { seriesName: string; marker: string; value: number }) => `${p.marker} ${p.seriesName}: ${p.value}`)]
          return lines.join('<br/>')
        },
      },
      legend: {
        data: ['Peak RPS', 'Storage 12mo (GB)', 'Downtime (min/month)', 'Recommendations'],
        textStyle: { color: '#f87171' },
      },
      grid: { left: 40, right: 20, top: 60, bottom: 40 },
      xAxis: {
        type: 'category',
        data: chartData.map((item) => item.label),
        axisLine: { lineStyle: { color: '#7f1d3d' } },
        axisLabel: { color: '#f87171', rotate: 30 },
      },
      yAxis: [
        { type: 'value', name: 'Requests', axisLine: { lineStyle: { color: '#7f1d3d' } }, axisLabel: { color: '#f87171' }, splitLine: { lineStyle: { color: '#3b1525' } } },
        { type: 'value', name: 'Storage', axisLine: { lineStyle: { color: '#7f1d3d' } }, axisLabel: { color: '#f87171' }, splitLine: { lineStyle: { color: '#3b1525' } } },
        { type: 'value', name: 'Downtime / Recommendations', axisLine: { lineStyle: { color: '#7f1d3d' } }, axisLabel: { color: '#f87171' }, splitLine: { lineStyle: { color: '#3b1525' } }, min: 0 },
      ],
      series: [
        { name: 'Peak RPS', type: 'bar', data: chartData.map((item) => item.peakRps), itemStyle: { color: '#dc2626' } },
        { name: 'Storage 12mo (GB)', type: 'bar', yAxisIndex: 1, data: chartData.map((item) => item.storage12MonthsGb), itemStyle: { color: '#991b1b' } },
        { name: 'Downtime (min/month)', type: 'line', yAxisIndex: 2, data: chartData.map((item) => item.allowedDowntimeMinutesPerMonth), lineStyle: { color: '#facc15', width: 2 }, itemStyle: { color: '#facc15' }, symbol: 'circle', symbolSize: 8 },
        { name: 'Recommendations', type: 'line', yAxisIndex: 2, data: chartData.map((item) => item.recommendationCount), lineStyle: { color: '#a855f7', width: 2 }, itemStyle: { color: '#a855f7' }, symbol: 'circle', symbolSize: 8 },
      ],
    }
  }, [chartData])

  return (
    <div className="visual-card">
      <div className="section-title">
        <span>Evaluation timeline</span>
      </div>
      {items.length === 0 && <p className="column-note">Evaluate the scenario to populate the timeline.</p>}
      {items.length > 0 && <ReactECharts option={option} style={{ height: 320 }} />}
      <div className="equalizer-insight">
        <strong>Interpretation:</strong> each column groups one evaluation variant, ordered by time. Use it to see how load, storage, downtime pressure and recommendations evolve together.
      </div>
    </div>
  )
}
