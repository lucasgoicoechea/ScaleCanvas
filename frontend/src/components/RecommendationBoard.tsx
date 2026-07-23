import { titleize } from '../formatters'
import type { RuleOutcome, Urgency } from '../types'

const columns: Array<{ urgency: Urgency; title: string; note: string }> = [
  { urgency: 'DO_NOW', title: 'Do now', note: 'Evidence supports implementation.' },
  { urgency: 'WATCH', title: 'Watch', note: 'Measure and prepare a reversible option.' },
  { urgency: 'NOT_YET', title: 'Not yet', note: 'Complexity is not justified today.' },
]

function RecommendationCard({ item }: { item: RuleOutcome }) {
  return (
    <article className={`recommendation-card ${item.urgency.toLowerCase()}`}>
      <div className="card-topline">
        <span>{titleize(item.component)}</span>
        <code title={`Rule version ${item.version}`}>{item.ruleId}@{item.version}</code>
      </div>
      <h4>{item.title}</h4>
      <p>{item.rationale}</p>
      <details>
        <summary>Why this recommendation?</summary>
        <p><b>Decision:</b> {titleize(item.status)}</p>
        <p className="threshold">Threshold: {item.threshold}</p>
        <p><b>Evidence</b></p>
        <ul>{Object.entries(item.evidence).map(([key, value]) => <li key={key}><b>{titleize(key)}:</b> {value}</li>)}</ul>
        {item.benefits.length > 0 && (
          <>
            <p><b>Benefits</b></p>
            <ul>{item.benefits.map((benefit) => <li key={benefit}>{benefit}</li>)}</ul>
          </>
        )}
        {item.tradeoffs.length > 0 && (
          <>
            <p><b>Trade-offs</b></p>
            <ul>{item.tradeoffs.map((tradeoff) => <li key={tradeoff}>{tradeoff}</li>)}</ul>
          </>
        )}
        <p><b>Simpler alternative:</b> {item.simplerAlternative}</p>
      </details>
    </article>
  )
}

export function RecommendationBoard({ recommendations }: { recommendations: RuleOutcome[] }) {
  return (
    <section className="recommendation-board">
      {columns.map((column) => {
        const items = recommendations.filter((item) => item.urgency === column.urgency)
        return (
          <div className="recommendation-column" key={column.urgency}>
            <div className="column-heading"><h3>{column.title}</h3><span>{items.length}</span></div>
            <p className="column-note">{column.note}</p>
            {items.length === 0 ? <p className="empty-state">No recommendations in this group.</p> : items.map((item) => <RecommendationCard key={item.ruleId} item={item} />)}
          </div>
        )
      })}
    </section>
  )
}
