import type { EvaluationResponse, ScenarioRequest } from '../types'

interface ScalingEqualizerProps {
  scenario: ScenarioRequest
  evaluation: EvaluationResponse | null
}

export function ScalingEqualizer({ scenario, evaluation }: ScalingEqualizerProps) {
  const services = scenario.deployment.serviceTopology.services
  const unitMemory = scenario.deployment.minimumUnitMemoryMb
  const unitCpu = scenario.deployment.minimumUnitCpuCount

  const totalRpm = services.reduce((sum, s) => sum + s.requestsPerMinute, 0)
  const totalMemory = services.reduce((sum, s) => sum + s.memoryMb * s.replicas, 0)
  const totalCpu = services.reduce((sum, s) => sum + s.cpuCount * s.replicas, 0)

  const maxRpmPerService = Math.max(...services.map((s) => s.requestsPerMinute), 1)

  return (
    <div className="visual-card">
      <div className="section-title">
        <span>Scaling equalizer</span>
      </div>
      <div className="equalizer-grid">
        <div className="equalizer-row header">
          <div>Service</div>
          <div>RPM</div>
          <div>Memory</div>
          <div>CPU</div>
          <div>Replicas</div>
          <div>Utilization</div>
          <div>Action</div>
        </div>
        {services.map((service, index) => {
          const memoryUtil = unitMemory > 0 ? (service.memoryMb * service.replicas) / unitMemory : 0
          const cpuUtil = unitCpu > 0 ? (service.cpuCount * service.replicas) / unitCpu : 0
          const rpmUtil = maxRpmPerService > 0 ? service.requestsPerMinute / maxRpmPerService : 0
          const util = Math.max(memoryUtil, cpuUtil, rpmUtil)
          const recommendation = util > 0.8 ? 'Increase capacity' : util > 0.5 ? 'Watch' : 'OK'
          const recommendationClass = recommendation === 'Increase capacity' ? 'watch' : recommendation === 'Watch' ? 'watch' : 'do_now'
          return (
            <div className="equalizer-row" key={index}>
              <div><strong>{service.serviceName}</strong></div>
              <div>{service.requestsPerMinute.toLocaleString()}</div>
              <div>{(service.memoryMb * service.replicas).toLocaleString()} MB</div>
              <div>{service.cpuCount * service.replicas} vCPU</div>
              <div>{service.replicas}</div>
              <div className="utilization-bar">
                <div className="utilization-fill" style={{ width: `${Math.min(util * 100, 100)}%` }} />
                <span>{Math.round(util * 100)}%</span>
              </div>
              <div className={`equalizer-action ${recommendationClass}`}>{recommendation}</div>
            </div>
          )
        })}
        <div className="equalizer-row total">
          <div><strong>Total</strong></div>
          <div>{totalRpm.toLocaleString()}</div>
          <div>{totalMemory.toLocaleString()} MB</div>
          <div>{totalCpu} vCPU</div>
          <div>{services.reduce((sum, s) => sum + s.replicas, 0)}</div>
          <div className="equalizer-summary">
            {scenario.deployment.serverType === 'SERVERLESS'
              ? 'Serverless scales automatically'
              : `Unit: ${unitMemory} MB / ${unitCpu} vCPU`}
          </div>
        </div>
      </div>
      {evaluation && (
        <div className="equalizer-insight">
          <strong>Insight:</strong> {evaluation.results[0]?.derivedMetrics.dailyRequests.toLocaleString()} daily requests ·{' '}
          {services.length} services ·{' '}
          {scenario.deployment.serverType} on {scenario.deployment.cloudProvider}
        </div>
      )}
    </div>
  )
}
