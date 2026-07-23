package com.scalecanvas.observation;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.integrations")
public record IntegrationProperties(
        boolean githubEnabled,
        String githubToken,
        String githubRepository,
        boolean sonarqubeEnabled,
        String sonarqubeUrl,
        String sonarqubeToken,
        String sonarqubeProject,
        Map<String, String> headers) {
}
