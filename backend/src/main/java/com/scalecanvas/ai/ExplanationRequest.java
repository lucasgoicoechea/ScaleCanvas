package com.scalecanvas.ai;

import com.scalecanvas.evaluation.api.EvaluationResponse;
import jakarta.validation.constraints.NotNull;

public record ExplanationRequest(@NotNull EvaluationResponse evaluation, String question) {
}
