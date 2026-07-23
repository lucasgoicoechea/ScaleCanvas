package com.scalecanvas.config;

import java.util.Map;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator/health")
public class HealthController {
    private final HealthIndicator aiHealthIndicator;

    public HealthController(HealthIndicator aiHealthIndicator) {
        this.aiHealthIndicator = aiHealthIndicator;
    }

    @GetMapping("/ai")
    ResponseEntity<Health> aiHealth() {
        Health health = aiHealthIndicator.health();
        return ResponseEntity.ok(health);
    }
}
