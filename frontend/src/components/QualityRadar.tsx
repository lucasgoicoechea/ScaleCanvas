import ReactECharts from 'echarts-for-react'
import { useMemo } from 'react'
import type { ScenarioRequest } from '../types'

interface QualityRadarProps {
  scenario: ScenarioRequest
}

const CONSISTENCY_SCORE: Record<ScenarioRequest['quality']['consistencyLevel'], number> = {
  EVENTUAL: 33,
  READ_YOUR_WRITES: 66,
  STRONG: 100,
}

const GEO_SCORE: Record<ScenarioRequest['quality']['geographicScope'], number> = {
  LOCAL: 25,
  COUNTRY: 50,
  MULTI_COUNTRY: 75,
  GLOBAL: 100,
}

export function QualityRadar({ scenario }: QualityRadarProps) {
  const quality = scenario.quality
  const option = useMemo(() => {
    const maxRto = 120
    const maxRpo = 60
    const maxP50 = 200
    const maxP95 = 500
    const maxP99 = 1000

    const availability = Number(quality.availabilitySloPercent.toFixed(2))
    const rto = Number((Math.max(maxRto - quality.rtoMinutes, 0) / maxRto * 100).toFixed(2))
    const rpo = Number((Math.max(maxRpo - quality.rpoMinutes, 0) / maxRpo * 100).toFixed(2))
    const p50 = Number((Math.max(maxP50 - quality.targetP50Ms, 0) / maxP50 * 100).toFixed(2))
    const p95 = Number((Math.max(maxP95 - quality.targetP95Ms, 0) / maxP95 * 100).toFixed(2))
    const p99 = Number((Math.max(maxP99 - quality.targetP99Ms, 0) / maxP99 * 100).toFixed(2))
    const consistency = CONSISTENCY_SCORE[quality.consistencyLevel] ?? 0
    const geo = GEO_SCORE[quality.geographicScope] ?? 0

    return {
      tooltip: {
        formatter: (params: { data: { value: number; name: string }[] }) => {
          return params.data
            .map((item) => `<strong>${item.name}</strong>: ${item.value.toFixed(1)}/100`)
            .join('<br/>')
        },
      },
      radar: {
        indicator: [
          { name: 'Availability SLO', max: 100 },
          { name: 'RTO', max: 100 },
          { name: 'RPO', max: 100 },
          { name: 'p50 Latency', max: 100 },
          { name: 'p95 Latency', max: 100 },
          { name: 'p99 Latency', max: 100 },
          { name: 'Consistency', max: 100 },
          { name: 'Geo Scope', max: 100 },
        ],
        shape: 'circle',
        splitNumber: 4,
        axisName: { color: '#f87171' },
        splitLine: { lineStyle: { color: '#3b1525' } },
        splitArea: { areaStyle: { color: ['rgba(127,29,61,0.08)', 'rgba(127,29,61,0.15)'] } },
      },
      series: [
        {
          type: 'radar',
          data: [
            {
              value: [availability, rto, rpo, p50, p95, p99, consistency, geo],
              name: 'Quality profile',
              areaStyle: { color: 'rgba(220,38,38,0.25)' },
              lineStyle: { color: '#dc2626' },
              itemStyle: { color: '#f87171' },
            },
          ],
        },
      ],
    }
  }, [quality])

  return (
    <div className="visual-card">
      <div className="section-title">
        <span>Quality radar</span>
      </div>
      <ReactECharts option={option} style={{ height: 360 }} />
      <div className="equalizer-insight">
        <strong>Interpretation:</strong> closer to the outer ring means stronger quality guarantees. Latency, RTO and RPO are reward-bounded.
      </div>
    </div>
  )
}
