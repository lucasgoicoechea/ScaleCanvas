interface ComparisonRow {
  label: string
  baseline: string
  growthX2: string
  growthX10: string
}

interface VariantComparatorProps {
  results: Array<{
    variant: 'BASELINE' | 'GROWTH_X2' | 'GROWTH_X10'
    derivedMetrics: {
      peakHourRequests: number
      storageAfter12MonthsGb: number
      allowedUnavailabilityMinutesPerMonth: number
      dailyRequests: number
      dailyTransferGb: number
      readRps: number
      writeRps: number
    }
    recommendations: Array<{ urgency: string }>
    risks: Array<{ level: string }>
  }>
}

export function VariantComparator({ results }: VariantComparatorProps) {
  const baseline = results.find((r) => r.variant === 'BASELINE')
  const x2 = results.find((r) => r.variant === 'GROWTH_X2')
  const x10 = results.find((r) => r.variant === 'GROWTH_X10')

  if (!baseline || !x2 || !x10) {
    return <p className="column-note">Run the evaluation to compare baseline, x2 and x10 variants.</p>
  }

  const fmt = (value: number) => {
    if (Number.isInteger(value)) return value.toLocaleString()
    return value.toLocaleString(undefined, { maximumFractionDigits: 2 })
  }

  const recCount = (result: typeof baseline) => ({
    immediate: result.recommendations.filter((r) => r.urgency === 'IMMEDIATE').length,
    soon: result.recommendations.filter((r) => r.urgency === 'SOON').length,
    notYet: result.recommendations.filter((r) => r.urgency === 'NOT_YET').length,
  })

  const riskCount = (result: typeof baseline) => ({
    high: result.risks.filter((r) => r.level === 'HIGH').length,
    medium: result.risks.filter((r) => r.level === 'MEDIUM').length,
    low: result.risks.filter((r) => r.level === 'LOW').length,
  })

  const rows: ComparisonRow[] = [
    { label: 'Peak hour requests', baseline: fmt(baseline.derivedMetrics.peakHourRequests), growthX2: fmt(x2.derivedMetrics.peakHourRequests), growthX10: fmt(x10.derivedMetrics.peakHourRequests) },
    { label: 'Daily requests', baseline: fmt(baseline.derivedMetrics.dailyRequests), growthX2: fmt(x2.derivedMetrics.dailyRequests), growthX10: fmt(x10.derivedMetrics.dailyRequests) },
    { label: 'Daily transfer', baseline: fmt(baseline.derivedMetrics.dailyTransferGb) + ' GB', growthX2: fmt(x2.derivedMetrics.dailyTransferGb) + ' GB', growthX10: fmt(x10.derivedMetrics.dailyTransferGb) + ' GB' },
    { label: 'Read RPS', baseline: fmt(baseline.derivedMetrics.readRps), growthX2: fmt(x2.derivedMetrics.readRps), growthX10: fmt(x10.derivedMetrics.readRps) },
    { label: 'Write RPS', baseline: fmt(baseline.derivedMetrics.writeRps), growthX2: fmt(x2.derivedMetrics.writeRps), growthX10: fmt(x10.derivedMetrics.writeRps) },
    { label: 'Storage after 12 months', baseline: fmt(baseline.derivedMetrics.storageAfter12MonthsGb) + ' GB', growthX2: fmt(x2.derivedMetrics.storageAfter12MonthsGb) + ' GB', growthX10: fmt(x10.derivedMetrics.storageAfter12MonthsGb) + ' GB' },
    { label: 'Allowed downtime/month', baseline: fmt(baseline.derivedMetrics.allowedUnavailabilityMinutesPerMonth) + ' min', growthX2: fmt(x2.derivedMetrics.allowedUnavailabilityMinutesPerMonth) + ' min', growthX10: fmt(x10.derivedMetrics.allowedUnavailabilityMinutesPerMonth) + ' min' },
    { label: 'Recommendations (immediate)', baseline: String(recCount(baseline).immediate), growthX2: String(recCount(x2).immediate), growthX10: String(recCount(x10).immediate) },
    { label: 'Recommendations (soon)', baseline: String(recCount(baseline).soon), growthX2: String(recCount(x2).soon), growthX10: String(recCount(x10).soon) },
    { label: 'Recommendations (not yet)', baseline: String(recCount(baseline).notYet), growthX2: String(recCount(x2).notYet), growthX10: String(recCount(x10).notYet) },
    { label: 'Risks (high)', baseline: String(riskCount(baseline).high), growthX2: String(riskCount(x2).high), growthX10: String(riskCount(x10).high) },
    { label: 'Risks (medium)', baseline: String(riskCount(baseline).medium), growthX2: String(riskCount(x2).medium), growthX10: String(riskCount(x10).medium) },
    { label: 'Risks (low)', baseline: String(riskCount(baseline).low), growthX2: String(riskCount(x2).low), growthX10: String(riskCount(x10).low) },
  ]

  return (
    <div className="visual-card">
      <div className="section-title">
        <span>Formal variant comparator</span>
      </div>
      <div className="comparator-table-wrap">
        <table className="comparator-table">
          <thead>
            <tr>
              <th>Metric</th>
              <th>Baseline</th>
              <th>Growth x2</th>
              <th>Growth x10</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((row) => (
              <tr key={row.label}>
                <td>{row.label}</td>
                <td>{row.baseline}</td>
                <td>{row.growthX2}</td>
                <td>{row.growthX10}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="equalizer-insight">
        <strong>Interpretation:</strong> compare baseline against x2/x10 to see how demand, storage and operational pressure scale. Use recommendation and risk counts to judge readiness.
      </div>
    </div>
  )
}
