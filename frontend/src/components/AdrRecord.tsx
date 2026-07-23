interface AdrRecord {
  id: string
  title: string
  context: string
  options: string
  decision: string
  consequences: string
  generatedFromEvaluationId: string
  generatedAt: string
}

interface AdrRecordProps {
  adr: AdrRecord | null
  loading: boolean
  onGenerate: () => void
}

export function AdrRecord({ adr, loading, onGenerate }: AdrRecordProps) {
  return (
    <div className="visual-card">
      <div className="section-title">
        <span>Architecture Decision Record</span>
        <button className="secondary-button compact" onClick={onGenerate} disabled={loading || !adr}>
          {adr ? 'Regenerate ADR' : 'Generate ADR'}
        </button>
      </div>
      {loading && <p className="column-note">Generating ADR…</p>}
      {!loading && !adr && <p className="column-note">Run an evaluation to generate an ADR.</p>}
      {adr && (
        <div className="adr">
          <h3>{adr.title}</h3>
          <p><strong>Context</strong></p>
          <pre className="adr-block">{adr.context}</pre>
          <p><strong>Options considered</strong></p>
          <pre className="adr-block">{adr.options}</pre>
          <p><strong>Decision</strong></p>
          <pre className="adr-block">{adr.decision}</pre>
          <p><strong>Consequences</strong></p>
          <pre className="adr-block">{adr.consequences}</pre>
          <p className="column-note">Generated from evaluation {adr.generatedFromEvaluationId} at {new Date(adr.generatedAt).toLocaleString()}</p>
        </div>
      )}
    </div>
  )
}
