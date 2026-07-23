import type { RiskFinding } from '../types'

export function RiskPanel({ risks }: { risks: RiskFinding[] }) {
  return (
    <section className="visual-card risk-card">
      <div className="section-title"><div><p className="eyebrow">Failure modes</p><h3>Risk heatmap</h3></div></div>
      <div className="risk-list">
        {risks.length === 0 ? <p className="empty-state">No explicit risk was derived.</p> : risks.map((risk) => (
          <article className={`risk-item ${risk.level.toLowerCase()}`} key={`${risk.level}-${risk.title}`}>
            <span>{risk.level}</span><div><strong>{risk.title}</strong><p>{risk.detail}</p></div>
          </article>
        ))}
      </div>
    </section>
  )
}
