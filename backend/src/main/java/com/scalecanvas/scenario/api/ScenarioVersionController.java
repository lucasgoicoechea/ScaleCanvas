package com.scalecanvas.scenario.api;

import com.scalecanvas.scenario.application.ScenarioVersionService;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import com.scalecanvas.scenario.api.dto.ScenarioVersionSummary;
import com.scalecanvas.scenario.infrastructure.ScenarioVersionEntity;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scenario-versions")
public class ScenarioVersionController {
    private final ScenarioVersionService service;

    public ScenarioVersionController(ScenarioVersionService service) {
        this.service = service;
    }

    @PostMapping("/{scenarioId}")
    ResponseEntity<ScenarioVersionEntity> create(
            @PathVariable UUID scenarioId,
            @RequestParam(defaultValue = "v1") String versionLabel,
            @Valid @RequestBody ScenarioRequest request) {
        return ResponseEntity.ok(service.createVersion(scenarioId, versionLabel, request));
    }

    @GetMapping("/{scenarioId}")
    List<ScenarioVersionSummary> list(@PathVariable UUID scenarioId) {
        return service.listVersions(scenarioId).stream()
                .map(entity -> new ScenarioVersionSummary(
                        entity.getId(),
                        entity.getScenarioId(),
                        entity.getVersionLabel(),
                        entity.getCreatedAt()))
                .toList();
    }
}
