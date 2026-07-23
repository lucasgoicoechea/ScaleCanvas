package com.scalecanvas.observation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/integrations")
public class IntegrationController {

    private final GitHubIntegrationService gitHub;
    private final SonarQubeIntegrationService sonarQube;

    public IntegrationController(GitHubIntegrationService gitHub, SonarQubeIntegrationService sonarQube) {
        this.gitHub = gitHub;
        this.sonarQube = sonarQube;
    }

    @GetMapping("/github/status")
    public String githubStatus() {
        return gitHub.repositoryStatus();
    }

    @GetMapping("/sonarqube/status")
    public String sonarQubeStatus() {
        return sonarQube.projectStatus();
    }

    @GetMapping("/telemetry-status")
    public String telemetryStatus() {
        return "open";
    }
}
