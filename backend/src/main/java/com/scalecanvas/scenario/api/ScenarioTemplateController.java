package com.scalecanvas.scenario.api;

import com.scalecanvas.scenario.api.dto.ScenarioSummary;
import com.scalecanvas.scenario.application.ScenarioService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scenario-templates")
public class ScenarioTemplateController {

  public record ExampleTemplate(String id, String name, String description, String scenarioJson) {
  }

  private final ScenarioService scenarioService;

  public ScenarioTemplateController(ScenarioService scenarioService) {
    this.scenarioService = scenarioService;
  }

  @GetMapping
  List<ExampleTemplate> list() {
    return List.of(
        new ExampleTemplate("baseline-single-region", "Baseline single region", "Simple 2-tier in one AWS region", "{\"name\":\"Baseline single region\",\"services\":[...]}"),
        new ExampleTemplate("multi-region-active-active", "Multi-region active-active", "Same topology replicated across two regions", "{\"name\":\"Multi-region active-active\",\"services\":[...]}"),
        new ExampleTemplate("event-driven-serverless", "Event-driven serverless", "API Gateway + Lambda + SQS", "{\"name\":\"Event-driven serverless\",\"services\":[...]}"),
        new ExampleTemplate("gcp-gke-analytics", "GCP GKE analytics", "GKE + BigQuery + Pub/Sub baseline", "{\"name\":\"GCP GKE analytics\",\"services\":[...]}")
    );
  }
}