package com.scalecanvas.ai.spi.ollama;

import com.scalecanvas.ai.api.dto.AiProviderResponse;
import com.scalecanvas.ai.spi.AiProvider;
import com.scalecanvas.evaluation.api.EvaluationResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OllamaAiProvider implements AiProvider {
    private final String baseUrl;
    private final String model;
    private final HttpClient client;

    public OllamaAiProvider(
            @Value("${app.ai.ollama-base-url}") String baseUrl,
            @Value("${app.ai.model}") String model) {
        this.baseUrl = baseUrl;
        this.model = model;
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public String name() {
        return "ollama";
    }

    @Override
    public AiProviderResponse explain(EvaluationResponse evaluation, String question) {
        String prompt = buildPrompt(evaluation, question);
        String escapedPrompt = escapeJson(prompt);
        String payload = "{\"model\":\"%s\",\"stream\":false,\"messages\":[{\"role\":\"user\",\"content\":%s}]}"
                .formatted(model, escapedPrompt);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/chat"))
                    .timeout(Duration.ofSeconds(60))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return new AiProviderResponse(name(), model, "Provider returned status " + response.statusCode());
            }
            String explanation = extractMessage(response.body());
            return new AiProviderResponse(name(), model, explanation);
        } catch (Exception exception) {
            return new AiProviderResponse(name(), model, "Provider unavailable: " + exception.getMessage());
        }
    }

    private String buildPrompt(EvaluationResponse evaluation, String question) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
        Eres un arquitecto de software senior. Explica las recomendaciones de arquitectura para el siguiente escenario.
        
        Escenario: """).append(evaluation.scenarioName()).append("\n");

        evaluation.results().forEach(result -> {
            sb.append("\nVariante: ").append(result.variant()).append("\n");
            sb.append("- Daily requests: ").append(result.derivedMetrics().dailyRequests()).append("\n");
            sb.append("- Peak-hour requests: ").append(result.derivedMetrics().peakHourRequests()).append("\n");
            sb.append("- Read RPS: ").append(result.derivedMetrics().readRps()).append("\n");
            sb.append("- Write RPS: ").append(result.derivedMetrics().writeRps()).append("\n");
            sb.append("- Storage after 12 months: ").append(result.derivedMetrics().storageAfter12MonthsGb()).append(" GB\n");
            sb.append("- Allowed downtime/month: ").append(result.derivedMetrics().allowedUnavailabilityMinutesPerMonth()).append(" minutes\n");

            if (!result.recommendations().isEmpty()) {
                sb.append("\nRecomendaciones:\n");
                result.recommendations().forEach(rec -> {
                    sb.append("- ").append(rec.title())
                            .append(" [").append(rec.urgency()).append("] ")
                            .append(rec.rationale())
                            .append(" Threshold: ").append(rec.threshold())
                            .append(". Simpler alternative: ").append(rec.simplerAlternative())
                            .append("\n");
                });
            }

            if (!result.risks().isEmpty()) {
                sb.append("\nRiesgos:\n");
                result.risks().forEach(risk -> {
                    sb.append("- ").append(risk.level()).append(": ").append(risk.title()).append(" — ").append(risk.detail()).append("\n");
                });
            }
        });

        if (question != null && !question.isBlank()) {
            sb.append("\nPregunta del usuario: ").append(question.trim()).append("\n");
        } else {
            sb.append("\nPregunta del usuario: (ninguna)\n");
        }

        sb.append("\nProporciona una explicación concisa, técnica y accionable. No inventes datos. Si falta información, indícalo.");
        return sb.toString();
    }

    private String escapeJson(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }

    private String extractMessage(String body) {
        int start = body.indexOf("\"content\":\"");
        if (start < 0) return body;
        start += "\"content\":\"".length();
        int end = body.indexOf("\"", start);
        if (end < 0) return body;
        return body.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}
