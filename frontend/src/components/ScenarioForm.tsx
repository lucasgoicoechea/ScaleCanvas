import { NumberField } from './NumberField'
import type { MaturityLevel, ProductType, ScenarioRequest, ScenarioSummary } from '../types'
import { goldenMasters } from '../goldenMasterScenarios'

interface ScenarioFormProps {
  section?: 'all' | 'demand' | 'data-quality' | 'infrastructure' | 'actions'
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

const productTypes: ProductType[] = [
  'INTERNAL_CRUD', 'SAAS_B2B', 'ECOMMERCE', 'BANKING_API', 'MARKETPLACE',
  'DOCUMENT_AI', 'IOT_INGESTION', 'SEARCH_PLATFORM', 'MOBILE_APPLICATION', 'STREAMING_METADATA',
]
const maturities: MaturityLevel[] = ['LOW', 'MEDIUM', 'HIGH']
const serverTypes = ['VM', 'CONTAINER', 'SERVERLESS', 'BARE_METAL'] as const
const cloudProviders = ['AWS', 'GCP', 'AZURE', 'ON_PREM', 'HYBRID'] as const
const deploymentServices = ['ECS', 'EKS', 'LAMBDA', 'APP_SERVICE', 'VM_MANUAL', 'CLOUD_RUN', 'OTHER'] as const
const gatewayTypes = ['API_GATEWAY', 'ALB', 'NLB', 'CLOUDFLARE', 'KONG', 'NGINX', 'NONE'] as const
const loadBalancerTypes = ['ALB', 'NLB', 'CLB', 'CLOUDFLARE', 'KONG', 'NGINX', 'NONE'] as const
const serviceBindings = ['SHARED', 'DEDICATED', 'SERVERLESS'] as const

export function ScenarioForm({ section = 'all', scenario, disabled, onChange, onEvaluate, savedScenarios, saving, persistenceError, editingId, onSave, onSaveEdit, onCancelEdit, onLoad, onStartEdit, onDuplicate, onRemove, onExport, onImport }: ScenarioFormProps) {
  const setRoot = <K extends keyof ScenarioRequest>(key: K, value: ScenarioRequest[K]) =>
    onChange({ ...scenario, [key]: value })
  const setWorkload = <K extends keyof ScenarioRequest['workload']>(key: K, value: ScenarioRequest['workload'][K]) =>
    setRoot('workload', { ...scenario.workload, [key]: value })
  const setData = <K extends keyof ScenarioRequest['data']>(key: K, value: ScenarioRequest['data'][K]) =>
    setRoot('data', { ...scenario.data, [key]: value })
  const setQuality = <K extends keyof ScenarioRequest['quality']>(key: K, value: ScenarioRequest['quality'][K]) =>
    setRoot('quality', { ...scenario.quality, [key]: value })
  const setOrganization = <K extends keyof ScenarioRequest['organization']>(
    key: K,
    value: ScenarioRequest['organization'][K],
  ) => setRoot('organization', { ...scenario.organization, [key]: value })
  const setDeployment = <K extends keyof ScenarioRequest['deployment']>(key: K, value: ScenarioRequest['deployment'][K]) =>
    setRoot('deployment', { ...scenario.deployment, [key]: value })
  const setServiceTopology = <K extends keyof ScenarioRequest['deployment']['serviceTopology']>(
    key: K,
    value: ScenarioRequest['deployment']['serviceTopology'][K],
  ) =>
    setDeployment('serviceTopology', { ...scenario.deployment.serviceTopology, [key]: value })
  const updateService = (index: number, patch: Partial<ScenarioRequest['deployment']['serviceTopology']['services'][0]>) => {
    const services = [...scenario.deployment.serviceTopology.services]
    services[index] = { ...services[index], ...patch }
    setServiceTopology('services', services)
  }
  const addService = () => {
    const services = [...scenario.deployment.serviceTopology.services, {
      serviceName: 'service-' + (scenario.deployment.serviceTopology.services.length + 1),
      requestsPerMinute: 0,
      memoryMb: 0,
      cpuCount: 1,
      replicas: 1,
      serverBinding: 'SHARED' as ScenarioRequest['deployment']['serviceTopology']['services'][0]['serverBinding'],
    }]
    setServiceTopology('services', services)
  }
  const removeService = (index: number) => {
    const services = scenario.deployment.serviceTopology.services.filter((_, i) => i !== index)
    setServiceTopology('services', services)
  }

  return (
    <aside className="scenario-panel">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">Scenario input</p>
          <h2>System profile</h2>
        </div>
        <span className="status-dot">Deterministic</span>
      </div>

      <div hidden={section !== 'all'}>
      <label className="field">
        <span>Name</span>
        <input value={scenario.name} onChange={(event) => setRoot('name', event.target.value)} />
      </label>
      <label className="field">
        <span>Golden master</span>
        <select value="" onChange={(event) => {
          const found = goldenMasters.find((gm) => gm.id === event.target.value)
          if (found) onChange(found.scenario)
        }}>
          <option value="">Load example...</option>
          {goldenMasters.map((gm) => <option key={gm.id} value={gm.id}>{gm.name} — {gm.description}</option>)}
        </select>
      </label>
      <label className="field">
        <span>Product type</span>
        <select value={scenario.productType} onChange={(event) => setRoot('productType', event.target.value as ProductType)}>
          {productTypes.map((value) => <option key={value}>{value}</option>)}
        </select>
      </label>
      </div>

      <details open hidden={section !== 'all' && section !== 'demand'}>
        <summary>Demand</summary>
        <div className="form-grid">
          <NumberField label="Average RPS" value={scenario.workload.averageRps} min={0} step={1} onChange={(v) => setWorkload('averageRps', v)} />
          <NumberField label="Peak RPS" value={scenario.workload.peakRps} min={0} step={1} onChange={(v) => setWorkload('peakRps', v)} />
          <NumberField label="Concurrent users" value={scenario.workload.concurrentUsers} min={0} onChange={(v) => setWorkload('concurrentUsers', v)} />
          <NumberField label="Burst factor" value={scenario.workload.burstFactor} min={1} step={0.1} suffix="×" onChange={(v) => setWorkload('burstFactor', v)} />
          <NumberField label="Read" value={scenario.workload.readPercentage} min={0} max={100} suffix="%" onChange={(v) => setWorkload('readPercentage', v)} />
          <NumberField label="Write" value={scenario.workload.writePercentage} min={0} max={100} suffix="%" onChange={(v) => setWorkload('writePercentage', v)} />
          <NumberField label="Async work" value={scenario.workload.asynchronousWorkPercentage} min={0} max={100} suffix="%" onChange={(v) => setWorkload('asynchronousWorkPercentage', v)} />
          <NumberField label="Max payload" value={scenario.workload.maximumPayloadBytes} min={0} suffix="bytes" onChange={(v) => setWorkload('maximumPayloadBytes', v)} />
        </div>
      </details>

      <details open hidden={section !== 'all' && section !== 'data-quality'}>
        <summary>Data</summary>
        <div className="form-grid">
          <NumberField label="Current storage" value={scenario.data.currentStorageGb} min={0} step={1} suffix="GB" onChange={(v) => setData('currentStorageGb', v)} />
          <NumberField label="Monthly growth" value={scenario.data.monthlyGrowthPercentage} min={-99} step={0.1} suffix="%" onChange={(v) => setData('monthlyGrowthPercentage', v)} />
          <NumberField label="Object storage" value={scenario.data.objectStorageGb} min={0} suffix="GB" onChange={(v) => setData('objectStorageGb', v)} />
          <NumberField label="Events/day" value={scenario.data.eventVolumePerDay} min={0} step={1000} onChange={(v) => setData('eventVolumePerDay', v)} />
        </div>
      </details>

      <details open hidden={section !== 'all' && section !== 'data-quality'}>
        <summary>Quality and organization</summary>
        <div className="form-grid">
          <NumberField label="Availability" value={scenario.quality.availabilitySloPercent} min={90} max={100} step={0.01} suffix="%" onChange={(v) => setQuality('availabilitySloPercent', v)} />
          <NumberField label="Target p95" value={scenario.quality.targetP95Ms} min={1} suffix="ms" onChange={(v) => setQuality('targetP95Ms', v)} />
          <NumberField label="RTO" value={scenario.quality.rtoMinutes} min={0} suffix="min" onChange={(v) => setQuality('rtoMinutes', v)} />
          <NumberField label="RPO" value={scenario.quality.rpoMinutes} min={0} suffix="min" onChange={(v) => setQuality('rpoMinutes', v)} />
          <NumberField label="Team size" value={scenario.organization.teamSize} min={1} onChange={(v) => setOrganization('teamSize', v)} />
          <NumberField label="Deployments/week" value={scenario.organization.deploymentFrequencyPerWeek} min={0} onChange={(v) => setOrganization('deploymentFrequencyPerWeek', v)} />
          <label className="field">
            <span>Operations maturity</span>
            <select value={scenario.organization.operationsMaturity} onChange={(e) => setOrganization('operationsMaturity', e.target.value as MaturityLevel)}>
              {maturities.map((value) => <option key={value}>{value}</option>)}
            </select>
          </label>
          <label className="field">
            <span>Geographic scope</span>
            <select value={scenario.quality.geographicScope} onChange={(e) => setQuality('geographicScope', e.target.value as ScenarioRequest['quality']['geographicScope'])}>
              <option>LOCAL</option><option>COUNTRY</option><option>MULTI_COUNTRY</option><option>GLOBAL</option>
            </select>
          </label>
        </div>
        <label className="checkbox-field">
          <input type="checkbox" checked={scenario.organization.onCallAvailable} onChange={(e) => setOrganization('onCallAvailable', e.target.checked)} />
          On-call coverage available
        </label>
      </details>

      <details open hidden={section !== 'all' && section !== 'infrastructure'}>
        <summary>Infrastructure</summary>
        <div className="form-grid">
          <label className="field">
            <span>Server type</span>
            <select value={scenario.deployment.serverType} onChange={(e) => setDeployment('serverType', e.target.value as ScenarioRequest['deployment']['serverType'])}>
              {serverTypes.map((value) => <option key={value}>{value}</option>)}
            </select>
          </label>
          <label className="field">
            <span>Cloud provider</span>
            <select value={scenario.deployment.cloudProvider} onChange={(e) => setDeployment('cloudProvider', e.target.value as ScenarioRequest['deployment']['cloudProvider'])}>
              {cloudProviders.map((value) => <option key={value}>{value}</option>)}
            </select>
          </label>
          <label className="field">
            <span>Deployment service</span>
            <select value={scenario.deployment.deploymentService} onChange={(e) => setDeployment('deploymentService', e.target.value as ScenarioRequest['deployment']['deploymentService'])}>
              {deploymentServices.map((value) => <option key={value}>{value}</option>)}
            </select>
          </label>
          <label className="field">
            <span>Gateway</span>
            <select value={scenario.deployment.gatewayType} onChange={(e) => setDeployment('gatewayType', e.target.value as ScenarioRequest['deployment']['gatewayType'])}>
              {gatewayTypes.map((value) => <option key={value}>{value}</option>)}
            </select>
          </label>
          <label className="field">
            <span>Load balancer</span>
            <select value={scenario.deployment.loadBalancerType} onChange={(e) => setDeployment('loadBalancerType', e.target.value as ScenarioRequest['deployment']['loadBalancerType'])}>
              {loadBalancerTypes.map((value) => <option key={value}>{value}</option>)}
            </select>
          </label>
          <NumberField label="Unit memory" value={scenario.deployment.minimumUnitMemoryMb} min={0} step={256} suffix="MB" onChange={(v) => setDeployment('minimumUnitMemoryMb', v)} />
          <NumberField label="Unit CPU" value={scenario.deployment.minimumUnitCpuCount} min={0} step={1} suffix="vCPU" onChange={(v) => setDeployment('minimumUnitCpuCount', v)} />
          <NumberField label="Total services" value={scenario.deployment.serviceTopology.totalServices} min={0} step={1} onChange={(v) => setServiceTopology('totalServices', v)} />
          <NumberField label="Microservices" value={scenario.deployment.serviceTopology.microservicesCount} min={0} step={1} onChange={(v) => setServiceTopology('microservicesCount', v)} />
          <NumberField label="Scalable services" value={scenario.deployment.serviceTopology.scalableServicesCount} min={0} step={1} onChange={(v) => setServiceTopology('scalableServicesCount', v)} />
        </div>

        <div className="service-list">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Services</p>
              <h2>Service capacity</h2>
            </div>
            <button className="secondary-button compact" onClick={addService}>Add service</button>
          </div>
          {scenario.deployment.serviceTopology.services.map((service, index) => (
            <div className="service-row" key={index}>
              <input value={service.serviceName} onChange={(e) => updateService(index, { serviceName: e.target.value })} />
              <NumberField label="RPM" value={service.requestsPerMinute} min={0} step={1} onChange={(v) => updateService(index, { requestsPerMinute: v })} />
              <NumberField label="Memory MB" value={service.memoryMb} min={0} step={256} onChange={(v) => updateService(index, { memoryMb: v })} />
              <NumberField label="CPU" value={service.cpuCount} min={0} step={1} onChange={(v) => updateService(index, { cpuCount: v })} />
              <NumberField label="Replicas" value={service.replicas} min={1} step={1} onChange={(v) => updateService(index, { replicas: v })} />
              <select value={service.serverBinding} onChange={(e) => updateService(index, { serverBinding: e.target.value as ScenarioRequest['deployment']['serviceTopology']['services'][0]['serverBinding'] })}>
                {serviceBindings.map((value) => <option key={value}>{value}</option>)}
              </select>
              <button className="secondary-button compact" onClick={() => removeService(index)}>Remove</button>
            </div>
          ))}
        </div>
      </details>

      <div className="persistence-section" hidden={section !== 'all' && section !== 'actions'}>
        <div className="panel-heading">
          <div>
            <p className="eyebrow">Saved scenarios</p>
            <h2>Persistence</h2>
          </div>
          <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
            <button className="secondary-button compact" onClick={onImport}>Import JSON</button>
            <button className="secondary-button compact" onClick={onExport} disabled={!editingId}>Export JSON</button>
            {editingId ? (
              <>
                <button className="secondary-button compact" disabled={saving} onClick={onCancelEdit}>Cancel</button>
                <button className="primary-button compact" disabled={saving} onClick={onSaveEdit}>{saving ? 'Saving…' : 'Save changes'}</button>
              </>
            ) : (
              <button className="secondary-button compact" disabled={saving} onClick={onSave}>{saving ? 'Saving…' : 'Save'}</button>
            )}
          </div>
        </div>
        {persistenceError && <div className="error-banner"><strong>Error</strong><span>{persistenceError}</span></div>}
        <div className="saved-list">
          {savedScenarios.length === 0 && <p className="column-note">No saved scenarios yet.</p>}
          {savedScenarios.map((item) => (
            <div className="saved-item" key={item.id}>
              <div>
                <strong>{item.name}</strong>
                <div className="column-note">{item.productType} · {new Date(item.updatedAt).toLocaleString()}</div>
              </div>
              <div className="saved-actions">
                {editingId === item.id ? (
                  <>
                    <button className="secondary-button compact" onClick={() => onCancelEdit()}>Cancel</button>
                    <button className="primary-button compact" disabled={saving} onClick={onSaveEdit}>{saving ? 'Saving…' : 'Save'}</button>
                  </>
                ) : (
                  <>
                    <button className="secondary-button compact" onClick={() => onLoad(item.id)}>Load</button>
                    <button className="secondary-button compact" onClick={() => onStartEdit(item.id)}>Edit</button>
                    <button className="secondary-button compact" onClick={() => onDuplicate(item.id)}>Duplicate</button>
                    <button className="secondary-button compact" onClick={() => onRemove(item.id)}>Delete</button>
                  </>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      <button className="primary-button" hidden={section !== 'all' && section !== 'actions'} disabled={disabled} onClick={onEvaluate}>
        {disabled ? 'Evaluating…' : 'Evaluate architecture'}
      </button>
    </aside>
  )
}
