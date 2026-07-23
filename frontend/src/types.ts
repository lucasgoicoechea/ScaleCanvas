export type ProductType =
  | 'INTERNAL_CRUD'
  | 'SAAS_B2B'
  | 'ECOMMERCE'
  | 'BANKING_API'
  | 'MARKETPLACE'
  | 'DOCUMENT_AI'
  | 'IOT_INGESTION'
  | 'SEARCH_PLATFORM'
  | 'MOBILE_APPLICATION'
  | 'STREAMING_METADATA'

export type MaturityLevel = 'LOW' | 'MEDIUM' | 'HIGH'
export type ScenarioVariant = 'BASELINE' | 'GROWTH_X2' | 'GROWTH_X10'
export type Urgency = 'DO_NOW' | 'WATCH' | 'NOT_YET'
export type ServerType = 'VM' | 'CONTAINER' | 'SERVERLESS' | 'BARE_METAL'
export type CloudProvider = 'AWS' | 'GCP' | 'AZURE' | 'ON_PREM' | 'HYBRID'
export type DeploymentService = 'ECS' | 'EKS' | 'LAMBDA' | 'APP_SERVICE' | 'VM_MANUAL' | 'CLOUD_RUN' | 'OTHER'
export type GatewayType = 'API_GATEWAY' | 'ALB' | 'NLB' | 'CLOUDFLARE' | 'KONG' | 'NGINX' | 'NONE'
export type LoadBalancerType = 'ALB' | 'NLB' | 'CLB' | 'CLOUDFLARE' | 'KONG' | 'NGINX' | 'NONE'
export type ServiceBinding = 'SHARED' | 'DEDICATED' | 'SERVERLESS'

export interface ScenarioRequest {
  name: string
  description: string
  productType: ProductType
  workload: {
    registeredUsers: number
    dailyActiveUsers: number
    concurrentUsers: number
    averageRps: number
    peakRps: number
    burstFactor: number
    readPercentage: number
    writePercentage: number
    averagePayloadBytes: number
    maximumPayloadBytes: number
    batchJobsPerDay: number
    asynchronousWorkPercentage: number
  }
  data: {
    currentStorageGb: number
    monthlyGrowthPercentage: number
    retentionMonths: number
    objectStorageGb: number
    hotDataPercentage: number
    eventVolumePerDay: number
  }
  quality: {
    targetP50Ms: number
    targetP95Ms: number
    targetP99Ms: number
    availabilitySloPercent: number
    rtoMinutes: number
    rpoMinutes: number
    consistencyLevel: 'EVENTUAL' | 'READ_YOUR_WRITES' | 'STRONG'
    geographicScope: 'LOCAL' | 'COUNTRY' | 'MULTI_COUNTRY' | 'GLOBAL'
  }
  organization: {
    teamSize: number
    operationsMaturity: MaturityLevel
    deploymentFrequencyPerWeek: number
    onCallAvailable: boolean
    observabilityMaturity: MaturityLevel
    cloudExperience: MaturityLevel
    budgetBand: 'MINIMAL' | 'MODERATE' | 'FLEXIBLE'
  }
  deployment: {
    serverType: ServerType
    cloudProvider: CloudProvider
    deploymentService: DeploymentService
    gatewayType: GatewayType
    loadBalancerType: LoadBalancerType
    minimumUnitMemoryMb: number
    minimumUnitCpuCount: number
    serviceTopology: {
      totalServices: number
      microservicesCount: number
      scalableServicesCount: number
      services: Array<{
        serviceName: string
        requestsPerMinute: number
        memoryMb: number
        cpuCount: number
        replicas: number
        serverBinding: ServiceBinding
      }>
    }
  }
}

export interface ScenarioSummary {
  id: string
  name: string
  description: string
  productType: ProductType
  createdAt: string
  updatedAt: string
}

export interface DerivedMetrics {
  dailyRequests: number
  peakHourRequests: number
  dailyTransferGb: number
  readRps: number
  writeRps: number
  storageAfter12MonthsGb: number
  allowedUnavailabilityMinutesPerMonth: number
}

export interface RuleOutcome {
  ruleId: string
  title: string
  version: string
  status: string
  urgency: Urgency
  action: string
  component: string
  rationale: string
  threshold: string
  evidence: Record<string, string>
  benefits: string[]
  tradeoffs: string[]
  simplerAlternative: string
}

export interface RiskFinding {
  level: 'LOW' | 'MEDIUM' | 'HIGH'
  title: string
  detail: string
}

export interface VariantResult {
  variant: ScenarioVariant
  derivedMetrics: DerivedMetrics
  recommendations: RuleOutcome[]
  risks: RiskFinding[]
}

export interface EvaluationSummary {
  evaluationId: string
  scenarioId?: string
  scenarioName: string
  catalogVersion: string
  generatedAt: string
}

export interface EvaluationResponse {
  evaluationId: string
  scenarioName: string
  catalogVersion: string
  generatedAt: string
  results: VariantResult[]
}

export interface ExplanationResponse {
  generatedAt: string
  explanation: string
  provider: string
  model: string
}

export interface ExplanationRequest {
  evaluation: EvaluationResponse
  question?: string
}
