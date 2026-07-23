package com.scalecanvas.evaluation.api;

import com.scalecanvas.evaluation.EvaluationService;
import com.scalecanvas.evaluation.api.dto.EvaluationTimelineItem;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluations")
public class EvaluationHistoryController {
    private final EvaluationService service;

    public EvaluationHistoryController(EvaluationService service) {
        this.service = service;
    }

    @GetMapping
    List<EvaluationSummary> list() {
        return service.listEvaluations();
    }

    @GetMapping("/scenario/{scenarioId}")
    List<EvaluationTimelineItem> getTimelineByScenario(@PathVariable UUID scenarioId) {
        return service.getEvaluationTimeline(scenarioId);
    }

    @GetMapping("/{id}")
    ResponseEntity<EvaluationResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getEvaluation(id));
    }
}
