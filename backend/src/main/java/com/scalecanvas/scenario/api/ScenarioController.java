package com.scalecanvas.scenario.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalecanvas.scenario.api.dto.CostComplexityItem;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import com.scalecanvas.scenario.api.dto.ScenarioSummary;
import com.scalecanvas.scenario.application.ScenarioService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scenarios")
public class ScenarioController {
    private final ScenarioService service;

    public ScenarioController(ScenarioService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<ScenarioSummary> create(@Valid @RequestBody ScenarioRequest request) {
        ScenarioSummary created = service.create(request);
        return ResponseEntity.created(URI.create("/api/v1/scenarios/" + created.id())).body(created);
    }

    @GetMapping
    List<ScenarioSummary> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    ScenarioRequest get(@PathVariable UUID id) {
        return service.get(id);
    }

    @GetMapping("/{id}/cost-complexity")
    List<CostComplexityItem> costComplexity(@PathVariable UUID id) {
        return service.estimateCostComplexity(id);
    }

    @GetMapping("/{id}/export")
    ResponseEntity<String> exportJson(@PathVariable UUID id) {
        ScenarioRequest request = service.get(id);
        try {
            String json = new ObjectMapper().writeValueAsString(request);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"scenario-" + id + ".json\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Scenario cannot be serialized", exception);
        }
    }

    @PutMapping("/{id}")
    ScenarioSummary update(@PathVariable UUID id, @Valid @RequestBody ScenarioRequest request) {
        return service.update(id, request);
    }

    @PostMapping("/{id}/duplicate")
    ResponseEntity<ScenarioSummary> duplicate(@PathVariable UUID id) {
        ScenarioSummary created = service.duplicate(id);
        return ResponseEntity.created(URI.create("/api/v1/scenarios/" + created.id())).body(created);
    }

    @PostMapping("/import")
    ResponseEntity<ScenarioSummary> importJson(@Valid @RequestBody ScenarioRequest request) {
        ScenarioSummary created = service.importScenario(request);
        return ResponseEntity.created(URI.create("/api/v1/scenarios/" + created.id())).body(created);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
