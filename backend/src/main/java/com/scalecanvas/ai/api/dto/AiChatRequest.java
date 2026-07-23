package com.scalecanvas.ai.api.dto;

public record AiChatRequest(String model, String prompt, boolean stream) {
}
