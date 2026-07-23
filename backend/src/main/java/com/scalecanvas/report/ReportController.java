package com.scalecanvas.report;

import com.scalecanvas.evaluation.api.EvaluationResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final MarkdownReportService service;

    public ReportController(MarkdownReportService service) {
        this.service = service;
    }

    @PostMapping(value = "/markdown", produces = "text/markdown")
    ResponseEntity<String> markdown(@RequestBody EvaluationResponse response) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/markdown"))
                .body(service.generate(response));
    }
}
