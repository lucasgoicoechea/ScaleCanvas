import '../styles.css'

interface CatalogVersion {
  id: string
  version: string
  name: string
  source: string
  createdAt: string
  active: boolean
}

interface RuleCatalogPanelProps {
  onClose: () => void
  items: Array<{ id: string; title: string; category: string; status: string }>
  loading: boolean
  versions: CatalogVersion[]
  onActivateVersion: (id: string) => void
  activatingVersion: boolean
}

export function RuleCatalogPanel({ onClose, items, loading, versions, onActivateVersion, activatingVersion }: RuleCatalogPanelProps) {
  return (
    <div className="history-panel">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">Rule catalog</p>
          <h2>Active rules</h2>
        </div>
        <button className="secondary-button compact" onClick={onClose}>Close</button>
      </div>
      <div className="history-section">
        <p className="eyebrow">Catalog versions</p>
        {versions.length === 0 && <p className="column-note">No catalog versions registered.</p>}
        <div className="history-list">
          {versions.map((item) => (
            <div className="history-item" key={item.id}>
              <div>
                <strong>{item.name}</strong>
                <div className="column-note">
                  {item.version} · {item.source} · {item.active ? 'Active' : 'Inactive'} · {new Date(item.createdAt).toLocaleString()}
                </div>
              </div>
              {!item.active && (
                <button className="secondary-button compact" disabled={activatingVersion} onClick={() => onActivateVersion(item.id)}>Activate</button>
              )}
            </div>
          ))}
        </div>
      </div>
      <div className="history-section">
        <p className="eyebrow">Rules</p>
        {loading && <p className="column-note">Loading catalog…</p>}
        {!loading && items.length === 0 && <p className="column-note">No rules available.</p>}
        <div className="history-list">
          {items.map((item) => (
            <div className="history-item" key={item.id}>
              <div>
                <strong>{item.title}</strong>
                <div className="column-note">
                  {item.id} · {item.category} · {item.status}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
