import { formatNumber } from '../formatters'
import type { DerivedMetrics } from '../types'

export function MetricCards({ metrics }: { metrics: DerivedMetrics }) {
  const cards = [
    ['Daily requests', formatNumber(metrics.dailyRequests, 0)],
    ['Peak-hour requests', formatNumber(metrics.peakHourRequests, 0)],
    ['Daily transfer', `${formatNumber(metrics.dailyTransferGb)} GB`],
    ['Read / Write RPS', `${formatNumber(metrics.readRps)} / ${formatNumber(metrics.writeRps)}`],
    ['Storage in 12 months', `${formatNumber(metrics.storageAfter12MonthsGb)} GB`],
    ['Downtime budget', `${formatNumber(metrics.allowedUnavailabilityMinutesPerMonth)} min/mo`],
  ]
  return <div className="metric-grid">{cards.map(([label, value]) => (
    <article className="metric-card" key={label}><span>{label}</span><strong>{value}</strong></article>
  ))}</div>
}
