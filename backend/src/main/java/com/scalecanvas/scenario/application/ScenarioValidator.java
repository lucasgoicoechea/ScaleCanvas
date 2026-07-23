package com.scalecanvas.scenario.application;

import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class ScenarioValidator {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.0");

    public void validate(ScenarioRequest request) {
        var workload = request.workload();
        if (workload.readPercentage().add(workload.writePercentage()).compareTo(ONE_HUNDRED) != 0) {
            throw new InvalidScenarioException("readPercentage + writePercentage must equal 100");
        }
        if (workload.averageRps().compareTo(workload.peakRps()) > 0) {
            throw new InvalidScenarioException("averageRps must be less than or equal to peakRps");
        }
        if (workload.averagePayloadBytes() > workload.maximumPayloadBytes()) {
            throw new InvalidScenarioException("averagePayloadBytes must be less than or equal to maximumPayloadBytes");
        }
        var quality = request.quality();
        if (!(quality.targetP50Ms() <= quality.targetP95Ms()
                && quality.targetP95Ms() <= quality.targetP99Ms())) {
            throw new InvalidScenarioException("Latency targets must satisfy p50 <= p95 <= p99");
        }
        var deployment = request.deployment();
        if (deployment.serviceTopology().microservicesCount() > deployment.serviceTopology().totalServices()) {
            throw new InvalidScenarioException("microservicesCount must be less than or equal to totalServices");
        }
        if (deployment.serviceTopology().scalableServicesCount() > deployment.serviceTopology().totalServices()) {
            throw new InvalidScenarioException("scalableServicesCount must be less than or equal to totalServices");
        }
        if (deployment.serviceTopology().services() == null) {
            throw new InvalidScenarioException("serviceTopology.services must not be null");
        }
        if (!deployment.serviceTopology().services().isEmpty() && deployment.serviceTopology().services().size() > deployment.serviceTopology().totalServices()) {
            throw new InvalidScenarioException("service count must not exceed totalServices");
        }
    }
}
