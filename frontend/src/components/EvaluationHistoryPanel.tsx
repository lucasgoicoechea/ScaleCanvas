import '../styles.css'

interface EvaluationHistoryPanelProps {
  onClose: () => void
  onSelectEvaluation: (evaluationId: string) => void
  items: Array<{ evaluationId: string; scenarioName: string; generatedAt: string; catalogVersion: string }>
  loading: boolean
}

export function EvaluationHistoryPanel({ onClose, onSelectEvaluation, items, loading }: EvaluationHistoryPanelProps) {
  return (
    <div className="history-panel">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">History</p>
          <h2>Evaluations</h2>
        </div>
        <button className="secondary-button compact" onClick={onClose}>Close</button>
      </div>
      {loading && <p className="column-note">Loading history…</p>}
      {!loading && items.length === 0 && <p className="column-note">No evaluations yet.</p>}
      <div className="history-list">
        {items.map((item) => (
          <div className="history-item" key={item.evaluationId} onClick={() => onSelectEvaluation(item.evaluationId)}>
            <div>
              <strong>{item.scenarioName || 'Unnamed scenario'}</strong>
              <div className="column-note">
                {new Date(item.generatedAt).toLocaleString()} · rules {item.catalogVersion}
              </div>
            </div>
            <button className="secondary-button compact">Open</button>
          </div>
        ))}
      </div>
    </div>
  )
}
