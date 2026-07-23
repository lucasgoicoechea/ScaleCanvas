package com.scalecanvas.observation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.springframework.stereotype.Service;

@Service
public class GitHubIntegrationService {

    private final HttpClient client;
    private final IntegrationProperties properties;

    public GitHubIntegrationService(IntegrationProperties properties) {
        this.properties = properties;
        this.client = HttpClient.newHttpClient();
    }

    public String repositoryStatus() {
        if (!properties.githubEnabled() || properties.githubRepository() == null || properties.githubRepository().isBlank()) {
            return "disabled";
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/repos/" + properties.githubRepository()))
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .header("User-Agent", "ScaleCanvas")
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
