package com.scalecanvas.evaluation.api;

import com.scalecanvas.evaluation.AdrGenerator;
import com.scalecanvas.evaluation.EvaluationService;
import com.scalecanvas.evaluation.api.dto.AdrResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluations")
public class EvaluationController {
    private final EvaluationService service;
    private final AdrGenerator adrGenerator;

    public EvaluationController(EvaluationService service, AdrGenerator adrGenerator) {
        this.service = service;
        this.adrGenerator = adrGenerator;
    }

    @PostMapping
    ResponseEntity<EvaluationResponse> evaluate(@Valid @RequestBody EvaluationRequest request) {
        EvaluationResponse response = service.evaluate(request);
        return ResponseEntity.created(URI.create("/api/v1/evaluations/" + response.evaluationId())).body(response);
    }

    @GetMapping("/{id}/adr")
    ResponseEntity<AdrResponse> generateAdr(@PathVariable UUID id) {
        EvaluationResponse evaluation = service.getEvaluation(id);
        return ResponseEntity.ok(adrGenerator.generate(evaluation));
    }
}
