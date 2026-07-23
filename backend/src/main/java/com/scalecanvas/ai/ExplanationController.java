package com.scalecanvas.ai;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/explanations")
public class ExplanationController {
    private final ExplanationService service;

    public ExplanationController(ExplanationService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<ExplanationResponse> explain(@Valid @RequestBody ExplanationRequest request) {
        return ResponseEntity.ok(service.explain(request));
    }
}
