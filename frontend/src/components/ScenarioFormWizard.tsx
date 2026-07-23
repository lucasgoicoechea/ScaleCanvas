import { useState } from 'react'
import { ScenarioForm } from './ScenarioForm'
import type { ScenarioRequest, ScenarioSummary } from '../types'

interface ScenarioFormWizardProps {
  scenario: ScenarioRequest
  disabled: boolean
  onChange: (scenario: ScenarioRequest) => void
  onEvaluate: () => void
  savedScenarios: ScenarioSummary[]
  saving: boolean
  persistenceError: string | null
  editingId: string | null
  onSave: () => void
  onSaveEdit: () => void
  onCancelEdit: () => void
  onLoad: (id: string) => void
  onStartEdit: (id: string) => void
  onDuplicate: (id: string) => void
  onRemove: (id: string) => void
  onExport: () => void
  onImport: () => void
}

const steps = [
  { key: 'identity', label: 'Identity' },
  { key: 'demand', label: 'Demand' },
  { key: 'data-quality', label: 'Data & quality' },
  { key: 'infrastructure', label: 'Infrastructure' },
  { key: 'actions', label: 'Actions' },
]

export function ScenarioFormWizard(props: ScenarioFormWizardProps) {
  const [stepIndex, setStepIndex] = useState(0)

  const go = (index: number) => setStepIndex(Math.max(0, Math.min(steps.length - 1, index)))

  return (
    <aside className="scenario-panel">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">Scenario input</p>
          <h2>System profile</h2>
        </div>
        <span className="status-dot">Deterministic</span>
      </div>

      <div className="wizard">
        <div className="wizard-stepper">
          {steps.map((step, index) => (
            <button
              key={step.key}
              className={`wizard-step ${index === stepIndex ? 'active' : ''} ${index < stepIndex ? 'done' : ''}`}
              onClick={() => go(index)}
              type="button"
            >
              <span className="wizard-step-marker">{index + 1}</span>
              <span className="wizard-step-label">{step.label}</span>
            </button>
          ))}
        </div>

        <div className="wizard-body">
          {stepIndex === 0 && (
            <div className="wizard-step-content">
              <label className="field">
                <span>Name</span>
                <input value={props.scenario.name} onChange={(event) => props.onChange({ ...props.scenario, name: event.target.value })} />
              </label>
              <label className="field">
                <span>Product type</span>
                <select value={props.scenario.productType} onChange={(event) => props.onChange({ ...props.scenario, productType: event.target.value as ScenarioRequest['productType'] })}>
                  {['INTERNAL_CRUD', 'SAAS_B2B', 'ECOMMERCE', 'BANKING_API', 'MARKETPLACE', 'DOCUMENT_AI', 'IOT_INGESTION', 'SEARCH_PLATFORM', 'MOBILE_APPLICATION', 'STREAMING_METADATA'].map((value) => <option key={value}>{value}</option>)}
                </select>
              </label>
            </div>
          )}

          {stepIndex === 1 && (
            <div className="wizard-step-content">
              <p className="column-note">Demand inputs</p>
              <ScenarioForm {...props} section="demand" />
            </div>
          )}

          {stepIndex === 2 && (
            <div className="wizard-step-content">
              <p className="column-note">Data, quality and organization</p>
              <ScenarioForm {...props} section="data-quality" />
            </div>
          )}

          {stepIndex === 3 && (
            <div className="wizard-step-content">
              <p className="column-note">Infrastructure and services</p>
              <ScenarioForm {...props} section="infrastructure" />
            </div>
          )}

          {stepIndex === 4 && (
            <div className="wizard-step-content">
              <p className="column-note">Persistence and evaluation</p>
              <ScenarioForm {...props} section="actions" />
            </div>
          )}
        </div>

        <div className="wizard-footer">
          <button className="secondary-button compact" disabled={stepIndex === 0} onClick={() => go(stepIndex - 1)} type="button">Back</button>
          <button className="secondary-button compact" disabled={stepIndex === steps.length - 1} onClick={() => go(stepIndex + 1)} type="button">Next</button>
        </div>
      </div>
    </aside>
  )
}
