package com.scalecanvas.scenario.api;

import com.scalecanvas.scenario.application.ScenarioService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scenarios")
public class ScalingController {
    private final ScenarioService service;

    public ScalingController(ScenarioService service) {
        this.service = service;
    }

    @GetMapping("/{id}/scaling-matrix")
    ResponseEntity<ScalingMatrixResponse> scalingMatrix(@PathVariable UUID id) {
        var deployment = service.getDeployment(id);
        List<String> services = deployment.serviceTopology().services().stream()
                .map(s -> s.serviceName())
                .toList();
        int maxReplicas = deployment.serviceTopology().services().stream()
                .mapToInt(s -> Math.max(1, s.replicas()))
                .max().orElse(1);
        List<MatrixCell> cells = new ArrayList<>();
        for (int serverIndex = 0; serverIndex < maxReplicas; serverIndex++) {
            for (var service : deployment.serviceTopology().services()) {
                double util = capacityPercent(
                        service.memoryMb(), service.cpuCount(),
                        deployment.minimumUnitMemoryMb(), deployment.minimumUnitCpuCount());
                long rpmShare = service.replicas() > 0 ? service.requestsPerMinute() / service.replicas() : 0;
                cells.add(new MatrixCell(service.serviceName(), serverIndex, util, rpmShare, service.replicas()));
            }
        }
        return ResponseEntity.ok(new ScalingMatrixResponse(
                deployment.serverType().name(),
                deployment.cloudProvider().name(),
                deployment.minimumUnitMemoryMb(),
                deployment.minimumUnitCpuCount(),
                services,
                maxReplicas,
                cells));
    }

    private double capacityPercent(int serviceMemory, int serviceCpu, int unitMemory, int unitCpu) {
        double memory = (double) serviceMemory / Math.max(unitMemory, 1);
        double cpu = (double) serviceCpu / Math.max(unitCpu, 1);
        return Math.max(memory, cpu) * 100;
    }
}
