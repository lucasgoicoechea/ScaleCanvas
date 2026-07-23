import ReactECharts from 'echarts-for-react'
import type { VariantResult } from '../types'

export function CapacityChart({ results }: { results: VariantResult[] }) {
  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['Peak RPS', 'Storage 12m GB'], textStyle: { color: '#a8b4c7' } },
    grid: { left: 48, right: 32, top: 50, bottom: 38 },
    xAxis: { type: 'category', data: results.map((item) => item.variant.replace('GROWTH_', '')), axisLabel: { color: '#8b98ad' } },
    yAxis: [
      { type: 'value', name: 'RPS', axisLabel: { color: '#8b98ad' }, splitLine: { lineStyle: { color: '#243044' } } },
      { type: 'value', name: 'GB', axisLabel: { color: '#8b98ad' }, splitLine: { show: false } },
    ],
    series: [
      { name: 'Peak RPS', type: 'bar', data: results.map((item) => item.derivedMetrics.readRps + item.derivedMetrics.writeRps), itemStyle: { color: '#6ee7b7' } },
      { name: 'Storage 12m GB', type: 'line', yAxisIndex: 1, smooth: true, data: results.map((item) => item.derivedMetrics.storageAfter12MonthsGb), lineStyle: { color: '#8b5cf6' }, itemStyle: { color: '#8b5cf6' } },
    ],
  }
  return (
    <section className="visual-card chart-card">
      <div className="section-title"><div><p className="eyebrow">Scenario comparison</p><h3>Capacity projection</h3></div></div>
      <ReactECharts option={option} style={{ height: 310 }} />
    </section>
  )
}
