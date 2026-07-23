package com.scalecanvas.scenario.api;

import com.scalecanvas.scenario.application.ScenarioService;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scenarios")
public class DeploymentController {
    private final ScenarioService service;

    public DeploymentController(ScenarioService service) {
        this.service = service;
    }

    @GetMapping("/{id}/deployment")
    ResponseEntity<ScenarioRequest.DeploymentRequest> getDeployment(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getDeployment(id));
    }

    @PostMapping("/{id}/deployment")
    ResponseEntity<ScenarioRequest.DeploymentRequest> createDeployment(
            @PathVariable UUID id, @Valid @RequestBody ScenarioRequest.DeploymentRequest deployment) {
        return ResponseEntity.ok(service.updateDeployment(id, deployment));
    }

    @PutMapping("/{id}/deployment")
    ResponseEntity<ScenarioRequest.DeploymentRequest> updateDeployment(
            @PathVariable UUID id, @Valid @RequestBody ScenarioRequest.DeploymentRequest deployment) {
        return ResponseEntity.ok(service.updateDeployment(id, deployment));
    }
}
