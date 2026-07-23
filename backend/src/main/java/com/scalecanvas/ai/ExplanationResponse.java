package com.scalecanvas.ai;

import java.time.Instant;

public record ExplanationResponse(Instant generatedAt, String explanation, String provider, String model) {
}
