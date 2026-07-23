package com.scalecanvas.ai;

import com.scalecanvas.ai.api.dto.AiProviderResponse;
import com.scalecanvas.ai.spi.AiProvider;
import com.scalecanvas.evaluation.api.EvaluationResponse;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExplanationService {
    private final List<AiProvider> providers;
    private final boolean enabled;

    public ExplanationService(List<AiProvider> providers, @org.springframework.beans.factory.annotation.Value("${app.ai.enabled}") boolean enabled) {
        this.providers = providers;
        this.enabled = enabled;
    }

    public ExplanationResponse explain(ExplanationRequest request) {
        if (!enabled) {
            return new ExplanationResponse(
                    Instant.now(),
                    "AI explanations are disabled. Set AI_ENABLED=true and ensure a provider is reachable to use this feature.",
                    "none",
                    "none");
        }

        AiProvider provider = providers.stream().findFirst().orElse(null);
        if (provider == null) {
            return new ExplanationResponse(
                    Instant.now(),
                    "No AI providers available.",
                    "none",
                    "none");
        }

        AiProviderResponse response = provider.explain(request.evaluation(), request.question());
        return new ExplanationResponse(
                Instant.now(),
                response.explanation(),
                response.provider(),
                response.model());
    }
}
