package com.scalecanvas.observation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.springframework.stereotype.Service;

@Service
public class SonarQubeIntegrationService {

    private final HttpClient client;
    private final IntegrationProperties properties;

    public SonarQubeIntegrationService(IntegrationProperties properties) {
        this.properties = properties;
        this.client = HttpClient.newHttpClient();
    }

    public String projectStatus() {
        if (!properties.sonarqubeEnabled()
                || properties.sonarqubeUrl() == null
                || properties.sonarqubeUrl().isBlank()
                || properties.sonarqubeProject() == null
                || properties.sonarqubeProject().isBlank()) {
            return "disabled";
        }
        try {
            String base = properties.sonarqubeUrl().replaceAll("/+$", "");
            URI uri = URI.create(base + "/api/qualitygates/project_status?projectKey=" + properties.sonarqubeProject());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return "connected";
            }
            return "http_" + response.statusCode();
        } catch (Exception exception) {
            return "error";
        }
    }
}
