import { useEffect, useMemo, useState } from 'react'
import { createScenario, deleteScenario, duplicateScenario, downloadMarkdown, evaluateScenario, exportScenario, estimateCloudCost, generateAdr, getCostComplexity, getEvaluation, getEvaluationTimeline, getScenario, getSimulatedSnapshot, importScenario, listEvaluations, listRuleCatalog, listRuleCatalogVersions, listScenarios, listScenarioVersions, requestExplanation, activateRuleCatalogVersion, updateScenario } from './api'
import { defaultScenario } from './defaultScenario'
import { ArchitectureCanvas } from './components/ArchitectureCanvas'
import { CapacityChart } from './components/CapacityChart'
import { CloudCostBreakdown } from './components/CloudCostBreakdown'
import { CostComplexityMatrix } from './components/CostComplexityMatrix'
import { EvaluationHistoryPanel } from './components/EvaluationHistoryPanel'
import { EvaluationTimeline } from './components/EvaluationTimeline'
import { MetricCards } from './components/MetricCards'
import { ObservabilityScene3D } from './components/ObservabilityScene3D'
import { ObservabilityScene3DFallback } from './components/ObservabilityScene3DFallback'
import { QualityRadar } from './components/QualityRadar'
import { RecommendationBoard } from './components/RecommendationBoard'
import { RiskPanel } from './components/RiskPanel'
import { RuleCatalogPanel } from './components/RuleCatalogPanel'
import { ScenarioFormWizard } from './components/ScenarioFormWizard'
import { ScenarioTimeline } from './components/ScenarioTimeline'
import { ScalingEqualizer } from './components/ScalingEqualizer'
import { ScalingMatrix3D } from './components/ScalingMatrix3D'
import { VariantComparator } from './components/VariantComparator'
import { AdrRecord } from './components/AdrRecord'
import type { EvaluationResponse, ExplanationResponse, ScenarioRequest, ScenarioSummary, ScenarioVariant } from './types'
import type { ObservabilitySnapshot } from './observabilityTypes'
import './styles.css'

const variants: ScenarioVariant[] = ['BASELINE', 'GROWTH_X2', 'GROWTH_X10']

export default function App() {
  const [scenario, setScenario] = useState<ScenarioRequest>(defaultScenario)
  const [evaluation, setEvaluation] = useState<EvaluationResponse | null>(null)
  const [selectedVariant, setSelectedVariant] = useState<ScenarioVariant>('BASELINE')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [explanation, setExplanation] = useState<ExplanationResponse | null>(null)
  const [explanationLoading, setExplanationLoading] = useState(false)
  const [explanationError, setExplanationError] = useState<string | null>(null)
  const [savedScenarios, setSavedScenarios] = useState<ScenarioSummary[]>([])
  const [saving, setSaving] = useState(false)
  const [persistenceError, setPersistenceError] = useState<string | null>(null)
  const [historyOpen, setHistoryOpen] = useState(false)
  const [historyItems, setHistoryItems] = useState<Array<{ evaluationId: string; scenarioName: string; generatedAt: string; catalogVersion: string }>>([])
  const [historyLoading, setHistoryLoading] = useState(false)
  const [catalogOpen, setCatalogOpen] = useState(false)
  const [catalogItems, setCatalogItems] = useState<Array<{ id: string; title: string; category: string; status: string }>>([])
  const [catalogLoading, setCatalogLoading] = useState(false)
  const [catalogVersions, setCatalogVersions] = useState<Array<{ id: string; version: string; name: string; source: string; createdAt: string; active: boolean }>>([])
  const [activatingCatalogVersion, setActivatingCatalogVersion] = useState(false)
  const [editingId, setEditingId] = useState<string | null>(null)
  const [activeScenarioId, setActiveScenarioId] = useState<string | null>(null)
  const [versions, setVersions] = useState<Array<{ id: string; scenarioId: string; versionLabel: string; createdAt: string }>>([])
  const [evaluationTimelineItems, setEvaluationTimelineItems] = useState<Array<{ evaluationId: string; scenarioId: string; scenarioName: string; catalogVersion: string; generatedAt: string; variant: string; peakRps: string; storage12MonthsGb: string; allowedDowntimeMinutesPerMonth: string; recommendationCount: number }>>([])
  const [costComplexityItems, setCostComplexityItems] = useState<Array<{ serviceName: string; estimatedMonthlyCost: number; complexityScore: number; driver: string }>>([])
  const [cloudCostItems, setCloudCostItems] = useState<Array<{ serviceName: string; cloudProvider: string; serviceType: string; region: string; unitMonthlyCost: number; quantity: number; monthlySubtotal: number; yearlySubtotal: number; driver: string }>>([])
  const [adr, setAdr] = useState<{ id: string; title: string; context: string; options: string; decision: string; consequences: string; generatedFromEvaluationId: string; generatedAt: string } | null>(null)
  const [adrLoading, setAdrLoading] = useState(false)
  const [observabilityOpen, setObservabilityOpen] = useState(false)
  const [observabilitySnapshot, setObservabilitySnapshot] = useState<ObservabilitySnapshot | null>(null)
  const [observabilityError, setObservabilityError] = useState<string | null>(null)
  const snapshotPlaceholder: import('./observabilityTypes').ObservabilitySnapshot = {
    snapshotId: 'empty-preview',
    connectionId: 'none',
    generatedAt: new Date(0).toISOString(),
    sourceWindow: 'PT0S',
    resources: [],
    relations: [],
    alerts: [],
    dataQualitySummary: {},
    partial: false,
    warnings: ['Open the 3D view to load the simulated observability snapshot.'],
  }

  useEffect(() => {
    refreshSavedScenarios()
    getSimulatedSnapshot('NORMAL', 1)
      .then(setObservabilitySnapshot)
      .catch((exception) => setObservabilityError(
        exception instanceof Error ? exception.message : 'Observability preview unavailable',
      ))
  }, [])

  async function saveEdit() {
    if (!editingId) return
    setSaving(true)
    setPersistenceError(null)
    try {
      await updateScenario(editingId, scenario)
      await refreshSavedScenarios()
      setEditingId(null)
    } catch (exception) {
      setPersistenceError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setSaving(false)
    }
  }

  async function handleDuplicate(id: string) {
    setSaving(true)
    setPersistenceError(null)
    try {
      const created = await duplicateScenario(id)
      await refreshSavedScenarios()
      const loaded = await getScenario(created.id)
      setScenario(loaded)
      setActiveScenarioId(created.id)
      setEvaluation(null)
      setExplanation(null)
      setEditingId(null)
    } catch (exception) {
      setPersistenceError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setSaving(false)
    }
  }

  async function startEditing(id: string) {
    setLoading(true)
    setError(null)
    try {
      const loaded = await getScenario(id)
      setScenario(loaded)
      setActiveScenarioId(id)
      setEvaluation(null)
      setExplanation(null)
      setEditingId(id)
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setLoading(false)
    }
  }

  function cancelEditing() {
    setEditingId(null)
  }

  async function refreshCatalog() {
    setCatalogLoading(true)
    try {
      const [items, versions] = await Promise.all([listRuleCatalog(), listRuleCatalogVersions()])
      setCatalogItems(items)
      setCatalogVersions(versions)
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setCatalogLoading(false)
    }
  }

  async function activateCatalogVersion(id: string) {
    setActivatingCatalogVersion(true)
    try {
      await activateRuleCatalogVersion(id)
      await refreshCatalog()
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setActivatingCatalogVersion(false)
    }
  }

  function toggleCatalog() {
    setCatalogOpen((prev) => !prev)
    if (!catalogOpen) {
      refreshCatalog()
    }
  }

  async function loadEvaluationHistoryItem(evaluationId: string) {
    setLoading(true)
    setError(null)
    try {
      const item = await getEvaluation(evaluationId)
      setEvaluation(item)
      setSelectedVariant('BASELINE')
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setLoading(false)
    }
  }

  async function refreshHistory() {
    setHistoryLoading(true)
    try {
      const items = await listEvaluations()
      setHistoryItems(items)
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setHistoryLoading(false)
    }
  }

  function toggleHistory() {
    setHistoryOpen((prev) => !prev)
    if (!historyOpen) {
      refreshHistory()
    }
  }

  const selected = useMemo(
    () => evaluation?.results.find((result) => result.variant === selectedVariant) ?? evaluation?.results[0],
    [evaluation, selectedVariant],
  )

  async function evaluate() {
    setLoading(true)
    setError(null)
    try {
      const response = await evaluateScenario(scenario, variants, activeScenarioId ?? undefined)
      setEvaluation(response)
      setSelectedVariant('BASELINE')
      loadAdr(response.evaluationId)
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setLoading(false)
    }
  }

  async function explain() {
    if (!evaluation) return
    setExplanationLoading(true)
    setExplanationError(null)
    try {
      const response = await requestExplanation(evaluation)
      setExplanation(response)
    } catch (exception) {
      setExplanationError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setExplanationLoading(false)
    }
  }

  async function refreshSavedScenarios() {
    try {
      const items = await listScenarios()
      setSavedScenarios(items)
    } catch (exception) {
      setPersistenceError(exception instanceof Error ? exception.message : 'Unexpected error')
    }
  }

  async function saveScenario() {
    setSaving(true)
    setPersistenceError(null)
    try {
      const created = await createScenario(scenario)
      setActiveScenarioId(created.id)
      await refreshSavedScenarios()
    } catch (exception) {
      setPersistenceError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setSaving(false)
    }
  }

  async function loadScenario(id: string) {
    setLoading(true)
    setError(null)
    try {
      const loaded = await getScenario(id)
      setScenario(loaded)
      setActiveScenarioId(id)
      setEvaluation(null)
      setExplanation(null)
      await loadVersions(id)
      await loadEvaluationTimeline(id)
      await loadCostComplexity(id)
      await loadCloudCost(id)
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'Unexpected error')
    } finally {
      setLoading(false)
    }
  }

  async function loadCloudCost(id: string) {
    try {
      const items = await estimateCloudCost(id)
      setCloudCostItems(items)
    } catch {
      setCloudCostItems([])
    }
  }

  async function loadAdr(id: string) {
    setAdrLoading(true)
    try {
      const record = await generateAdr(id)
      setAdr(record)
    } catch {
      setAdr(null)
    } finally {
      setAdrLoading(false)
    }
  }

  async function loadVersions(id: string) {
    try {
      const items = await listScenarioVersions(id)
      setVersions(items)
    } catch {
      setVersions([])
    }
  }

  async function loadEvaluationTimeline(id: string) {
    try {
      const items = await getEvaluationTimeline(id)
      setEvaluationTimelineItems(items)
    } catch {
      setEvaluationTimelineItems([])
    }
  }

  async function loadCostComplexity(id: string) {
    try {
      const items = await getCostComplexity(id)
      setCostComplexityItems(items)
    } catch {
      setCostComplexityItems([])
    }
  }

  async function removeScenario(id: string) {
    try {
      await deleteScenario(id)
      if (activeScenarioId === id) {
        setActiveScenarioId(null)
        setEditingId(null)
      }
      await refreshSavedScenarios()
    } catch (exception) {
      setPersistenceError(exception instanceof Error ? exception.message : 'Unexpected error')
    }
  }

  async function handleExport() {
    if (!editingId) return
    try {
      await exportScenario(editingId)
    } catch (exception) {
      setPersistenceError(exception instanceof Error ? exception.message : 'Unexpected error')
    }
  }

  async function handleImport() {
    const input = document.createElement('input')
    input.type = 'file'
    input.accept = 'application/json'
    input.onchange = async () => {
      const file = input.files?.[0]
      if (!file) return
      const reader = new FileReader()
      reader.onload = async () => {
        try {
          const json = JSON.parse(reader.result as string)
          const request = json as ScenarioRequest
          const created = await importScenario(request)
          await refreshSavedScenarios()
          const loaded = await getScenario(created.id)
          setScenario(loaded)
          setActiveScenarioId(created.id)
          setEvaluation(null)
          setExplanation(null)
          setEditingId(null)
        } catch (exception) {
          setPersistenceError(exception instanceof Error ? exception.message : 'Unexpected error')
        }
      }
      reader.readAsText(file)
    }
    input.click()
  }

  return (
    <main className="app-shell">
      <header className="topbar">
        <div className="brand-mark">SC</div>
        <div><p className="eyebrow">Architecture intelligence without black boxes</p><h1>ScaleCanvas</h1></div>
        <div className="topbar-actions">
          <span className="catalog-badge">Rules {evaluation?.catalogVersion ?? '1.0.0'}</span>
          <button className="secondary-button compact" disabled={!evaluation} onClick={() => evaluation && downloadMarkdown(evaluation)}>Export report</button>
          <button className="secondary-button compact" disabled={!evaluation} onClick={explain} title="Explain with AI">Explain</button>
          <button className="secondary-button compact" onClick={toggleHistory}>{historyOpen ? 'Close history' : 'History'}</button>
          <button className="secondary-button compact" onClick={toggleCatalog}>{catalogOpen ? 'Close catalog' : 'Rule catalog'}</button>
        </div>
      </header>

      <div className="workspace">
        <ScenarioFormWizard
            scenario={scenario}
            disabled={loading}
            onChange={setScenario}
            onEvaluate={evaluate}
            savedScenarios={savedScenarios}
            saving={saving}
            persistenceError={persistenceError}
            editingId={editingId}
            onSave={saveScenario}
            onSaveEdit={saveEdit}
            onCancelEdit={cancelEditing}
            onLoad={loadScenario}
            onStartEdit={startEditing}
            onDuplicate={handleDuplicate}
            onRemove={removeScenario}
            onExport={handleExport}
            onImport={handleImport}
        />
        <section className="results-panel">
          {catalogOpen && (
            <RuleCatalogPanel onClose={toggleCatalog} items={catalogItems} loading={catalogLoading} versions={catalogVersions} onActivateVersion={activateCatalogVersion} activatingVersion={activatingCatalogVersion} />
          )}
          {historyOpen && (
            <EvaluationHistoryPanel onClose={toggleHistory} onSelectEvaluation={loadEvaluationHistoryItem} items={historyItems} loading={historyLoading} />
          )}
          {!evaluation && !loading && (
            <div className="hero-empty">
              <p className="eyebrow">Portfolio-ready architecture lab</p>
              <h2>Model the system you have.<br />See the architecture you actually need.</h2>
              <p>Compare baseline, x2 and x10. Every recommendation exposes its threshold, evidence, trade-offs and simpler alternative.</p>
              <button className="primary-button compact" onClick={evaluate}>Run example scenario</button>
            </div>
          )}
          {loading && <div className="hero-empty"><div className="loader" /><h2>Evaluating deterministic rules…</h2></div>}
          {error && <div className="error-banner"><strong>Evaluation failed</strong><span>{error}</span></div>}
          {evaluation && selected && (
            <>
              <div className="results-heading">
                <div><p className="eyebrow">Evaluation result</p><h2>{evaluation.scenarioName}</h2></div>
                <div className="variant-tabs">
                  {evaluation.results.map((result) => (
                    <button className={selectedVariant === result.variant ? 'active' : ''} key={result.variant} onClick={() => setSelectedVariant(result.variant)}>
                      {result.variant === 'BASELINE' ? 'Baseline' : result.variant.replace('GROWTH_', '')}
                    </button>
                  ))}
                </div>
              </div>
              <MetricCards metrics={selected.derivedMetrics} />
              <QualityRadar scenario={scenario} />
              <RecommendationBoard recommendations={selected.recommendations} />
              <div className="visual-grid">
                <ArchitectureCanvas recommendations={selected.recommendations} />
                <RiskPanel risks={selected.risks} />
                <div className="visual-card">
                  <div className="section-title"><span>Observability</span></div>
                  <button className="secondary-button compact" onClick={() => setObservabilityOpen(true)}>Open 3D view</button>
                  <ObservabilityScene3DFallback snapshot={observabilitySnapshot ?? snapshotPlaceholder} />
                  {observabilityError && <p className="column-note">{observabilityError}</p>}
                </div>
              </div>
              <CapacityChart results={evaluation.results} />
              <ScalingEqualizer scenario={scenario} evaluation={evaluation} />
              <ScalingMatrix3D scenario={scenario} />
              <ScenarioTimeline versions={versions} />
              <EvaluationTimeline items={evaluationTimelineItems} />
              <CostComplexityMatrix items={costComplexityItems} />
              <CloudCostBreakdown items={cloudCostItems} />
              <VariantComparator results={evaluation.results} />
              <AdrRecord adr={adr} loading={adrLoading} onGenerate={() => loadAdr(evaluation.evaluationId)} />
              {observabilityOpen && (
                <div className="modal-backdrop" onClick={() => setObservabilityOpen(false)}>
                  <div className="modal" onClick={(e) => e.stopPropagation()}>
                    <div className="modal-header">
                      <h3>Observability 3D</h3>
                      <button className="modal-close" onClick={() => setObservabilityOpen(false)}>×</button>
                    </div>
                    <div className="modal-body">
                      <ObservabilityScene3D />
                    </div>
                  </div>
                </div>
              )}
            </>
          )}
          {explanationLoading && <div className="hero-empty"><div className="loader" /><h2>Generating explanation…</h2></div>}
          {explanationError && <div className="error-banner"><strong>Explanation failed</strong><span>{explanationError}</span></div>}
          {explanation && (
            <div className="modal-backdrop" onClick={() => setExplanation(null)}>
              <div className="modal" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                  <h3>AI Explanation</h3>
                  <button className="modal-close" onClick={() => setExplanation(null)}>×</button>
                </div>
                <div className="modal-body">
                  <p><strong>Provider:</strong> {explanation.provider}</p>
                  <p><strong>Model:</strong> {explanation.model}</p>
                  <pre>{explanation.explanation}</pre>
                </div>
              </div>
            </div>
          )}
        </section>
      </div>
    </main>
  )
}
