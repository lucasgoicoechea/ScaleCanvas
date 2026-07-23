package com.scalecanvas.scenario.application;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScenarioNotFoundException extends RuntimeException {
    public ScenarioNotFoundException(UUID id) {
        super("Scenario not found: " + id);
    }
}
