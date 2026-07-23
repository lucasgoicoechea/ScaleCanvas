package com.scalecanvas.ai.health;

import com.scalecanvas.ai.spi.AiProvider;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AiHealthIndicator implements HealthIndicator {
    private final AiProvider provider;

    public AiHealthIndicator(AiProvider provider) {
        this.provider = provider;
    }

    @Override
    public Health health() {
        if (provider == null) {
            return Health.down().withDetail("provider", "none").build();
        }
        try {
            String name = provider.name();
            return Health.up().withDetail("provider", name).build();
        } catch (Exception exception) {
            return Health.down().withDetail("error", exception.getMessage()).build();
        }
    }
}
