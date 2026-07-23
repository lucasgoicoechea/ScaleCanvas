package com.scalecanvas.ai.spi;

import com.scalecanvas.ai.api.dto.AiProviderResponse;
import com.scalecanvas.evaluation.api.EvaluationResponse;

public interface AiProvider {
    String name();
    AiProviderResponse explain(EvaluationResponse evaluation, String question);
}
