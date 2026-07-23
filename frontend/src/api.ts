import type { EvaluationResponse, EvaluationSummary, ExplanationResponse, ScenarioRequest, ScenarioVariant, ScenarioSummary } from './types'

async function parseError(response: Response): Promise<string> {
  try {
    const problem = (await response.json()) as { detail?: string; violations?: Record<string, string> }
    if (problem.violations) {
      return Object.entries(problem.violations)
        .map(([field, message]) => `${field}: ${message}`)
        .join('; ')
    }
    return problem.detail ?? `Request failed with status ${response.status}`
  } catch {
    return `Request failed with status ${response.status}`
  }
}

export async function evaluateScenario(
  scenario: ScenarioRequest,
  variants: ScenarioVariant[],
  scenarioId?: string,
): Promise<EvaluationResponse> {
  const response = await fetch('/api/v1/evaluations', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ scenarioId, scenario, variants }),
  })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as EvaluationResponse
}

export async function downloadMarkdown(evaluation: EvaluationResponse): Promise<void> {
  const response = await fetch('/api/v1/reports/markdown', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(evaluation),
  })
  if (!response.ok) throw new Error(await parseError(response))
  const markdown = await response.text()
  const blob = new Blob([markdown], { type: 'text/markdown' })
  const url = URL.createObjectURL(blob)
  const anchor = document.createElement('a')
  anchor.href = url
  anchor.download = `scalecanvas-${evaluation.scenarioName.toLowerCase().replace(/[^a-z0-9]+/g, '-')}.md`
  anchor.click()
  URL.revokeObjectURL(url)
}

export async function requestExplanation(
  evaluation: EvaluationResponse,
  question?: string,
): Promise<ExplanationResponse> {
  const response = await fetch('/api/v1/explanations', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ evaluation, question }),
  })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as ExplanationResponse
}

export async function createScenario(scenario: ScenarioRequest): Promise<ScenarioSummary> {
  const response = await fetch('/api/v1/scenarios', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(scenario),
  })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as ScenarioSummary
}

export async function listScenarios(): Promise<ScenarioSummary[]> {
  const response = await fetch('/api/v1/scenarios')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as ScenarioSummary[]
}

export async function getScenario(id: string): Promise<ScenarioRequest> {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id))
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as ScenarioRequest
}

export async function getCostComplexity(id: string) {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id) + '/cost-complexity')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as Array<{ serviceName: string; estimatedMonthlyCost: number; complexityScore: number; driver: string }>
}

export async function listScenarioVersions(id: string): Promise<Array<{ id: string; scenarioId: string; versionLabel: string; createdAt: string }>> {
  const response = await fetch('/api/v1/scenario-versions/' + encodeURIComponent(id))
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as Array<{ id: string; scenarioId: string; versionLabel: string; createdAt: string }>
}

export async function deleteScenario(id: string): Promise<void> {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id), { method: 'DELETE' })
  if (!response.ok) throw new Error(await parseError(response))
}

export async function updateScenario(id: string, scenario: ScenarioRequest): Promise<ScenarioRequest> {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id), {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(scenario),
  })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as ScenarioRequest
}

export async function duplicateScenario(id: string): Promise<ScenarioSummary> {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id) + '/duplicate', { method: 'POST' })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as ScenarioSummary
}

export async function exportScenario(id: string): Promise<void> {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id) + '/export')
  if (!response.ok) throw new Error(await parseError(response))
  const blob = await response.blob()
  const url = URL.createObjectURL(blob)
  const anchor = document.createElement('a')
  anchor.href = url
  anchor.download = 'scenario-' + id + '.json'
  anchor.click()
  URL.revokeObjectURL(url)
}

export async function importScenario(request: ScenarioRequest): Promise<ScenarioSummary> {
  const response = await fetch('/api/v1/scenarios/import', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(request),
  })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as ScenarioSummary
}

export async function listEvaluations(): Promise<EvaluationSummary[]> {
  const response = await fetch('/api/v1/evaluations')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as EvaluationSummary[]
}

export async function generateAdr(id: string) {
  const response = await fetch('/api/v1/evaluations/' + encodeURIComponent(id) + '/adr')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as { id: string; title: string; context: string; options: string; decision: string; consequences: string; generatedFromEvaluationId: string; generatedAt: string }
}

export async function estimateCloudCost(id: string) {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id) + '/cloud-cost')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as Array<{ serviceName: string; cloudProvider: string; serviceType: string; region: string; unitMonthlyCost: number; quantity: number; monthlySubtotal: number; yearlySubtotal: number; driver: string }>
}

export async function listRuleCatalogVersions() {
  const response = await fetch('/api/v1/rule-catalog/versions')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as Array<{ id: string; version: string; name: string; source: string; createdAt: string; active: boolean }>
}

export async function activateRuleCatalogVersion(id: string) {
  const response = await fetch('/api/v1/rule-catalog/versions/' + encodeURIComponent(id) + '/activate', { method: 'POST' })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as { id: string; version: string; name: string; source: string; createdAt: string; active: boolean }
}

export async function getEvaluation(id: string): Promise<EvaluationResponse> {
  const response = await fetch('/api/v1/evaluations/' + encodeURIComponent(id))
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as EvaluationResponse
}

export async function getEvaluationTimeline(id: string) {
  const response = await fetch('/api/v1/evaluations/scenario/' + encodeURIComponent(id))
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as Array<{
    evaluationId: string
    scenarioId: string
    scenarioName: string
    catalogVersion: string
    generatedAt: string
    variant: string
    peakRps: string
    storage12MonthsGb: string
    allowedDowntimeMinutesPerMonth: string
    recommendationCount: number
  }>
}

export async function getDeployment(id: string): Promise<ScenarioRequest['deployment']> {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id) + '/deployment')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as ScenarioRequest['deployment']
}

export async function updateDeployment(id: string, deployment: ScenarioRequest['deployment']): Promise<ScenarioRequest['deployment']> {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id) + '/deployment', {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(deployment),
  })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as ScenarioRequest['deployment']
}

export async function getScalingMatrix(id: string): Promise<{
  serverType: string
  cloudProvider: string
  minimumUnitMemoryMb: number
  minimumUnitCpuCount: number
  services: string[]
  maxReplicas: number
  matrix: Array<{
    serviceName: string
    serverIndex: number
    utilizationPercent: number
    requestsPerMinuteShare: number
    replicas: number
  }>
}> {
  const response = await fetch('/api/v1/scenarios/' + encodeURIComponent(id) + '/scaling-matrix')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as {
    serverType: string
    cloudProvider: string
    minimumUnitMemoryMb: number
    minimumUnitCpuCount: number
    services: string[]
    maxReplicas: number
    matrix: Array<{
      serviceName: string
      serverIndex: number
      utilizationPercent: number
      requestsPerMinuteShare: number
      replicas: number
    }>
  }
}

export async function listRuleCatalog(): Promise<Array<{ id: string; title: string; category: string; status: string }>> {
  const response = await fetch('/api/v1/rule-catalog')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as Array<{ id: string; title: string; category: string; status: string }>
}

export async function listProviders(): Promise<Array<{
  providerType: string
  status: 'IMPLEMENTED' | 'CONTRACT_ONLY'
  readOnly: boolean
  capabilities: {
    discoverResources: boolean
    queryMetrics: boolean
    queryAlarms: boolean
  }
}>> {
  const response = await fetch('/api/v1/observability/providers')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as Array<{
    providerType: string
    status: 'IMPLEMENTED' | 'CONTRACT_ONLY'
    readOnly: boolean
    capabilities: {
      discoverResources: boolean
      queryMetrics: boolean
      queryAlarms: boolean
    }
  }>
}

export async function listConnections(): Promise<import('./observabilityTypes').ProviderConnection[]> {
  const response = await fetch('/api/v1/observability/connections')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as import('./observabilityTypes').ProviderConnection[]
}

export async function createConnection(connection: import('./observabilityTypes').ProviderConnection): Promise<import('./observabilityTypes').ProviderConnection> {
  const response = await fetch('/api/v1/observability/connections', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(connection),
  })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as import('./observabilityTypes').ProviderConnection
}

export async function deleteConnection(id: string): Promise<void> {
  const response = await fetch('/api/v1/observability/connections/' + encodeURIComponent(id), { method: 'DELETE' })
  if (!response.ok) throw new Error(await parseError(response))
}

export async function testConnection(id: string): Promise<{ ok: boolean; state: string; detail?: string }> {
  const response = await fetch('/api/v1/observability/connections/' + encodeURIComponent(id) + '/test', { method: 'POST' })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as { ok: boolean; state: string; detail?: string }
}

export async function syncConnection(id: string): Promise<import('./observabilityTypes').ObservabilitySnapshot> {
  const response = await fetch('/api/v1/observability/connections/' + encodeURIComponent(id) + '/sync', { method: 'POST' })
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as import('./observabilityTypes').ObservabilitySnapshot
}

export async function getSimulatedSnapshot(profile?: string, seed?: number): Promise<import('./observabilityTypes').ObservabilitySnapshot> {
  const url = new URL('/api/v1/observability/snapshot/simulated', window.location.origin)
  if (profile) url.searchParams.set('profile', profile)
  if (seed !== undefined) url.searchParams.set('seed', String(seed))
  const response = await fetch(url.toString())
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as import('./observabilityTypes').ObservabilitySnapshot
}

export async function listScenarioTemplates(): Promise<Array<{ id: string; name: string; description: string; scenarioJson: string }>> {
  const response = await fetch('/api/v1/scenario-templates')
  if (!response.ok) throw new Error(await parseError(response))
  return (await response.json()) as Array<{ id: string; name: string; description: string; scenarioJson: string }>
}
